package com.xtree.lottery.rule.betting.decision;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Rule(name = "SumPlusDifRule", description = "三不同号和值选号")
public class SumPlusDifRule {

    @Priority
    public int getPriority() {
        return -19600;
    }

    @Condition
    public boolean when(Facts facts) {
        // 检查是否包含 "sum-plus-dif"
        List<String> ruleSuite = facts.get("ruleSuite");
        return ruleSuite != null && ruleSuite.contains("sum-plus-dif");
    }

    @Action
    public void then(Facts facts) {
        try {
            // 初始化数据
            List<List<Integer>> formatCodes = facts.get("formatCodes");
            Map<String, Object> attached = facts.get("attached");
            Integer num = facts.get("num");
            if (formatCodes == null || formatCodes.isEmpty() || attached == null || num == null) {
                facts.put("num", 0);
                return;
            }

            // 提取配置
            List<Integer> scope = (List<Integer>) attached.getOrDefault("scope", Arrays.asList(1, 2, 3, 4, 5, 6));
            int number = (Integer) attached.get("number");
            boolean isTail = (Boolean) attached.getOrDefault("isTail", false);
            List<String> flag = (List<String>) attached.getOrDefault("flag", new ArrayList<>());

            // 构造待组合二维数组
            List<List<Integer>> pendingArr = Collections.nCopies(number, scope);

            // 获取全部组合
            List<List<Integer>> allCombination = cartesianProduct(pendingArr);

            int resultNum = 0;

            for (int codeStr : formatCodes.get(0)) {
                int code = codeStr;
                List<List<Integer>> validCombinations = new ArrayList<>();

                // 过滤符合条件的组合
                for (List<Integer> combination : allCombination) {
                    int sum = combination.stream().mapToInt(Integer::intValue).sum();
                    if ((!isTail && sum == code) || (isTail && sum % 10 == code)) {
                        validCombinations.add(new ArrayList<>(combination.stream().sorted().toList()));
                    }
                }

                if (flag.contains("group")) {
                    // 组选和值
                    validCombinations = validCombinations.stream()
                            .filter(item -> new HashSet<>(item).size() != 1) // 过滤全相等的组合
                            .distinct() // 去重
                            .collect(Collectors.toList());

                    int duplicateCount = (int) validCombinations.stream()
                            .filter(item -> hasDuplicates(item))
                            .count();

                    resultNum += validCombinations.size() - duplicateCount;
                } else {
                    resultNum += validCombinations.size();
                }
            }

            facts.put("num", resultNum);
        } catch (Exception e) {
            CfLog.e(e.getMessage());
        }
    }

    // Helper: Cartesian Product
    private List<List<Integer>> cartesianProduct(List<List<Integer>> lists) {
        List<List<Integer>> resultLists = new ArrayList<>();
        if (lists.isEmpty()) {
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

    // Helper: Check if a list has duplicates
    private boolean hasDuplicates(List<Integer> list) {
        Set<Integer> uniqueSet = new HashSet<>(list);
        return uniqueSet.size() != list.size();
    }
}
