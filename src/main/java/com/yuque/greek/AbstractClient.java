package com.yuque.greek;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuque.greek.entity.resp.Result;

import javax.management.MBeanAttributeInfo;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class AbstractClient {


    private final String host;
    private final String userId;
    private final String accessToken;

    AbstractClient(String userId, String accessToken) {
        this.accessToken = accessToken;
        this.userId = userId;
        this.host = "https://${userId}.yuque.com/api/v2".replace("${userId}", userId);
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

    protected <T> T postRequest(String path, TypeReference<Result<T>> typeReference, String jsonStr) throws IOException, InterruptedException {
        HttpRequest request = baseRequestBuilder(path).POST(HttpRequest.BodyPublishers.ofString(jsonStr))
                .build();
        return null;
//        return getResponseBody(request, typeReference);
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

}
