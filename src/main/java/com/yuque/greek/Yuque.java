package com.yuque.greek;

import com.yuque.greek.entity.resp.Repo;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.Scanner;

public class Yuque {

    private static int[] repoIds;

    private static final YuqueClient client = YuqueClientFactory.initClient(getToken());

    public static void main(String[] args) {

        printRepoList();

        System.out.println("选择要导出的仓库序号：");
        Scanner scanner = new Scanner(System.in);
        int number = scanner.nextInt();

        System.out.println("所选择的仓库信息： " + YuqueClientFactory.getInstance().getRepoById(repoIds[number]));


        String mdDownloadPath = getMdDownloadPath();

        // todo: 获取图片下载路径

        /*
        * 1. 获取图片下载路径
        * 2. 替换 md 中的下载地址
        * 3. 开始下载 md 文件
        * 4. 结束提示语（根据条件判断是否继续）
        * */
        System.out.println("正在开始导出...");
    }

    private static String getMdDownloadPath() {
        String configMdDownloadPath = System.getProperty("mdPath");

        if (Objects.isNull(configMdDownloadPath) || Files.isDirectory(Path.of(configMdDownloadPath))) {
            System.out.println("未配置 md 文件下载路径或路径无效！请输入 md 下载路径: ");

            Scanner scanner = new Scanner(System.in);
            String mdDownloadPath = scanner.next();
            while (!Files.isDirectory(Path.of(mdDownloadPath))) {
                scanner = new Scanner("请重新输入 md 下载路径: ");
                mdDownloadPath = scanner.next();
            }

            return mdDownloadPath;
        }

        return configMdDownloadPath;
    }

    private static void printRepoList() {
        List<Repo> allRepos = client.getAllRepos();
        StringBuilder builder = new StringBuilder("Hello " + client.currentUser.getName() + ", 根据序号选择要导出的仓库")
                .append("\n——————————————————————");

        int count = 1;
        repoIds = new int[allRepos.size() + 1];
        for (Repo repo : allRepos) {
            builder.append("\n" + count + ". " + repo.getName());
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
