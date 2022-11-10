package com.yuque.greek.entity;

import com.fasterxml.jackson.annotation.JsonAlias;


public record User(int id, int accountId, String login, String name) {
}
