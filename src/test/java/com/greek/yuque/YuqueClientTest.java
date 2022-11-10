package com.greek.yuque;

import com.yuque.greek.YuqueClient;
import com.yuque.greek.entity.Doc;
import com.yuque.greek.entity.Repo;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class YuqueClientTest {

    private static final Integer TEST_REPO_ID = 34925886;


    @Order(1)
    @Test
    void throwExceptionWhenUseErrorToken() {
        String configToken = System.getProperty("token");

        System.setProperty("token", "");
        RuntimeException noToken = assertThrows(RuntimeException.class, YuqueClient::getInstance);
        assertEquals("请指定token信息!", noToken.getMessage());

        System.setProperty("token", "errorToken");
        RuntimeException tokenError = assertThrows(RuntimeException.class, YuqueClient::getInstance);
        assertEquals("获取用户信息失败，请检查 token 信息!", tokenError.getMessage());

        System.setProperty("token", configToken);
    }

    @Test
    void getAllRepoTest() {
        YuqueClient yuqueClient = YuqueClient.getInstance();

        List<Repo> allRepos = yuqueClient.getAllRepos();
        assertEquals(6, allRepos.size());

        Optional<Repo> repo = allRepos.stream()
                .filter(e -> e.name().equals("yuque-export-test-repo"))
                .findAny();

        assertTrue(repo.isPresent());
        assertEquals(TEST_REPO_ID, repo.get().id());
    }

    @Test
    void getRepoDetail() {
        YuqueClient yuqueClient = YuqueClient.getInstance();

        Repo repoDetail = yuqueClient.getRepoById(TEST_REPO_ID);
        assertTrue(Objects.nonNull(repoDetail));
        assertEquals(2, Integer.parseInt(repoDetail.itemsCount()));
        assertEquals(0, repoDetail.state());
    }

    @Test
    void getDocList() {
        YuqueClient yuqueClient = YuqueClient.getInstance();

        List<Doc> docList = yuqueClient.getDocList(TEST_REPO_ID);

        assertEquals(2, docList.size());
        assertTrue(docList.stream().anyMatch(doc -> doc.title().equals("yuque-test-doc-01")));
        assertTrue(docList.stream().anyMatch(doc -> doc.title().equals("yuque-doc-test-02")));
    }

    @Test
    void getDocDeatil() {
        YuqueClient yuqueClient = YuqueClient.getInstance();

        List<Doc> docList = yuqueClient.getDocList(TEST_REPO_ID);
        List<Doc> docDetails = docList.stream()
                .map(doc -> yuqueClient.getDocDetail(TEST_REPO_ID, doc.slug()))
                .toList();

        docDetails.forEach(this::docVerify);
    }

    private void docVerify(Doc doc) {
        assertTrue(Objects.nonNull(doc.markdownContent()) && !doc.markdownContent().isEmpty());
        assertEquals(doc.repoId(), YuqueClientTest.TEST_REPO_ID);
    }


}
