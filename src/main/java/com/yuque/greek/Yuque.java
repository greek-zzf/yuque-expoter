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
import java.util.Scanner;
import java.util.stream.Collectors;

public class Yuque {

    private static int[] repoIds;

    public static void main(String[] args){

        YuqueClientFactory.initClient("greek-zzf", getToken());

        printRepoList();

        System.out.println("选择要导出的仓库序号：");
        Scanner scanner = new Scanner(System.in);
        int number = scanner.nextInt();

        System.out.println("所选择的仓库信息： " + YuqueClientFactory.getInstance().getRepoById(repoIds[number]));


        System.out.println("正在开始导出...");


    }

    private static void printRepoList() {
        List<Repo> allRepos = YuqueClientFactory.getInstance().getAllRepos();
        StringBuilder builder = new StringBuilder("——————————————————————")
                .append("\n根据序号选择要导出的仓库");

        int count = 1;
        repoIds = new int[allRepos.size() + 1];
        for (Repo repo : allRepos) {
            builder.append("\n[" + count + "]" + repo.getName());
            repoIds[count++] = repo.getId();
        }

        System.out.println(builder.append("\n——————————————————————\n"));
    }

    private static String getToken() {
        String token = System.getProperty("token");
        if (null == token || token.isEmpty()) {
            Properties properties = new Properties();
            try {
                properties.load(new FileReader("token.properties"));
                return properties.getProperty("token");
            } catch (IOException e) {
                throw new RuntimeException("请指定token信息！");
            }
        }

        return token;
    }
}
