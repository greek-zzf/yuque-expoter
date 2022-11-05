package com.yuque.greek;

import com.yuque.greek.entity.resp.Repo;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

public class Yuque {


    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileReader("token.properties"));

        String accessToken = properties.getProperty("token");
        YuqueClient yuqueClient = new YuqueClient("greek-zzf", accessToken);

        System.out.println(yuqueClient.getDocList(1927824));

        System.out.println(yuqueClient.getDocDetail(1927824, 97759346));
    }
}
