# yuque-expoter

## 简介

语雀开始收费，个人用户文章数量受到限制，因此想导出曾经写过的文章。

语雀官方未提供 Java 相关的 SDK，参照官方文档写了这么个程序。

代码出版使用的 Jdk 版本是 17，后续会补充 java8 的版本。

程序代码少，本着简单的原则，项目用的依赖也非常少，只使用了 `Jackson` 和 `Junit5`。麻雀虽小，五脏俱全，还是写了测试代码，方便之后进行重构。

## 使用说明

### 方法一

使用 `maven clean package -DskipTests` 打包该项目, `java -jar` 命令运行即可。

### 方法二

在发布页下载压缩好的 jar 包，运行即可。

### 启动参数说明

**必须**

启动项目的时候必需指定 token 消息

```shell
java -jar -Dtoken=你的token yuque-exoprt-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```

**非必须**

启动参数同样支持设置 markdown 文件导出路径 `mdPath`，和图片存放路径 `picPath` 。默认的 markdown 导出路径为程序运行目录，图片路径为
程序运行目录的 `picture` 文件夹。

```shell
java -jar -DmdPath=xxx -DpicPath=xxx -Dtoken=xxx yuque-exoprt-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```

参数支持指定 markdown 中替换的图片路径为绝对路径或相对路径。使用参数 `fullPath`，值为 `true` 或 `false`，true
表示绝对路径。默认使用相对路径。

```shell
java -jar -DfullPath=true -Dtoken=xxx yuque-exoprt-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```

## 已实现

- [x] 语雀仓库的信息获取
- [x] 语雀文章的信息获取
- [x] 文章导出为 md 初步实现
- [x] 导出的 md 文件路径指定，以及图片位置指定
- [x] 自动下载 md 包含的图片
- [x] 替换 md 中的网络图片路径为本地图片
- [x] 打包运行
- [x] 编写测试代码
- [x] 更人性化交互
- [x] 支持选择多个仓库
- [x] 代码初步重构优化
- [x] 语雀锚点格式化

## 待实现

- [ ] 再次重构优化
- [ ] java8 实现功能
