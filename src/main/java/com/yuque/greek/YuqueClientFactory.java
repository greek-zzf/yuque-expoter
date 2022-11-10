package com.yuque.greek;

public class YuqueClientFactory {

    private static YuqueClient client;

    public static YuqueClient getInstance() {
        return client;
    }

    public static YuqueClient initClient() {
        client = new YuqueClient(getToken());
        return client;
    }

    private static String getToken() {
        String token = System.getProperty("token");
        if (null == token || token.isEmpty()){
            throw new RuntimeException("请指定token信息!");
        }

        return token;
    }
}
