package com.yuque.greek;

import com.yuque.greek.entity.resp.Doc;
import com.yuque.greek.entity.resp.Repo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class Yuque {

    private static int[] repoIds;

    private static Path mdDownloadPath = Path.of(System.getProperty("user.dir"));

    private static Path picDownloadPath = Path.of(mdDownloadPath.toString(), File.separator + "picture");

    private static final YuqueClient client = YuqueClientFactory.initClient(getToken());

    public static void main(String[] args) {

        // 加载配置信息
        loadConfig();

        // 打印用户的仓库信息
        printRepoList();

        // 选择要打印的仓库信息
        System.out.print("选择要导出的仓库序号：");
        Scanner scanner = new Scanner(System.in);
        int number = scanner.nextInt();
        System.out.println("所选择的仓库信息： " + YuqueClientFactory.getInstance().getRepoById(repoIds[number]));

        System.out.println("正在开始导出 md 文件...");

        // 获取选择仓库下所有文章信息
        List<Doc> docsInChooseRepo = client.getDocList(repoIds[number]);
        docsInChooseRepo.stream()
                .map(doc -> client.getDocDetail(repoIds[number], doc.getSlug()))
                .forEach(doc -> MarkdownUtil.downloadMd(doc, mdDownloadPath, picDownloadPath));

        System.out.println("导出完成...");
    }

    private static void loadConfig() {
        if (isInvalidPath(System.getProperty("mdPath"))) {
            mdDownloadPath = Path.of(System.getProperty("mdPath"));
        }

        if (isInvalidPath(System.getProperty("picPath"))) {
            picDownloadPath = Path.of(System.getProperty("picPath"));
        }

        try {
            if (!Files.exists(mdDownloadPath)) {
                Files.createDirectory(mdDownloadPath);
            }

            if (!Files.exists(picDownloadPath)) {
                Files.createDirectory(picDownloadPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("创建文件下载目录失败！");
        }

    }

    private static boolean isInvalidPath(String path) {
        return null != path && !path.isEmpty() && Files.isDirectory(Path.of(path));
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
