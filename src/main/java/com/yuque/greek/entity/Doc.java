package com.yuque.greek.entity;

import com.fasterxml.jackson.annotation.JsonAlias;

public class Doc {
    private Integer id;
    private String slug;
    private String title;
    @JsonAlias("book_id")
    private Integer repoId;
    private String format;

    @JsonAlias("body")
    private String markdownContent;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getRepoId() {
        return repoId;
    }

    public void setRepoId(Integer repoId) {
        this.repoId = repoId;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getMarkdownContent() {
        return markdownContent;
    }

    public void setMarkdownContent(String markdownContent) {
        this.markdownContent = markdownContent;
    }

    @Override
    public String toString() {
        return "Doc{" +
                "id=" + id +
                ", slug='" + slug + '\'' +
                ", title='" + title + '\'' +
                ", repoId=" + repoId +
                ", format='" + format + '\'' +
                ", markdownContent='" + markdownContent + '\'' +
                '}';
    }
}
