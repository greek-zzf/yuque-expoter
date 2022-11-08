package com.yuque.greek;

import com.yuque.greek.entity.Doc;
import com.yuque.greek.entity.Repo;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
        int choosedRepoId = chooseThenPrintChoosedRepo();

        int exportCount = exportDocsInChoosedRepo(choosedRepoId);
        System.out.println("成功导出 " + exportCount + " 篇文章！");
    }

    private static int exportDocsInChoosedRepo(int choosedRepoId) {
        List<Doc> docsInChooseRepo = client.getDocList(choosedRepoId);
        docsInChooseRepo.stream()
                .map(doc -> client.getDocDetail(choosedRepoId, doc.getSlug()))
                .forEach(doc -> MarkdownUtil.downloadMd(doc, mdDownloadPath, picDownloadPath));

        return docsInChooseRepo.size();
    }

    private static int chooseThenPrintChoosedRepo() {
        System.out.print("选择要导出的仓库序号：");
        Scanner scanner = new Scanner(System.in);
        int number = scanner.nextInt();

        Repo chooseRepo = client.getRepoById(repoIds[number]);
        System.out.println("所选择的仓库名称: " + chooseRepo.getName() + " 文章数量: " + chooseRepo.getItemsCount() + " 仓库属性: " + chooseRepo.getState());

        return repoIds[number];
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
        StringBuilder builder = new StringBuilder("Hello " + client.currentUser.getName() + ", 根据序号选择要导出的仓库").append("\n——————————————————————");

        int count = 1;
        repoIds = new int[allRepos.size() + 1];
        for (Repo repo : allRepos) {
            builder.append("\n").append(count).append(". ").append(repo.getName());
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
