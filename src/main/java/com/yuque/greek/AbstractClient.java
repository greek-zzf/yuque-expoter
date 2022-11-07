package com.yuque.greek;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuque.greek.entity.resp.Result;
import com.yuque.greek.entity.resp.User;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
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
        HttpRequest request = baseRequestBuilder(path).GET()
                .build();
        try {
            return baseRequest(request);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String baseRequest(HttpRequest request) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (200 == response.statusCode()) {
            return response.body();
        }

        throw new RuntimeException("访问失败！");
    }

    private HttpRequest.Builder baseRequestBuilder(String path) {
        return HttpRequest.newBuilder(URI.create(host + path))
                .header("Content-Type", "application/json")
                .header("User-Agent", "chrome")
                .header("X-Auth-Token", accessToken);
    }

    private User initUser(String accessToken) {
        HttpRequest request = HttpRequest.newBuilder(URI.create("https://greek-zzf.yuque.com/api/v2/user"))
                .header("Content-Type", "application/json")
                .header("User-Agent", "chrome")
                .header("X-Auth-Token", accessToken)
                .build();
        try {
            return asObject(baseRequest(request), User.class).getData();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("获取用户信息失败，请检查 token 信息！");
        }

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
