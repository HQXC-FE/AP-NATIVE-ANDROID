package com.xtree.lottery.rule.decision;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;

import java.util.List;
import java.util.Map;

@Rule(name = "CombinationChosenRule", description = "通用组合算法，从 M 中选 N 组合")
public class CombinationChosenRule {

    @Priority
    public int getPriority() {
        return 19950;
    }

    @Condition
    public boolean when(Map<String, Object> facts) {
        // 检查是否包含 "combination-chosen"
        List<String> ruleSuite = (List<String>) facts.get("ruleSuite");
        return ruleSuite != null && ruleSuite.contains("combination-chosen");
    }

    @Action
    public void then(Map<String, Object> facts) {
        // 获取相关数据
        List<List<String>> formatCodes = (List<List<String>>) facts.get("formatCodes");
        Integer number = (Integer) facts.get("attached.number");
        Integer num = (Integer) facts.get("num");

        // 检查输入的有效性
        if (formatCodes == null || formatCodes.isEmpty() || number == null || num == null) {
            facts.put("num", 0);
            return;
        }

        // 获取第一组的长度
        int m = formatCodes.get(0).size();

        // 检查是否满足最小选择值
        if (m < number) {
            facts.put("num", 0);
        } else {
            // 计算组合数 C(m, n)
            int combinationCount = calculateCombination(m, number);
            facts.put("num", combinationCount);
        }
    }

    // Helper method to calculate combinations C(n, r) = n! / (r! * (n - r)!)
    private int calculateCombination(int n, int r) {
        if (r > n || n <= 0 || r < 0) return 0;
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
