package com.xtree.bet.bean.response;

import java.util.List;

public class SportsCacheSwitchInfo {

    private List<Integer> fb;        // fb 数组，元素是整数
    private List<Integer> fbxc;      // fbxc 数组，元素是整数
    private List<Integer> obg;       // obg 数组，元素是整数
    private List<Integer> obgzy;     // obgzy 数组，元素是整数
    private List<String> users;      // users 数组，元素是字符串

    // Getters 和 Setters 方法
    public List<Integer> getFb() {
        return fb;
    }

    public void setFb(List<Integer> fb) {
        this.fb = fb;
    }

    public List<Integer> getFbxc() {
        return fbxc;
    }

    public void setFbxc(List<Integer> fbxc) {
        this.fbxc = fbxc;
    }

    public List<Integer> getObg() {
        return obg;
    }

    public void setObg(List<Integer> obg) {
        this.obg = obg;
    }

    public List<Integer> getObgzy() {
        return obgzy;
    }

    public void setObgzy(List<Integer> obgzy) {
        this.obgzy = obgzy;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}

