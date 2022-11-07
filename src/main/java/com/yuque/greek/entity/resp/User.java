package com.yuque.greek.entity.resp;

import com.fasterxml.jackson.annotation.JsonAlias;

public class User {
    private Integer id;
    @JsonAlias("account_id")
    private Integer accountId;
    private String login;
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
