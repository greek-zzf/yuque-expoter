package com.yuque.greek;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AbstractClient {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String host;
    private final String userId;
    private final String accessToken;

    AbstractClient(String userId, String accessToken) {
        this.accessToken = accessToken;
        this.userId = userId;
        this.host = "https://${userId}.yuque.com/api/v2".replace("${userId}", userId);
    }


    protected String getRequest(String path,Class responseClass) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(host + path))
                .headers(
                        "Content-Type", "application/json",
                        "User-Agent", "chrome",
                        "X-Auth-Token", accessToken
                )
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if(200 == response.statusCode()){
            return response.body();
        }

        return null;
    }
}
