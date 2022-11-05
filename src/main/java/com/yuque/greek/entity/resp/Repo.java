package com.yuque.greek.entity.resp;

import com.fasterxml.jackson.annotation.JsonAlias;

public class Repo {
    private Integer id;
    private String slug;
    private String type;
    private String name;

    @JsonAlias("user_id")
    private String userId;
    @JsonAlias("items_count")
    private Integer itemsCount;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getItemsCount() {
        return itemsCount;
    }

    public void setItemsCount(Integer itemsCount) {
        this.itemsCount = itemsCount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Repo{" +
                "id=" + id +
                ", slug='" + slug + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", userId='" + userId + '\'' +
                ", itemsCount=" + itemsCount +
                '}';
    }
}
