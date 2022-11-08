package com.greek.yuque;

import com.yuque.greek.YuqueClient;
import com.yuque.greek.YuqueClientFactory;
import com.yuque.greek.entity.Doc;
import com.yuque.greek.entity.Repo;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class YuqueClientTest {

    private static final Integer TEST_REPO_ID = 34925886;

    @Test
    void throwExceptionWhenUseErrorToken() {
        System.setProperty("token", "");
        RuntimeException noToken = assertThrows(RuntimeException.class, YuqueClientFactory::initClient);
        assertEquals("请指定token信息!", noToken.getMessage());

        System.setProperty("token", "errorToken");
        RuntimeException tokenError = assertThrows(RuntimeException.class, YuqueClientFactory::initClient);
        assertEquals("获取用户信息失败，请检查 token 信息!", tokenError.getMessage());
    }

    @Test
    void getAllRepoTest() {
        YuqueClient yuqueClient = YuqueClientFactory.initClient();

        List<Repo> allRepos = yuqueClient.getAllRepos();
        assertEquals(6, allRepos.size());

        Optional<Repo> repo = allRepos.stream()
                .filter(e -> e.getName().equals("yuque-export-test-repo"))
                .findAny();

        assertTrue(repo.isPresent());
        assertEquals(TEST_REPO_ID, repo.get().getId());
    }

    @Test
    void getRepoDetail() {
        YuqueClient yuqueClient = YuqueClientFactory.initClient();

        Repo repoDetail = yuqueClient.getRepoById(TEST_REPO_ID);
        assertTrue(Objects.nonNull(repoDetail));
        assertEquals(2, (int) repoDetail.getItemsCount());
        assertEquals("私有", repoDetail.getState());
    }

    @Test
    void getDocList() {
        YuqueClient yuqueClient = YuqueClientFactory.initClient();

        List<Doc> docList = yuqueClient.getDocList(TEST_REPO_ID);

        assertEquals(2, docList.size());
        assertTrue(docList.stream().anyMatch(doc -> doc.getTitle().equals("yuque-test-doc-01")));
        assertTrue(docList.stream().anyMatch(doc -> doc.getTitle().equals("yuque-doc-test-02")));
    }

    @Test
    void getDocDeatil() {
        YuqueClient yuqueClient = YuqueClientFactory.initClient();

        List<Doc> docList = yuqueClient.getDocList(TEST_REPO_ID);
        List<Doc> docDetails = docList.stream()
                .map(doc -> yuqueClient.getDocDetail(TEST_REPO_ID, doc.getSlug()))
                .toList();

        docDetails.forEach(this::docVerify);
    }

    private void docVerify(Doc doc) {
        assertTrue(Objects.nonNull(doc.getMarkdownContent()) && !doc.getMarkdownContent().isEmpty());
        assertEquals(doc.getRepoId(), YuqueClientTest.TEST_REPO_ID);
    }


}