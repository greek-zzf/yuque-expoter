package com.yuque.greek;

import com.yuque.greek.entity.Doc;
import com.yuque.greek.entity.Repo;

import java.util.List;

public class YuqueClient extends AbstractClient {

    private static YuqueClient client;

    public static YuqueClient getInstance() {
        if (null == client) {
            client = new YuqueClient(getToken());
        }
        return client;
    }

    public List<Repo> getAllRepos() {
        final String path = String.format("/users/%s/repos", currentUser.id());
        return asList(getRequest(path), Repo.class).getData();
    }

    public Repo getRepoById(Integer id) {
        final String path = String.format("/repos/%s", id);
        return asObject(getRequest(path), Repo.class).getData();
    }

    public List<Doc> getDocList(Integer repoId) {
        final String path = String.format("/repos/%s/docs", repoId);
        return asList(getRequest(path), Doc.class).getData();
    }

    public Doc getDocDetail(Integer repoId, String slug) {
        final String path = String.format("/repos/%s/docs/%s", repoId, slug);
        return asObject(getRequest(path), Doc.class).getData();
    }


    private YuqueClient(String accessToken) {
        super(accessToken);
    }

    private static String getToken() {
        String token = System.getProperty("token");
        if (null == token || token.isEmpty()) {
            throw new RuntimeException("请指定token信息!");
        }

        return token;
    }

}
