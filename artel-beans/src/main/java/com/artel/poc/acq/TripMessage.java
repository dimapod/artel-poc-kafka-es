package com.artel.poc.acq;

public class TripMessage {
    private long imei;
    private long tripId;
    private TripMessageType type;

    private long mileage;
    private String events;

    public long getImei() {
        return imei;
    }

    public void setImei(long imei) {
        this.imei = imei;
    }

    public long getTripId() {
        return tripId;
    }

    public void setTripId(long tripId) {
        this.tripId = tripId;
    }

    public TripMessageType getType() {
        return type;
    }

    public void setType(TripMessageType type) {
        this.type = type;
    }

    public long getMileage() {
        return mileage;
    }

    public void setMileage(long mileage) {
        this.mileage = mileage;
    }

    public String getEvents() {
        return events;
    }

    public void setEvents(String events) {
        this.events = events;
    }

    public boolean isStart() {
        return TripMessageType.START.equals(this.getType());
    }

    @Override
    public String toString() {
        return "TripMessage{" +
                "imei=" + imei +
                ", tripId=" + tripId +
                ", type=" + type +
                ", mileage=" + mileage +
                ", events='" + events + '\'' +
                '}';
    }
}

