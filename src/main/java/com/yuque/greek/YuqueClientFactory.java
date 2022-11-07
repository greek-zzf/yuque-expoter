package com.yuque.greek;

public class YuqueClientFactory {

    private static YuqueClient client;

    public static YuqueClient getInstance() {
        return client;
    }

    public static YuqueClient initClient(String token) {
        client = new YuqueClient(token);
        return client;
    }
}
