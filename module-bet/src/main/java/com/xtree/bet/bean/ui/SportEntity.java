package com.xtree.bet.bean.ui;

/**
 * 斯诺克比分实体类
 */
public class SportEntity {
    private int id;
    private String code;
    private String name;

    public SportEntity(int id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "SportEntity{id=" + id + ", code='" + code + "', name='" + name + "'}";
    }
}