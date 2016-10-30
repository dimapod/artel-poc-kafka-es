package com.artel.poc.trip.engine;

import com.artel.poc.acq.TripMessage;
import com.artel.poc.trip.engine.bean.TripContainer;
import com.artel.poc.trip.engine.bean.VehicleTrips;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TripService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

//    Map<Long, VehicleTrips> vehicles = new HashMap<>();
//    TreeMap<Long, Acknowledgment> acks = new TreeMap<>();

    @Autowired
    private TripContainer tripContainer;

    public void process(TripMessage tripMessage, long offset, Acknowledgment acknowledgment) {
        // Store ack for start messages
        if (tripMessage.isStart()) {
            tripContainer.getAcks().put(offset, acknowledgment);
        }

        // Process message
        VehicleTrips vehicleTrips = tripContainer.getVehicles().get(tripMessage.getImei());
        if (vehicleTrips == null) {
            // todo initialize VehicleTrips with current day trips of this vehicle (from ES)
            vehicleTrips = new VehicleTrips();
            tripContainer.getVehicles().put(tripMessage.getImei(), vehicleTrips);
        }
        Optional<Long> startMessageOffset = vehicleTrips.processMessage(tripMessage, offset);

        // Commit offset
        startMessageOffset.ifPresent(this::commit);
    }

    private void commit(long offsetToCommit) {
        if (offsetToCommit == tripContainer.getAcks().firstKey()) {
            tripContainer.getAcks().get(offsetToCommit).acknowledge();
            logger.debug("Commit {}", offsetToCommit);
        }
        tripContainer.getAcks().remove(offsetToCommit);
    }

}
