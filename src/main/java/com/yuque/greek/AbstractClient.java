package com.yuque.greek;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kevinsawicki.http.HttpRequest;
import com.yuque.greek.entity.Result;
import com.yuque.greek.entity.User;

import java.util.List;

public class AbstractClient {

    private final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    final String host;
    final String accessToken;

    final User currentUser;

    AbstractClient(String accessToken) {
        this.accessToken = accessToken;
        this.currentUser = initUser(accessToken);
        this.host = "https://${userId}.yuque.com/api/v2".replace("${userId}", currentUser.getId().toString());
    }


    protected String getRequest(String path) {
        HttpRequest request = setYuqueHeader(HttpRequest.get(host + path));

        if (request.ok() && !request.isBodyEmpty()) {
            return request.body();
        }

        throw new RuntimeException("访问失败！");
    }

    private HttpRequest setYuqueHeader(HttpRequest request) {
        return request.header("Content-Type", "application/json")
                .header("User-Agent", "chrome")
                .header("X-Auth-Token", accessToken);
    }

    private User initUser(String accessToken) {
        HttpRequest request = HttpRequest.get("https://greek-zzf.yuque.com/api/v2/user")
                .header("Content-Type", "application/json")
                .header("User-Agent", "chrome")
                .header("X-Auth-Token", accessToken);

        if (request.ok()) {
            return asObject(request.body(), User.class).getData();
        }
        throw new RuntimeException("获取用户信息失败，请检查 token 信息!");
    }

    protected <T> Result<List<T>> asList(String response, Class<T> klass) {
        Result<List<T>> result;
        try {
            JavaType beanType = objectMapper.getTypeFactory().constructCollectionType(List.class, klass);
            JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Result.class, beanType);
            result = objectMapper.readValue(response, responseType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    protected <T> Result<T> asObject(String response, Class<T> klass) {
        Result<T> result;
        try {
            JavaType responseType = objectMapper.getTypeFactory().constructParametricType(Result.class, klass);
            result = objectMapper.readValue(response, responseType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
