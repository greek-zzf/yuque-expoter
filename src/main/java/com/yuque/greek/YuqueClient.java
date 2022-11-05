package com.yuque.greek;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.yuque.greek.entity.resp.Repo;
import com.yuque.greek.entity.resp.Result;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class YuqueClient extends AbstractClient {

    private final ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    YuqueClient(String userId, String accessToken) {
        super(userId, accessToken);
    }

    public List<Repo> getAllRepos() {
        return asList(getRequest("/users/greek-zzf/repos"), Repo.class).getData();
    }

    public Repo getRepoById(Integer id) {
        return null;
//        return getRequest("/repos/" + id);
    }


    private <T> Result<List<T>> asList(String response, Class<T> klass) {
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

    private <T> Result<T> asObject(String response) {
        Result<T> result;
        try {
            result = objectMapper.readValue(response, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
