package com.artel.poc.indexer.service;

import com.artel.poc.indexer.service.bean.TripJson;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripBulkService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${elasticsearch.index:artel}")
    private String indexName;

    @Value("${elasticsearch.type:trip}")
    private String typeName;

    @Autowired
    private TransportClient client;

    public void bulkToEs(List<TripJson> tripJsons) {
        BulkRequestBuilder bulkRequest = client.prepareBulk();

        for (TripJson tripJson : tripJsons) {
            IndexRequestBuilder index = client.prepareIndex(indexName, typeName, String.valueOf(tripJson.getId()))
                    .setId(String.valueOf(tripJson.getId()))
                    .setSource(tripJson.getJson());
            bulkRequest.add(index);
        }

        BulkResponse bulkResponse = bulkRequest.get();
        if (bulkResponse.hasFailures()) {
            logger.warn("Warning");
        }
    }
}
