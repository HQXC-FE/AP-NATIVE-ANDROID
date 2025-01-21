package com.xtree.lottery.rule.betting.decision;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
            // 获取相关数据
            List<List<String>> formatCodes = facts.get("formatCodes");
            Integer num = facts.get("num");

            // 检查输入的有效性
            if (formatCodes == null || formatCodes.isEmpty() || num == null) {
                facts.put("num", 0);
                return;
            }

            // 检查是否每个位置都有选择值
            boolean noFinish = formatCodes.stream().anyMatch(List::isEmpty);
            if (noFinish) {
                facts.put("num", 0);
                return;
            }

            // 生成所有组合方式（笛卡尔积）
            List<List<String>> allCombination = cartesianProduct(formatCodes);

            // 筛选出每个位置值唯一的组合
            int validCombinationCount = (int) allCombination.stream()
                    .filter(combination -> isUnique(combination))
                    .count();

            facts.put("num", validCombinationCount);
        } catch (Exception e) {
            CfLog.e(e.getMessage());
        }
    }

    // Helper method: Cartesian Product
    private List<List<String>> cartesianProduct(List<List<String>> lists) {
        List<List<String>> resultLists = new java.util.ArrayList<>();
        if (lists.isEmpty()) {
            resultLists.add(new java.util.ArrayList<>());
            return resultLists;
        } else {
            List<String> firstList = lists.get(0);
            List<List<String>> remainingLists = cartesianProduct(lists.subList(1, lists.size()));
            for (String condition : firstList) {
                for (List<String> remainingList : remainingLists) {
                    List<String> resultList = new java.util.ArrayList<>();
                    resultList.add(condition);
                    resultList.addAll(remainingList);
                    resultLists.add(resultList);
                }
            }
        }
        return resultLists;
    }

    // Helper method: Check if a combination contains unique elements
    private boolean isUnique(List<String> combination) {
        Set<String> uniqueSet = new HashSet<>(combination);
        return uniqueSet.size() == combination.size();
    }
}

