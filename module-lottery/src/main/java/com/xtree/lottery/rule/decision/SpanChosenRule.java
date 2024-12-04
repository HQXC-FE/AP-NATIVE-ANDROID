package com.xtree.lottery.rule.decision;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Rule(name = "SpanChosenRule", description = "跨度算法")
public class SpanChosenRule {

    @Priority
    public int getPriority() {
        return 19300;
    }

    @Condition
    public boolean when(Map<String, Object> facts) {
        // 检查是否包含 "span-chosen"
        List<String> ruleSuite = (List<String>) facts.get("ruleSuite");
        return ruleSuite != null && ruleSuite.contains("span-chosen");
    }

    @Action
    public void then(Map<String, Object> facts) {
        // 从 facts 中获取必要数据
        Integer number = (Integer) facts.get("attached.number");
        List<Integer> scope = (List<Integer>) facts.get("attached.scope");
        List<List<String>> formatCodes = (List<List<String>>) facts.get("formatCodes");
        Integer num = (Integer) facts.get("num");

        // 默认 scope 为 0-9
        if (scope == null) {
            scope = new ArrayList<>();
            for (int i = 0; i <= 9; i++) {
                scope.add(i);
            }
        }

        // 验证 number 是否有效
        if (number == null || number <= 0 || formatCodes == null || formatCodes.isEmpty()) {
            facts.put("num", 0);
            return;
        }

        // 构建所有组合方式
        List<List<Integer>> pendingArr = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            pendingArr.add(new ArrayList<>(scope));
        }

        List<List<Integer>> allCombination = cartesianProduct(pendingArr);

        // 排序每个组合并计算跨度
        int validCount = 0;
        for (List<Integer> combination : allCombination) {
            combination.sort(Integer::compareTo);
            int span = combination.get(combination.size() - 1) - combination.get(0);

            for (List<String> eachCodes : formatCodes) {
                for (String code : eachCodes) {
                    if (span == Integer.parseInt(code)) {
                        validCount++;
                        break;
                    }
                }
            }
        }

        facts.put("num", validCount);
    }

    // Helper method: Cartesian Product
    private List<List<Integer>> cartesianProduct(List<List<Integer>> lists) {
        List<List<Integer>> resultLists = new ArrayList<>();
        if (lists.size() == 0) {
            resultLists.add(new ArrayList<>());
            return resultLists;
        } else {
            List<Integer> firstList = lists.get(0);
            List<List<Integer>> remainingLists = cartesianProduct(lists.subList(1, lists.size()));
            for (Integer condition : firstList) {
                for (List<Integer> remainingList : remainingLists) {
                    List<Integer> resultList = new ArrayList<>();
                    resultList.add(condition);
                    resultList.addAll(remainingList);
                    resultLists.add(resultList);
                }
            }
        }
        return resultLists;
    }
}

