package com.yuque.greek;

public class Yuque {


    public static void main(String[] args) {
        YuqueClient yuqueClient = new YuqueClient("greek-zzf","");
        yuqueClient.getAllRepos();
    }
}
