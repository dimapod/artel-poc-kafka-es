package com.artel.poc.trip.engine;

import com.artel.poc.acq.TripMessage;
import com.artel.poc.acq.TripMessageType;
import com.artel.poc.trip.Trip;
import com.artel.poc.trip.engine.bean.VehicleTrips;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

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
    public void should_verify_is_new() throws Exception {
        assertThat(vehicleTrips.isNew(prepareMessage(100L, 10L, START))).isTrue();
        assertThat(vehicleTrips.isNew(prepareMessage(100L, 10L, END))).isTrue();

        vehicleTrips.setPendingMessage(prepareMessage(100L, 10L, START));
        assertThat(vehicleTrips.isNew(prepareMessage(100L, 10L, START))).isFalse();
        assertThat(vehicleTrips.isNew(prepareMessage(100L, 10L, END))).isTrue();
        assertThat(vehicleTrips.isNew(prepareMessage(100L, 1L, END))).isFalse();
        assertThat(vehicleTrips.isNew(prepareMessage(100L, 200L, END))).isTrue();

        vehicleTrips.setPendingMessage(null);
        vehicleTrips.addTrip(new Trip(prepareMessage(100L, 10L, START), prepareMessage(100L, 10L, END)));
        assertThat(vehicleTrips.isNew(prepareMessage(100L, 10L, START))).isFalse();
        assertThat(vehicleTrips.isNew(prepareMessage(100L, 10L, END))).isFalse();
        assertThat(vehicleTrips.isNew(prepareMessage(100L, 11L, START))).isTrue();
        assertThat(vehicleTrips.isNew(prepareMessage(100L, 11L, END))).isTrue();
    }

    private TripMessage prepareMessage(long imei, long tripId, TripMessageType start) {
        TripMessage tripMessage = new TripMessage();
        tripMessage.setImei(imei);
        tripMessage.setTripId(tripId);
        tripMessage.setType(start);
        return tripMessage;
    }
}