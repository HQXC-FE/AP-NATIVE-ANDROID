package com.xtree.lottery.rule.decision;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.*;
import java.util.stream.Collectors;

@Rule(name = "SumPlusRule", description = "和值规则")
public class SumPlusRule {

    @Priority
    public int getPriority() {
        return 19600;
    }

    @Condition
    public boolean when(Facts facts) {
        List<String> ruleSuite = facts.get("ruleSuite");
        return ruleSuite.contains("sum-plus");
    }

    @Action
    public void then(Facts facts) {
        List<Integer> scope = (List<Integer>) ((Map<String, Object>) facts.get("attached")).getOrDefault("scope", Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
        int number = (int) ((Map<String, Object>) facts.get("attached")).get("number");
        boolean isTail = (boolean) ((Map<String, Object>) facts.get("attached")).getOrDefault("isTail", false);
        String flag = (String) ((Map<String, Object>) facts.get("attached")).get("flag");

        List<String> formatCodes = ((List<List<String>>) facts.get("formatCodes")).get(0);
        List<List<Integer>> pendingArr = Collections.nCopies(number, scope);
        List<List<Integer>> allCombination = cartesianProduct(pendingArr);

        int num = 0;

        for (String codeStr : formatCodes) {
            int code = Integer.parseInt(codeStr);
            List<List<Integer>> eachCombination = allCombination.stream()
                    .filter(codeSet -> isTail ? codeSet.stream().mapToInt(Integer::intValue).sum() % 10 == code
                            : codeSet.stream().mapToInt(Integer::intValue).sum() == code)
                    .map(codeSet -> codeSet.stream().sorted().collect(Collectors.toList()))
                    .collect(Collectors.toList());

            if ("group".equals(flag)) {
                eachCombination = eachCombination.stream()
                        .filter(item -> new HashSet<>(item).size() > 1) // 组选不允许全相同
                        .distinct()
                        .collect(Collectors.toList());
            }

            num += eachCombination.size();
        }

        formatCodes.sort(Comparator.comparingInt(Integer::parseInt));
        facts.put("formatCodes", Collections.singletonList(formatCodes));
        facts.put("num", num);
    }

    private List<List<Integer>> cartesianProduct(List<List<Integer>> lists) {
        List<List<Integer>> result = new ArrayList<>();
        cartesianHelper(lists, 0, new ArrayList<>(), result);
        return result;
    }

    private void cartesianHelper(List<List<Integer>> lists, int depth, List<Integer> current, List<List<Integer>> result) {
        if (depth == lists.size()) {
            result.add(new ArrayList<>(current));
            return;
        }
        for (Integer item : lists.get(depth)) {
            current.add(item);
            cartesianHelper(lists, depth + 1, current, result);
            current.remove(current.size() - 1);
        }
    }
}

