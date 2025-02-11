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
            Map<String, Object> attached = facts.get("attached");
            List<Integer> scope = (List<Integer>) attached.getOrDefault("scope", Arrays.asList(1, 2, 3, 4, 5, 6));
            Integer number = Integer.parseInt((String) attached.get("number"));
            boolean isTail;
            List<String> flag = (List<String>) attached.get("flags");
            List<String> formatCodes = facts.get("formatCodes");

            if (attached.get("isTail") != null) {
                isTail = (boolean) attached.get("isTail");
            } else {
                isTail = false;
            }

            // Generate all possible combinations
            List<List<Integer>> pendingArr = Collections.nCopies(number, scope);
            List<List<Integer>> allCombinations = cartesianProduct(pendingArr);

            int num = 0;
            for (String code : formatCodes) {
                List<List<Integer>> eachCombination = new ArrayList<>();
                int codeNum = Integer.parseInt(code);

                for (List<Integer> codeSet : allCombinations) {
                    int sum = codeSet.stream().mapToInt(Integer::intValue).sum();
                    if ((!isTail && sum == codeNum) || (isTail && sum % 10 == codeNum)) {
                        eachCombination.add(codeSet.stream().sorted().collect(Collectors.toList()));
                    }
                }

                if (flag.contains("group")) {
                    eachCombination = eachCombination.stream()
                            .filter(set -> new HashSet<>(set).size() != 1)
                            .distinct()
                            .collect(Collectors.toList());

                    int duplicateCount = 0;
                    for (List<Integer> codeSet : eachCombination) {
                        if (codeSet.get(0).equals(codeSet.get(1)) || codeSet.get(0).equals(codeSet.get(2)) || codeSet.get(1).equals(codeSet.get(2))) {
                            duplicateCount++;
                        }
                    }
                    num += eachCombination.size() - duplicateCount;
                } else {
                    num += eachCombination.size();
                }
            }

            facts.put("num", num);
        } catch (Exception e) {
            CfLog.e(e.getMessage());
        }
    }

    private List<List<Integer>> cartesianProduct(List<List<Integer>> lists) {
        List<List<Integer>> result = new ArrayList<>();
        if (lists.isEmpty()) return result;
        result.add(new ArrayList<>());

        for (List<Integer> list : lists) {
            List<List<Integer>> newResult = new ArrayList<>();
            for (List<Integer> current : result) {
                for (Integer item : list) {
                    List<Integer> newCombination = new ArrayList<>(current);
                    newCombination.add(item);
                    newResult.add(newCombination);
                }
            }
            result = newResult;
        }
        return result;
    }
}
