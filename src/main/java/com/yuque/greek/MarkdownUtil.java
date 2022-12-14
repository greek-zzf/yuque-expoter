package com.yuque.greek;

import com.yuque.greek.entity.Doc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarkdownUtil {

    private static final Pattern IMAGE_REGEX = Pattern.compile("!\\[(.*?)\\]\\((.*?)\\)");
    private static final String ANCHOR_REGEX = "<a name=\".*?\"></a>";

    private static final boolean IS_FULL_PATH = Boolean.getBoolean("fullPath");

    public static List<Image> getAllImage(String markdown) {
        Matcher matcher = IMAGE_REGEX.matcher(markdown);
        List<Image> result = new ArrayList<>();
        while (matcher.find()) {
            result.add(new Image(matcher.group(1), matcher.group(2)));
        }
        return result;
    }

    public static void downloadMd(Doc doc, Path mdPath, Path picPath) {
        String markdown = doc.getMarkdownContent().replaceAll(ANCHOR_REGEX, "");
        List<Image> allImage = getAllImage(markdown);

        for (Image image : allImage) {
            Path picSavePath = Paths.get(picPath.toString(), File.separator, doc.getTitle().replaceAll(" ", ""), File.separator, UUID.randomUUID() + ".png");

            if (!Files.exists(picSavePath.getParent())) {
                try {
                    Files.createDirectory(picSavePath.getParent());
                } catch (IOException e) {
                    throw new RuntimeException("创建图片保存路径 " + picSavePath.getParent() + " 失败！");
                }
            }
            downloadAndSaveImage(image.getUrl(), picSavePath);

            markdown = markdown.replace(image.getUrl(), IS_FULL_PATH ? picSavePath.toString() : mdPath.relativize(picSavePath).toString());
        }

        saveMarkdown(markdown, Paths.get(mdPath + File.separator + doc.getTitle() + ".md"));
    }

    private static void saveMarkdown(String markdownContent, Path mdSavePath) {
        try {
            Files.write(mdSavePath, markdownContent.getBytes(StandardCharsets.UTF_8));
            System.out.println("导出: " + mdSavePath.getFileName() + "成功!");
        } catch (IOException e) {
            throw new RuntimeException("下载 md 文件失败！");
        }
    }

    private static void downloadAndSaveImage(String url, Path picSavePath) {
        try {
            BufferedImage read = ImageIO.read(new URL(url));
            ImageIO.write(read, "png", picSavePath.toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static class Image {

        private String name;
        private String url;

        public String getName() {
            return name;
        }

        public String getUrl() {
            return url;
        }

        public Image(String name, String url) {
            this.name = name;
            this.url = url;
        }
    }


}
