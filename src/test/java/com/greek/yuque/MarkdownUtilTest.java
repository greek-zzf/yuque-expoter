package com.greek.yuque;

import com.yuque.greek.MarkdownUtil;
import com.yuque.greek.YuqueClient;
import com.yuque.greek.YuqueClientFactory;
import com.yuque.greek.entity.Doc;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MarkdownUtilTest {

    private final Pattern PIC_REGEX = Pattern.compile("!\\[(.*)\\]\\((.*)\\)");
    private final String TEST_DOC_SLUG_01 = "kyclhx";
    private final String TEST_DOC_SLUG_02 = "vg1kak";
    private static final Integer TEST_REPO_ID = 34925886;
    private final YuqueClient yuqueClient = YuqueClientFactory.initClient();

    @Test
    void getAllImageTest() {
        Doc docDetail1 = yuqueClient.getDocDetail(TEST_REPO_ID, TEST_DOC_SLUG_01);
        List<MarkdownUtil.Image> doc01Images = MarkdownUtil.getAllImage(docDetail1.getMarkdownContent());
        assertEquals(1, doc01Images.size());

        Doc docDetail2 = yuqueClient.getDocDetail(TEST_REPO_ID, TEST_DOC_SLUG_02);
        List<MarkdownUtil.Image> doc02Images = MarkdownUtil.getAllImage(docDetail2.getMarkdownContent());
        assertEquals(1, doc02Images.size());
    }

    @Test
    void downloadMdTest() throws IOException {
        Doc doc = yuqueClient.getDocDetail(TEST_REPO_ID, TEST_DOC_SLUG_01);

        Path mdDownloadTestPath = Path.of(System.getProperty("user.dir"), File.separator, "md");
        Path picDownloadTestPath = Path.of(mdDownloadTestPath.toString(), File.separator + "picture");

        Files.createDirectory(mdDownloadTestPath);
        Files.createDirectory(picDownloadTestPath);

        MarkdownUtil.downloadMd(doc, mdDownloadTestPath, picDownloadTestPath);

        // 验证文件是否下载成功
        assertTrue(Files.exists(mdDownloadTestPath));
        assertTrue(Files.exists(picDownloadTestPath));
        assertTrue(Files.exists(Path.of(mdDownloadTestPath.toString(), File.separator, "yuque-test-doc-01.md")));

        Path docPicSavePath = Path.of(picDownloadTestPath.toString(), File.separator, doc.getTitle().replaceAll(" ", ""));
        List<Path> picPath;
        try (Stream<Path> paths = Files.walk(docPicSavePath)) {
            picPath = paths.filter(Files::isRegularFile)
                    .toList();
        }
        assertEquals(1, picPath.size());

        // 验证 md 文件中的路径是否替换成功
        String downloadedMarkdownContent = Files.readString(Path.of(mdDownloadTestPath.toString(), File.separator, doc.getTitle() + ".md"));
        Matcher matcher = PIC_REGEX.matcher(downloadedMarkdownContent);
        assertTrue(matcher.find());

        assertEquals(Boolean.getBoolean("fullPath") ? picPath.get(0).toString() : mdDownloadTestPath.relativize(picPath.get(0)).toString(),
                matcher.group(2)
        );

        // 清除生成的临时文件
//        clenTempFile(mdDownloadTestPath.toFile());
    }


    private void clenTempFile(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                clenTempFile(file);
            }
        }

        directoryToBeDeleted.delete();
    }
}
