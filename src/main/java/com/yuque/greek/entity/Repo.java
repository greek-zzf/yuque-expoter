package com.yuque.greek.entity;

import com.fasterxml.jackson.annotation.JsonAlias;

public class Repo {

    private static final String[] STATE = new String[]{"私有", "公开"};
    private Integer id;
    private String slug;
    private String type;
    private String name;

    @JsonAlias("user_id")
    private String userId;
    @JsonAlias("items_count")
    private Integer itemsCount;

    @JsonAlias("public")
    private Integer state;

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

    public String  getState() {
        return STATE[state];
    }

    public void setState(Integer state) {
        this.state = state;
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
                ", state=" + getState() +
                '}';
    }
}
