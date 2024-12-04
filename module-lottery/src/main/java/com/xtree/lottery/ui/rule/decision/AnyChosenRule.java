package com.xtree.lottery.ui.rule.decision;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Rule(name = "AnyChosenRule", description = "任选直选玩法规则")
public class AnyChosenRule {

    @Priority
    public int getPriority() {
        return 19500;
    }

    @Condition
    public boolean when(Map<String, Object> facts) {
        List<String> ruleSuite = (List<String>) facts.get("ruleSuite");
        return ruleSuite.contains("any-chosen");
    }

    @Action
    public void then(Map<String, Object> facts) {
        List<List<String>> formatCodes = (List<List<String>>) facts.get("formatCodes");
        int number = (int) ((Map<String, Object>) facts.get("attached")).get("number");

        int num = 0;
        List<Integer> eachNumbers = new ArrayList<>();

        List<int[]> positionGroup = getCombinations(IntStream.range(0, formatCodes.size()).toArray(), number);

        for (int[] positions : positionGroup) {
            List<List<String>> currentCodeSet = new ArrayList<>();
            boolean isFinish = true;

            for (int index : positions) {
                if (formatCodes.get(index).isEmpty()) {
                    isFinish = false;
                    break;
                }
                currentCodeSet.add(formatCodes.get(index));
            }

            if (isFinish) {
                int combinationCount = cartesianProduct(currentCodeSet).size();
                eachNumbers.add(combinationCount);
                num += combinationCount;
            }
        }

        facts.put("minNumber", eachNumbers.stream().min(Integer::compare).orElse(0));
        facts.put("num", num);
    }

    private List<int[]> getCombinations(int[] elements, int k) {
        List<int[]> result = new ArrayList<>();
        combinationHelper(elements, new int[k], 0, 0, result);
        return result;
    }

    private void combinationHelper(int[] elements, int[] combination, int start, int depth, List<int[]> result) {
        if (depth == combination.length) {
            result.add(Arrays.copyOf(combination, combination.length));
            return;
        }
        for (int i = start; i < elements.length; i++) {
            combination[depth] = elements[i];
            combinationHelper(elements, combination, i + 1, depth + 1, result);
        }
    }

    private List<List<String>> cartesianProduct(List<List<String>> lists) {
        List<List<String>> result = new ArrayList<>();
        cartesianHelper(lists, 0, new ArrayList<>(), result);
        return result;
    }

    private void cartesianHelper(List<List<String>> lists, int depth, List<String> current, List<List<String>> result) {
        if (depth == lists.size()) {
            result.add(new ArrayList<>(current));
            return;
        }
        for (String item : lists.get(depth)) {
            current.add(item);
            cartesianHelper(lists, depth + 1, current, result);
            current.remove(current.size() - 1);
        }
    }
}
