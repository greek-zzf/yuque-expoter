package com.yuque.greek;

import com.yuque.greek.entity.Doc;
import com.yuque.greek.entity.Repo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Yuque {

    private static Map<Integer, String> exportRecords = new HashMap<>();
    private static int[] repoIds;

    private static Path mdDownloadPath = Path.of(System.getProperty("user.dir"));

    private static Path picDownloadPath = Path.of(mdDownloadPath.toString(), File.separator + "picture");

    private static final YuqueClient client = YuqueClient.getInstance();

    public static void main(String[] args) {
        // 加载配置信息
        loadConfig();

        System.out.print(("Hello " + client.currentUser.name() + ", 根据序号选择要导出的仓库"));
        while (true) {
            // 打印用户的仓库信息
            printRepoList();

            // 选择并打印仓库信息
            int choosedRepoId = chooseThenPrintChoosedRepo();

            // 导出所选仓库的 md 文件
            int exportCount = exportDocsInChoosedRepo(choosedRepoId);
            System.out.println("成功导出 " + exportCount + " 篇文章！");

            // 记录已导出的仓库
            recordExportRepo(choosedRepoId);
        }
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

    private static boolean isInvalidPath(String strPath) {
        if (null != strPath && !strPath.isEmpty()) {
            Path path = Path.of(strPath);
            if (Files.exists(path)) {
                return true;
            }else {
                try {
                    Files.createDirectory(path);
                    return true;
                } catch (IOException e) {
                    throw new RuntimeException("请检查文件路径配置！");
                }
            }
        }
        return false;
    }

    private static void printRepoList() {
        List<Repo> allRepos = client.getAllRepos();
        StringBuilder builder = new StringBuilder("\n——————————————————————");

        int count = 1;
        repoIds = new int[allRepos.size() + 1];
        for (Repo repo : allRepos) {
            builder.append("\n").append(count).append(". ").append(repo.name()).append(exportRecords.getOrDefault(repo.id(), ""));
            repoIds[count++] = repo.id();
        }

        System.out.println(builder.append("\n——————————————————————\n"));
    }

    private static int chooseThenPrintChoosedRepo() {
        System.out.print("选择要导出的仓库序号[输入 0 退出程序]：");
        Scanner scanner = new Scanner(System.in);
        int number = scanner.nextInt();

        if (number == 0) {
            System.exit(0);
        }

        Repo chooseRepo = client.getRepoById(repoIds[number]);
        System.out.println("所选择的仓库名称: " + chooseRepo.name() + " 文章数量: " + chooseRepo.itemsCount() + " 仓库属性: " + chooseRepo.state());

        return repoIds[number];
    }

    private static int exportDocsInChoosedRepo(int choosedRepoId) {
        List<Doc> docsInChooseRepo = client.getDocList(choosedRepoId);
        docsInChooseRepo.stream()
                .map(doc -> client.getDocDetail(choosedRepoId, doc.slug()))
                .forEach(doc -> MarkdownUtil.downloadMd(doc, mdDownloadPath, picDownloadPath));

        return docsInChooseRepo.size();
    }

    private static void recordExportRepo(int choosedRepoId) {
        exportRecords.put(choosedRepoId, " (已导出)");
    }

}
