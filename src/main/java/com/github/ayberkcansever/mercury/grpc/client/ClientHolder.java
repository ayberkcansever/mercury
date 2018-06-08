package com.github.ayberkcansever.mercury.grpc.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ClientHolder {

    private static final Logger LOG = LoggerFactory.getLogger(ClientHolder.class);
    private Map<String, GRpcClient> map = new HashMap<>();

    public GRpcClient getNode(String serverUrl) {
        return map.get(serverUrl);
    }

    public void addNewNode(String serverUrl) {
        if(!map.containsKey(serverUrl)) {
            map.put(serverUrl, new GRpcClient(serverUrl));
            LOG.info(serverUrl + " added to nodes.");
        } else {
            LOG.info(serverUrl + " not added: is already in nodes.");
        }
        LOG.info("Nodes after add attempt: " + map);
    }

    public void removeNode(String serverUrl) {
        if(map.containsKey(serverUrl)) {
            map.remove(serverUrl);
            LOG.info(serverUrl + " removed from nodes.");
        } else {
            LOG.info(serverUrl + " not removed: not in nodes.");
        }
        LOG.info("Nodes after remove attempt" + map);
    }

}
