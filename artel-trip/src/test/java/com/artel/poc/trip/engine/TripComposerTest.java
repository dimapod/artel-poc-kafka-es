package com.artel.poc.trip.engine;

import com.artel.poc.acq.TripMessage;
import com.artel.poc.acq.TripMessageType;
import com.artel.poc.trip.Trip;
import com.artel.poc.trip.engine.bean.VehicleTrips;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static com.artel.poc.acq.TripMessageType.END;
import static com.artel.poc.acq.TripMessageType.START;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class TripComposerTest {

    @InjectMocks
    private TripComposer tripComposer;

    @Mock
    private TripService tripService;

    @Test
    public void should_process_start() {
        // given
        VehicleTrips vehicleTrips = new VehicleTrips();

        // when
        TripMessage start = prepareMessage(100L, 10L, START);
        Optional<Long> offset = tripComposer.processMessage(vehicleTrips, start, 1L);

        // then
        assertThat(offset.isPresent()).isFalse();
        assertThat(vehicleTrips.getOffset()).isEqualTo(1L);
        assertThat(vehicleTrips.getPendingMessage()).isEqualTo(start);
        assertThat(vehicleTrips.getTripList()).isEmpty();
        verifyZeroInteractions(tripService);
    }

    @Test
    public void should_process_end() {
        // given
        VehicleTrips vehicleTrips = new VehicleTrips();
        tripComposer.processMessage(vehicleTrips, prepareMessage(100L, 10L, START), 1L);
        TripMessage end = prepareMessage(100L, 10L, END);

        // when
        Optional<Long> offset = tripComposer.processMessage(vehicleTrips, end, 2L);

        // then
        assertThat(offset.isPresent()).isTrue();
        assertThat(offset.get()).isEqualTo(1L);
        assertThat(vehicleTrips.getTripList()).hasSize(1);
        assertThat(vehicleTrips.getTripList().get(0).getStartMessage()).isNotNull();
        assertThat(vehicleTrips.getTripList().get(0).getEndMessage()).isNotNull();
        assertThat(vehicleTrips.getPendingMessage()).isNull();
        verify(tripService).saveTrip(isA(Trip.class));
    }

    @Test
    public void should_process_end_without_start() {
        // given
        VehicleTrips vehicleTrips = new VehicleTrips();
        tripComposer.processMessage(vehicleTrips, prepareMessage(100L, 10L, START), 1L);
        TripMessage start2 = prepareMessage(100L, 11L, START);

        // when
        Optional<Long> offset = tripComposer.processMessage(vehicleTrips, start2, 2L);

        // then
        assertThat(offset.isPresent()).isTrue();
        assertThat(offset.get()).isEqualTo(1L);
        assertThat(vehicleTrips.getTripList()).hasSize(1);
        assertThat(vehicleTrips.getTripList().get(0).getStartMessage()).isNotNull();
        assertThat(vehicleTrips.getTripList().get(0).getEndMessage()).isNull();
        assertThat(vehicleTrips.getPendingMessage()).isEqualTo(start2);
        verify(tripService).saveTrip(isA(Trip.class));
    }

    @Test
    public void should_process_start_after_start() {
        // given
        VehicleTrips vehicleTrips = new VehicleTrips();
        TripMessage end = prepareMessage(100L, 10L, END);

        // when
        Optional<Long> offset = tripComposer.processMessage(vehicleTrips, end, 2L);

        // then
        assertThat(offset.isPresent()).isFalse();
        assertThat(vehicleTrips.getTripList()).hasSize(1);
        assertThat(vehicleTrips.getTripList().get(0).getStartMessage()).isNull();
        assertThat(vehicleTrips.getTripList().get(0).getEndMessage()).isNotNull();
        assertThat(vehicleTrips.getPendingMessage()).isNull();
        verify(tripService).saveTrip(isA(Trip.class));
    }

    @Test
    public void should_process_missing_trip() {
        // given
        VehicleTrips vehicleTrips = new VehicleTrips();
        tripComposer.processMessage(vehicleTrips, prepareMessage(100L, 10L, START), 1L);

        // when
        Optional<Long> offset = tripComposer.processMessage(vehicleTrips, prepareMessage(100L, 12L, END), 2L);

        // then
        assertThat(offset.isPresent()).isTrue();
        assertThat(offset.get()).isEqualTo(1L);
        assertThat(vehicleTrips.getTripList()).hasSize(2);
        assertThat(vehicleTrips.getTripList().get(0).getStartMessage()).isNotNull();
        assertThat(vehicleTrips.getTripList().get(0).getEndMessage()).isNull();
        assertThat(vehicleTrips.getTripList().get(1).getStartMessage()).isNull();
        assertThat(vehicleTrips.getTripList().get(1).getEndMessage()).isNotNull();
        assertThat(vehicleTrips.getPendingMessage()).isNull();
        verify(tripService, times(2)).saveTrip(isA(Trip.class));
    }

    private TripMessage prepareMessage(long imei, long tripId, TripMessageType start) {
        TripMessage tripMessage = new TripMessage();
        tripMessage.setImei(imei);
        tripMessage.setTripId(tripId);
        tripMessage.setType(start);
        return tripMessage;
    }

}