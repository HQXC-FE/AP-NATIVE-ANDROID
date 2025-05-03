package com.xtree.bet.manager;

import com.xtree.bet.bean.ui.SportEntity;
import java.util.*;

/**
 * 斯诺克局数数据管理
 */
public class SportEntityManager {
    private static final List<SportEntity> SNOOKER_FRAMES = new ArrayList<>();
    private static final Map<Integer, SportEntity> ID_LOOKUP = new HashMap<>();

    static {
        for (int i = 1; i <= 35; i++) {
            SportEntity entity = new SportEntity(16100 + i, "snooker Frame " + i, getChineseFrameName(i));
            SNOOKER_FRAMES.add(entity);
            ID_LOOKUP.put(entity.getId(), entity);
        }
    }

    public static List<SportEntity> getSnookerFrames() {
        return Collections.unmodifiableList(SNOOKER_FRAMES);
    }

    public static SportEntity getSportEntityById(int id) {
        return ID_LOOKUP.get(id); // O(1) 时间复杂度，效率更高
    }

    public static String getChineseFrameName(int frameNumber) {
        String[] chineseNumbers = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十"};
        if (frameNumber <= 10) {
            return "第" + chineseNumbers[frameNumber] + "局";
        } else if (frameNumber < 20) {
            return "第十" + chineseNumbers[frameNumber % 10] + "局";
        } else if (frameNumber % 10 == 0) {
            return "第" + chineseNumbers[frameNumber / 10] + "十局";
        } else {
            return "第" + chineseNumbers[frameNumber / 10] + "十" + chineseNumbers[frameNumber % 10] + "局";
        }
    }
}

