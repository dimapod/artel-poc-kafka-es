package com.artel.poc.trip;

import com.artel.poc.acq.TripMessage;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Trip {
    TripMessage startMessage, endMessage;

    public Trip() {
    }

    public Trip(TripMessage startMessage, TripMessage endMessage) {
        this.startMessage = startMessage;
        this.endMessage = endMessage;
    }

    public TripMessage getStartMessage() {
        return startMessage;
    }

    public void setStartMessage(TripMessage startMessage) {
        this.startMessage = startMessage;
    }

    public TripMessage getEndMessage() {
        return endMessage;
    }

    public void setEndMessage(TripMessage endMessage) {
        this.endMessage = endMessage;
    }

    @JsonIgnore
    public long getTripId() {
        return this.startMessage != null ? this.startMessage.getTripId() : this.endMessage.getTripId();
    }

    @Override
    public String toString() {
        return "Trip{" +
                "startMessage=" + startMessage +
                ", endMessage=" + endMessage +
                '}';
    }
}
