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
import java.util.stream.Collectors;

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
            int num = 0;

            if (formatCodes != null) {
                boolean noFinish = formatCodes.stream().anyMatch(List::isEmpty);

                if (noFinish) {
                    num = 0;
                } else {
                    List<List<String>> allCombinations = cartesianProduct(formatCodes);
                    List<List<String>> validCombinations = allCombinations.stream()
                            .filter(item -> item.size() == new HashSet<>(item).size()) // 確保所有元素唯一
                            .collect(Collectors.toList());

                    num = validCombinations.size();
                }
            }

            facts.put("num", num);
        } catch (Exception e) {
            System.err.println("Error in RankingChosenRule: " + e.getMessage());
        }
    }

    private List<List<String>> cartesianProduct(List<List<String>> lists) {
        if (lists.isEmpty()) return List.of(List.of());
        List<String> firstList = lists.get(0);
        List<List<String>> rest = cartesianProduct(lists.subList(1, lists.size()));

        return firstList.stream()
                .flatMap(item -> rest.stream().map(subList -> {
                    List<String> newList = new java.util.ArrayList<>(subList);
                    newList.add(0, item);
                    return newList;
                }))
                .collect(Collectors.toList());
    }
}

