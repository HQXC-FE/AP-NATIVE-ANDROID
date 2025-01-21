package com.xtree.lottery.rule.betting.after;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.List;
import java.util.Map;

@Rule(name = "任意单注判断", description = "判断是否符合任意单注规则")
public class AnySoloRule {

    @Priority
    public int getPriority() {
        return -8950;
    }

    @Condition
    public boolean when(Facts facts) {
        List<String> ruleSuite = facts.get("ruleSuite"); // 规则套件
        int num = facts.get("num"); // 当前投注数

        // 条件：ruleSuite 包含 "any-solo" 且 num > 0
        return ruleSuite != null && ruleSuite.contains("any-solo") && num > 0;
    }

    @Action
    public void then(Facts facts) {
        try {
            // 选中位置的数量
            List<List<Integer>> formatCodes = facts.get("formatCodes"); // 号码格式
            long posChoose = formatCodes.stream().filter(item -> !item.isEmpty()).count();

            // 任意N配置
            Map<String, String> attached = facts.get("attached");
            int attachedNumber = Integer.getInteger(attached.get("number"));
            int minNumber = facts.get("minNumber"); // 最小号码

            // 判断 solo 规则
            boolean solo = false;
            switch (attachedNumber) {
                case 2: // 任意2
                    if (posChoose == 2 && minNumber <= 1) {
                        solo = true;
                    } else if (posChoose == 3 && minNumber <= 3) {
                        solo = true;
                    } else if (posChoose == 4 && minNumber <= 6) {
                        solo = true;
                    } else if (posChoose == 5 && minNumber <= 10) {
                        solo = true;
                    }
                    break;
                case 3: // 任意3
                    if (posChoose == 3 && minNumber <= 10) {
                        solo = true;
                    } else if (posChoose == 4 && minNumber <= 40) {
                        solo = true;
                    } else if (posChoose == 5 && minNumber <= 100) {
                        solo = true;
                    }
                    break;
                case 4: // 任意4
                    if (posChoose == 4 && minNumber <= 100) {
                        solo = true;
                    } else if (posChoose == 5 && minNumber <= 500) {
                        solo = true;
                    }
                    break;
            }

            // 更新结果
            facts.put("solo", solo);
        } catch (Exception e) {
            CfLog.e(e.getMessage());
        }
    }
}

