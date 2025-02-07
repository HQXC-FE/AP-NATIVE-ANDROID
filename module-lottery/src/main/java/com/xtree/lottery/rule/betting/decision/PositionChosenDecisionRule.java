package com.xtree.lottery.rule.betting.decision;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.List;
import java.util.Map;

@Rule(name = "PositionChosenRule", description = "任选位数算法")
public class PositionChosenDecisionRule {

    @Priority
    public int getPriority() {
        return -15000;
    }

    @Condition
    public boolean when(Facts facts) {
        // 从 facts 获取规则套件
        List<String> ruleSuite = facts.get("ruleSuite");
        return ruleSuite != null && ruleSuite.contains("position-chosen");
    }

    @Action
    public void then(Facts facts) {
        try {
            // 从 facts 中获取相关数据
            List<Boolean> poschoose = ((Map<String, List<Boolean>>) facts.get("betposchoose")).get("poschoose");

            Integer posnum = (Integer) ((Map<String, Object>) facts.get("attached")).get("posnum");
            Integer number = (Integer) ((Map<String, Object>) facts.get("attached")).get("number");
            Integer num = facts.get("num");

            if (poschoose == null || num == null) {
                throw new IllegalArgumentException("Missing required facts: poschoose or num");
            }

            // 筛选出被选择的位置
            long selectedCount = poschoose.stream().filter(item -> item).count();

            // 计算最小选择数
            int minNumber = (posnum != null) ? posnum : (number != null ? number : 0);

            if (minNumber > selectedCount) {
                facts.put("num", 0); // 如果选中的位数小于最小选择量，将 num 置为 0
            } else {
                // 计算排列组合
                int combination = calculateCombination((int) selectedCount, minNumber);
                facts.put("num", num * combination); // 更新 num
            }
        } catch (Exception e) {
            CfLog.e(e.getMessage());
        }
    }

    // Helper method to calculate combinations C(n, r) = n! / (r! * (n - r)!)
    private int calculateCombination(int n, int r) {
        if (r > n) return 0;
        return factorial(n) / (factorial(r) * factorial(n - r));
    }

    private int factorial(int num) {
        int result = 1;
        for (int i = 1; i <= num; i++) {
            result *= i;
        }
        return result;
    }
}