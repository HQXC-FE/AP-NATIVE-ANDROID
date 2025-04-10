package com.xtree.lottery.rule.betting.decision;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Rule(name = "Dantuo Any Rule", description = "任选胆拖计算注数")
public class DantuoAnyRule {

    @Priority
    public int getPriority() {
        return -17600;
    }

    @Condition
    public boolean when(Facts facts) {
        List<String> ruleSuite = facts.get("ruleSuite");
        return ruleSuite != null && ruleSuite.contains("dantuo-any");
    }

    @Action
    public void then(Facts facts) {
        try {
            List<List<String>> formatCodes = facts.get("formatCodes");
            Map<String, Object> currentMethod = facts.get("currentMethod");

            // 分别取得胆码和拖码
            List<String> dan = formatCodes.get(0);
            List<String> tuo = formatCodes.get(1);

            // 互相排除重复的号码
            List<String> danFiltered = dan.stream().filter(d -> !tuo.contains(d)).collect(Collectors.toList());
            List<String> tuoFiltered = tuo.stream().filter(t -> !dan.contains(t)).collect(Collectors.toList());

            // 更新去重后的格式码
            formatCodes.set(0, danFiltered);
            formatCodes.set(1, tuoFiltered);

            if (danFiltered.isEmpty() || tuoFiltered.isEmpty()) {
                facts.put("num", 0);
                return;
            }

            String playType = (String) currentMethod.get("name");
            int requiredCount = getRequiredCount(playType);
            if (requiredCount == -1) {
                facts.put("num", 0);
                return;
            }

            int k = requiredCount - danFiltered.size();
            int count = combination(tuoFiltered.size(), k);
            facts.put("num", count);
        } catch (Exception e) {
            CfLog.e("DantuoAnyRule Error: " + e.getMessage());
        }
    }

    private int combination(int n, int k) {
        if (k < 0 || k > n) return 0;
        long result = 1;
        for (int i = 1; i <= k; i++) {
            result = result * (n - i + 1) / i;
        }
        return (int) result;
    }

    private int getRequiredCount(String playType) {
        switch (playType) {
            case "二中二":
                return 2;
            case "三中三":
                return 3;
            case "四中四":
                return 4;
            case "五中五":
                return 5;
            case "六中五":
                return 6;
            case "七中五":
                return 7;
            case "八中五":
                return 8;
            default:
                return -1;
        }
    }
}