package com.xtree.lottery.rule.betting.decision;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Rule(name = "SpanChosenRule", description = "跨度算法")
public class SpanChosenRule {

    @Priority
    public int getPriority() {
        return -19300;
    }

    @Condition
    public boolean when(Facts facts) {
        // 检查是否包含 "span-chosen"
        List<String> ruleSuite = facts.get("ruleSuite");
        return ruleSuite != null && ruleSuite.contains("span-chosen");
    }

    @Action
    public void then(Facts facts) {
        try {
            // 从 facts 中获取必要数据
            Map<String, Object> attached = facts.get("attached");
            Integer number = Integer.parseInt((String) attached.get("number"));
            List<Integer> scope = (List<Integer>) attached.get("scope");
            List<Object> formatCodes = facts.get("formatCodes");

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

                if (formatCodes.get(0) instanceof String) {
                    for (Object eachCodes : formatCodes) {
                        if (span == Integer.parseInt((String) eachCodes)) {
                            validCount++;
                            break;
                        }
                    }
                } else {
                    for (Object eachCodes : formatCodes) {
                        for (String code : (List<String>) eachCodes) {
                            if (span == Integer.parseInt(code)) {
                                validCount++;
                                break;
                            }
                        }
                    }
                }
            }

            facts.put("num", validCount);
        } catch (Exception e) {
            CfLog.e(e.getMessage());
        }
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

