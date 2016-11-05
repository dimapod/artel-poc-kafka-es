package com.artel.poc.indexer.service;

import com.artel.poc.indexer.service.bean.Result;
import com.artel.poc.indexer.service.bean.ResultAdminQuery;
import com.artel.poc.indexer.service.bean.TripJson;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static java.util.Collections.singletonList;

@Service
public class TripCreatorService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ObjectMapper objectMapper;

    private static final List<String> NUMERIC_FIELDS =
            Arrays.asList("id", "legal_entity_id_fk", "vehicle_id_fk", "from_date", "to_date", "validation_date",
                    "distance_pro", "distance_private", "distance_urban",
                    "distance_road",
                    "distance_highway",
                    "distance_daytime",
                    "distance_in_night",
                    "duration_pro",
                    "duration_private",
                    "duration_daytime",
                    "duration_at_dusk",
                    "duration_in_night",
                    "currency_id_fk",
                    "cost_ht_in_currency",
                    "cost_ht_in_euro",
                    "cost_tva_in_currency",
                    "cost_tva_in_euro",
                    "vehicle_stop_duration",
                    "safety_score",
                    "fuel_score",
                    "update_date",
                    "telematics_user_id_fk",
                    "distance_at_dusk",
                    "duration_urban",
                    "duration_road",
                    "duration_highway",
                    "hidden_mode",
                    "trip_stop_status",
                    "insertion_date",
                    "summary_tag_daily",
                    "summary_tag_weekly",
                    "summary_tag_monthly"
            );


    public TripJson createTripJson(List<Result> tripFields) {

        ObjectNode rootNode = objectMapper.createObjectNode();
        long id = -1;

        for (Result field : tripFields) {
            if (isNumeric(field)) {
                try {
                    long value = Double.valueOf(field.getValue()).longValue();
                    rootNode.put(field.getColName().toLowerCase(), value);

                    if (isId(field)) {
                        id = value;
                    }
                } catch (NumberFormatException e) {
                    rootNode.put(field.getColName().toLowerCase(), field.getValue());
                }
            } else {
                rootNode.put(field.getColName().toLowerCase(), field.getValue());
            }
        }

        return new TripJson(id, rootNode.toString());
    }

    private boolean isId(Result field) {
        return "id".equals(field.getColName().toLowerCase());
    }

    private boolean isNumeric(Result field) {
        return NUMERIC_FIELDS.contains(field.getColName().toLowerCase())
                && field.getValue() != null && !field.getValue().trim().equals("");
    }

}
