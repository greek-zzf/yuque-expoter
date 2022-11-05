package com.yuque.greek;

import com.yuque.greek.entity.resp.Doc;
import com.yuque.greek.entity.resp.Repo;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

public class Yuque {


    public static void main(String[] args) throws IOException {
//        Properties properties = new Properties();
//        properties.load(new FileReader("token.properties"));
//
//        String accessToken = properties.getProperty("token");
//        YuqueClient yuqueClient = new YuqueClient("greek-zzf", accessToken);
//
//        Doc docDetail = yuqueClient.getDocDetail(1927824, 97759346);
//        System.out.println(docDetail);
//
//        Files.writeString(Path.of(docDetail.getTitle() + ".md"), docDetail.getMarkdownContent());


        String markdown = Files.readString(Path.of("Git Bisect 快速上手.md"));
        List<MarkdownUtil.Image> allImage = MarkdownUtil.getAllImage(markdown);
        allImage.stream().forEach(e -> MarkdownUtil.downloadImage(e.getName(), e.getUrl()));
    }
}
