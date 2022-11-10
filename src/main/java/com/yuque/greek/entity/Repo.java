package com.yuque.greek.entity;

import com.fasterxml.jackson.annotation.JsonAlias;


public record Repo(int id, String slug, String type, String name,
                   @JsonAlias("public") int state,
                   @JsonAlias("user_id") String userId,
                   @JsonAlias("items_count") String itemsCount) {
}

