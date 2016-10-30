package com.artel.poc.trip.engine.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

@Component
public class TripContainer {

    Map<Long, VehicleTrips> vehicles = new HashMap<>();
    TreeMap<Long, Acknowledgment> acks = new TreeMap<>();

    public Map<Long, VehicleTrips> getVehicles() {
        return vehicles;
    }

    public void setVehicles(Map<Long, VehicleTrips> vehicles) {
        this.vehicles = vehicles;
    }

    @JsonIgnore
    public TreeMap<Long, Acknowledgment> getAcks() {
        return acks;
    }

    public Set<Long> getAcksKeys() {
        return acks.keySet();
    }

    public void setAcks(TreeMap<Long, Acknowledgment> acks) {
        this.acks = acks;
    }

    @Override
    public String toString() {
        return "TripService{" +
                "vehicles=" + vehicles +
                ", acks=" + acks.keySet() +
                '}';
    }
}
