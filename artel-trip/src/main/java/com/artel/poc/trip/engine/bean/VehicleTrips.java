package com.artel.poc.trip.engine.bean;

import com.artel.poc.acq.TripMessage;
import com.artel.poc.trip.Trip;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VehicleTrips {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private List<Trip> tripList = new ArrayList<>();
    private TripMessage pendingMessage;
    private long offset;

    public Optional<Long> processMessage(TripMessage tripMessage, long offset) {
        if (!tripList.isEmpty() && tripMessage.getTripId() <= tripList.get(tripList.size() - 1).getTripId()) {
            logger.debug("Message already processed {}", tripMessage);
            return Optional.empty();
        }

        if (pendingMessage != null
                && pendingMessage.getTripId() != tripMessage.getTripId()) {
            logger.debug("Missing messages: start id {} - received id {}", pendingMessage.getTripId(), tripMessage.getTripId());
            saveTrip(this.pendingMessage, null);
            this.pendingMessage = null;
        }

        // Start
        if (tripMessage.isStart()) {
            this.pendingMessage = tripMessage;
            this.offset = offset;
            return Optional.empty();
        }

        // End
        Optional<Long> response;
        if (this.pendingMessage == null) {
            logger.debug("Missing start");
            response = Optional.empty();
        } else {
            response = Optional.of(this.offset);
        }

        saveTrip(this.pendingMessage, tripMessage);
        this.pendingMessage = null;
        return response;
    }

    private Trip saveTrip(TripMessage startMessage, TripMessage stopMessage) {
        Trip trip = new Trip(startMessage, stopMessage);
        logger.debug("Save trip {}", trip);
        tripList.add(trip);
        return trip;
    }

    public List<Trip> getTripList() {
        return tripList;
    }

    public TripMessage getPendingMessage() {
        return pendingMessage;
    }

    public long getOffset() {
        return offset;
    }

    @Override
    public String toString() {
        return "VehicleTrips{" +
                "tripList=" + tripList +
                ", pendingMessage=" + pendingMessage +
                ", offset=" + offset +
                '}';
    }
}
