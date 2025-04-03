package com.xtree.lottery.rule.betting.decision;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@Rule(name = "RankingChosenRule", description = "猜名次算法")
public class RankingChosenRule {

    @Priority
    public int getPriority() {
        return -19200;
    }

    @Condition
    public boolean when(Facts facts) {
        // 检查是否包含 "ranking-chosen"
        List<String> ruleSuite = facts.get("ruleSuite");
        return ruleSuite != null && ruleSuite.contains("ranking-chosen");
    }

    @Action
    public void then(Facts facts) {
        try {
            List<List<String>> formatCodes = facts.get("formatCodes");
            if (formatCodes == null) {
                facts.put("num", 0);
                return;
            }

            // **提前检查是否有空列表**
            for (List<?> list : formatCodes) {
                if (list.isEmpty()) {
                    facts.put("num", 0);
                    return;
                }
            }

            // **使用迭代方式计算笛卡尔积**
            List<List<String>> allCombination = cartesianProductIterative(formatCodes);

            // **优化去重逻辑**
            long num = allCombination.stream()
                    .filter(item -> item.size() == new LinkedHashSet<>(item).size())
                    .count();

            facts.put("num", (int) num);
        } catch (Exception e) {
            CfLog.e("Error in RankingChosenRule: " + e.getMessage());
        }
    }

    /**
     * **优化版：使用迭代方式计算笛卡尔积**
     */
    private static List<List<String>> cartesianProductIterative(List<List<String>> lists) {
        List<List<String>> result = new ArrayList<>();
        result.add(new ArrayList<>()); // 初始化一个空列表

        for (List<String> list : lists) {
            List<List<String>> newResult = new ArrayList<>();
            for (List<String> combination : result) {
                for (String element : list) {
                    List<String> newCombination = new ArrayList<>(combination);
                    newCombination.add(element);
                    newResult.add(newCombination);
                }
            }
            result = newResult;
        }
        return result;
    }
}

