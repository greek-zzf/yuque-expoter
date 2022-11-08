package com.yuque.greek;

import com.yuque.greek.entity.Doc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarkdownUtil {

    private static final Pattern IMAGE_REGEX = Pattern.compile("!\\[(.*)\\]\\((.*)\\)");

    public static List<Image> getAllImage(String markdown) {
        Matcher matcher = IMAGE_REGEX.matcher(markdown);
        List<Image> result = new ArrayList<>();
        while (matcher.find()) {
            result.add(new Image(matcher.group(1), matcher.group(2)));
        }
        return result;
    }

    public static void downloadMd(Doc doc, Path mdPath, Path picPath) {
        String markdown = doc.getMarkdownContent();
        List<Image> allImage = getAllImage(markdown);

        Path picSavePath;
        for (Image image : allImage) {
            picSavePath = Path.of(picPath.toString(), File.separator, doc.getTitle().replaceAll(" ", ""), File.separator, image.getName());

            if (!Files.exists(picSavePath.getParent())) {
                try {
                    Files.createDirectory(picSavePath.getParent());
                } catch (IOException e) {
                    throw new RuntimeException("创建图片保存路径 " + picSavePath.getParent() + " 失败！");
                }
            }
            downloadImage(image.getUrl(), picSavePath);

            markdown = markdown.replace(image.getUrl(), picSavePath.toString());
        }

        try {
            Files.writeString(Path.of(mdPath + File.separator + doc.getTitle() + ".md"), markdown);
            System.out.println("导出: " + doc.getTitle() + "成功!");
        } catch (IOException e) {
            throw new RuntimeException("下载 md 文件失败！");
        }
    }

    public static void downloadImage(String url, Path picSavePath) {
        try {
            BufferedImage read = ImageIO.read(new URL(url));
            ImageIO.write(read, "png", picSavePath.toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    static class Image {

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
