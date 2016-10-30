package com.artel.poc.trip.engine;

import com.artel.poc.acq.TripMessage;
import com.artel.poc.acq.TripMessageType;
import com.artel.poc.trip.engine.bean.VehicleTrips;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static com.artel.poc.acq.TripMessageType.END;
import static com.artel.poc.acq.TripMessageType.START;
import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class VehicleTripsTest {

    private VehicleTrips vehicleTrips;

    @Before
    public void setUp() {
        vehicleTrips = new VehicleTrips();
    }

    @Test
    public void should_process_start() {
        TripMessage start = prepareMessage(100L, 10L, START);
        Optional<Long> offset = vehicleTrips.processMessage(start, 1L);
        assertThat(offset.isPresent()).isFalse();
        assertThat(vehicleTrips.getOffset()).isEqualTo(1L);
        assertThat(vehicleTrips.getPendingMessage()).isEqualTo(start);
        assertThat(vehicleTrips.getTripList()).isEmpty();
    }

    @Test
    public void should_process_end() {
        // given
        vehicleTrips.processMessage(prepareMessage(100L, 10L, START), 1L);
        TripMessage end = prepareMessage(100L, 10L, END);

        // when
        Optional<Long> offset = vehicleTrips.processMessage(end, 2L);

        // then
        assertThat(offset.isPresent()).isTrue();
        assertThat(offset.get()).isEqualTo(1L);
        assertThat(vehicleTrips.getTripList()).hasSize(1);
        assertThat(vehicleTrips.getTripList().get(0).getStartMessage()).isNotNull();
        assertThat(vehicleTrips.getTripList().get(0).getEndMessage()).isNotNull();
        assertThat(vehicleTrips.getPendingMessage()).isNull();
    }

    @Test
    public void should_process_end_without_start() {
        // given
        vehicleTrips.processMessage(prepareMessage(100L, 10L, START), 1L);
        TripMessage start2 = prepareMessage(100L, 11L, START);

        // when
        Optional<Long> offset = vehicleTrips.processMessage(start2, 2L);

        // then
        assertThat(offset.isPresent()).isFalse();
        assertThat(vehicleTrips.getTripList()).hasSize(1);
        assertThat(vehicleTrips.getTripList().get(0).getStartMessage()).isNotNull();
        assertThat(vehicleTrips.getTripList().get(0).getEndMessage()).isNull();
        assertThat(vehicleTrips.getPendingMessage()).isEqualTo(start2);
    }

    @Test
    public void should_process_start_after_start() {
        // given
        TripMessage end = prepareMessage(100L, 10L, END);

        // when
        Optional<Long> offset = vehicleTrips.processMessage(end, 2L);

        // then
        assertThat(offset.isPresent()).isFalse();
        assertThat(vehicleTrips.getTripList()).hasSize(1);
        assertThat(vehicleTrips.getTripList().get(0).getStartMessage()).isNull();
        assertThat(vehicleTrips.getTripList().get(0).getEndMessage()).isNotNull();
        assertThat(vehicleTrips.getPendingMessage()).isNull();
    }

    @Test
    public void should_process_missing_trip() {
        // given
        vehicleTrips.processMessage(prepareMessage(100L, 10L, START), 1L);
        TripMessage start = prepareMessage(100L, 12L, END);

        // when
        Optional<Long> offset = vehicleTrips.processMessage(start, 2L);

        // then
        assertThat(offset.isPresent()).isFalse();
        assertThat(vehicleTrips.getTripList()).hasSize(2);
        assertThat(vehicleTrips.getTripList().get(0).getStartMessage()).isNotNull();
        assertThat(vehicleTrips.getTripList().get(0).getEndMessage()).isNull();
        assertThat(vehicleTrips.getTripList().get(1).getStartMessage()).isNull();
        assertThat(vehicleTrips.getTripList().get(1).getEndMessage()).isNotNull();
        assertThat(vehicleTrips.getPendingMessage()).isNull();
    }

    private TripMessage prepareMessage(long imei, long tripId, TripMessageType start) {
        TripMessage tripMessage = new TripMessage();
        tripMessage.setImei(imei);
        tripMessage.setTripId(tripId);
        tripMessage.setType(start);
        return tripMessage;
    }
}