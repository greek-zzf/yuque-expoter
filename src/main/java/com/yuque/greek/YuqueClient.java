package com.yuque.greek;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class YuqueClient extends AbstractClient {

    YuqueClient(String userId, String accessToken) {
        super(userId, accessToken);
    }

    void getAllRepos() {
        try {
            System.out.println(this.getRequest("/users/greek-zzf/repos"));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    void getRepoByNamespace() {

    }

//    protected void getRequest(String path) throws IOException, InterruptedException {
//        HttpClient httpClient = HttpClient.newHttpClient();
//
//        HttpRequest request = HttpRequest.newBuilder()
//                .GET()
//                .uri(URI.create(host + path))
//                .headers(
//                        "Content-Type", "application/json",
//                        "User-Agent", "chrome",
//                        "X-Auth-Token", accessToken
//                )
//                .build();
//
//        HttpResponse<String> send = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//        System.out.println(send);
//    }
}
