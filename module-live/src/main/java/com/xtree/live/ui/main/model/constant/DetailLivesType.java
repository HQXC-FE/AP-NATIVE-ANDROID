package com.xtree.live.ui.main.model.constant;

import java.util.HashMap;
import java.util.Map;

public enum DetailLivesType {

//    -1：获取全部，0：足球，1：篮球，2：其他，3：电竞，4：区块链。默认-1

    SQUARE("-1", "广场"),
    BET("0", "投注"),
    ANCHOR_PRIVATE("1", "主播私聊"),
    ANCHOR_ASSISTANT("2", "主播助理");

    private static final Map<String, DetailLivesType> VALUE_MAP = new HashMap<>();
    private static final Map<String, DetailLivesType> LABEL_MAP = new HashMap<>();

    // 静态块：在类加载时缓存所有枚举常量
    static {
        for (DetailLivesType accountType : DetailLivesType.values()) {
            VALUE_MAP.put(accountType.value, accountType);
            LABEL_MAP.put(accountType.label, accountType);
        }
    }

    private final String value;
    private final String label;

    DetailLivesType(String value, String label) {
        this.value = value;
        this.label = label;
    }

    // 通过 value 查找枚举
    public static DetailLivesType fromValue(String value) {
        return VALUE_MAP.get(value);
    }

    // 通过 label 查找枚举
    public static DetailLivesType fromLabel(String label) {
        return LABEL_MAP.get(label);
    }

    public String getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return this.name() + "(" + value + ")";
    }
}
