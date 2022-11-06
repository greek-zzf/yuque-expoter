package com.yuque.greek;

public class YuqueClientFactory {

    private static YuqueClient client;

    public static YuqueClient getInstance() {
        return client;
    }

    public static void initClient(String userId, String token) {
        client = new YuqueClient(userId, token);
    }
}
