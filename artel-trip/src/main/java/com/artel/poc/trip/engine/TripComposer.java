package com.artel.poc.trip.engine;

import com.artel.poc.acq.TripMessage;
import com.artel.poc.trip.Trip;
import com.artel.poc.trip.engine.bean.VehicleTrips;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TripComposer {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TripService tripService;

    public Optional<Long> processMessage(VehicleTrips vehicleTrips, TripMessage tripMessage, long offset) {
        if (!vehicleTrips.isNew(tripMessage)) {
            logger.warn("Old message: {}", tripMessage);
            return Optional.empty();
        }

        Optional<Long> response = Optional.empty();
        if (vehicleTrips.getPendingMessage() != null
                && vehicleTrips.getPendingMessage().getTripId() != tripMessage.getTripId()) {
            logger.debug("Missing messages: start id {} - received id {}", vehicleTrips.getPendingMessage().getTripId(), tripMessage.getTripId());
            addTrip(vehicleTrips, vehicleTrips.getPendingMessage(), null);
            response = Optional.of(vehicleTrips.getOffset());
            vehicleTrips.setPendingMessage(null);
        }

        // Start
        if (tripMessage.isStart()) {
            vehicleTrips.setPendingMessage(tripMessage);
            vehicleTrips.setOffset(offset);
            return response;
        }

        // End
        if (vehicleTrips.hasPending()) {
            response = Optional.of(vehicleTrips.getOffset());
        } else {
            logger.debug("Missing start");
        }

        addTrip(vehicleTrips, vehicleTrips.getPendingMessage(), tripMessage);
        vehicleTrips.setPendingMessage(null);
        return response;
    }

    private Trip addTrip(VehicleTrips vehicleTrips, TripMessage startMessage, TripMessage stopMessage) {
        Trip trip = new Trip(startMessage, stopMessage);
        logger.debug("Save trip {}", trip);
        vehicleTrips.addTrip(trip);

        // Save it to Kafka log
        tripService.saveTrip(trip);
        return trip;
    }
}
