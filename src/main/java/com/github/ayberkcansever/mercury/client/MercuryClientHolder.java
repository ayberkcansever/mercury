package com.github.ayberkcansever.mercury.client;

import java.util.HashMap;
import java.util.Map;

public class MercuryClientHolder {

    private static Map<String, MercuryClient> map = new HashMap<>();

    public static MercuryClient getClient(String id) {
        return map.get(id);
    }

    public static void putClient(String id, MercuryClient mercuryClient) {
        map.put(id, mercuryClient);
    }

    public static void removeClient(String id) {
        map.remove(id);
    }
}
