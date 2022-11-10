package com.yuque.greek.entity;

import com.fasterxml.jackson.annotation.JsonAlias;

public record Doc(int id, String slug, String title, String format,
                  @JsonAlias("book_id") int repoId,
                  @JsonAlias("body") String markdownContent) {

}