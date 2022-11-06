package com.yuque.greek;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
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

    public static void downloadImage(String name, String url) {
        try {
            BufferedImage read = ImageIO.read(new URL(url));
            ImageIO.write(read, "png", Path.of(name).toFile());
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

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Image(String name, String url) {
            this.name = name;
            this.url = url;
        }
    }


}
