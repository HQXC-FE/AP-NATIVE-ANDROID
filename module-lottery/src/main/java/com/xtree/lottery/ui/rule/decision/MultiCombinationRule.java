package com.xtree.lottery.ui.rule.decision;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Rule(name = "MultiCombinationRule", description = "组选玩法规则")
public class MultiCombinationRule {

    @Priority
    public int getPriority() {
        return 19700;
    }

    @Condition
    public boolean when(Map<String, Object> facts) {
        List<String> ruleSuite = (List<String>) facts.get("ruleSuite");
        return ruleSuite.contains("multi-combination");
    }

    @Action
    public void then(Map<String, Object> facts) {
        List<List<String>> formatCodes = (List<List<String>>) facts.get("formatCodes");
        List<Map<String, Object>> layout = (List<Map<String, Object>>) ((Map<String, Object>) facts.get("currentMethod")).get("selectarea.layout");

        boolean noFinish = layout.stream().anyMatch(item -> formatCodes.get((int) item.get("place")).size() < (int) item.get("minchosen"));

        if (noFinish) {
            facts.put("num", 0);
            return;
        }

        List<List<List<String>>> eachCombination = IntStream.range(0, formatCodes.size())
                .mapToObj(index -> getCombinations(formatCodes.get(index), (int) layout.get(index).get("minchosen")))
                .collect(Collectors.toList());

        List<List<String>> allCombination = cartesianProduct(eachCombination);

        long validCombinations = allCombination.stream()
                .filter(comb -> comb.size() == new HashSet<>(comb).size())
                .count();

        facts.put("num", (int) validCombinations);
    }

    private List<List<String>> getCombinations(List<String> items, int minChosen) {
        if (items.size() < minChosen) return Collections.emptyList();
        List<List<String>> combinations = new ArrayList<>();
        generateCombinations(items, minChosen, 0, new ArrayList<>(), combinations);
        return combinations;
    }

    private void generateCombinations(List<String> items, int minChosen, int start, List<String> current, List<List<String>> result) {
        if (current.size() == minChosen) {
            result.add(new ArrayList<>(current));
            return;
        }
        for (int i = start; i < items.size(); i++) {
            current.add(items.get(i));
            generateCombinations(items, minChosen, i + 1, current, result);
            current.remove(current.size() - 1);
        }
    }

    private List<List<String>> cartesianProduct(List<List<List<String>>> lists) {
        List<List<String>> result = new ArrayList<>();
        cartesianProductHelper(lists, 0, new ArrayList<>(), result);
        return result;
    }

    private void cartesianProductHelper(List<List<List<String>>> lists, int depth, List<String> current, List<List<String>> result) {
        if (depth == lists.size()) {
            result.add(new ArrayList<>(current));
            return;
        }
        for (List<String> list : lists.get(depth)) {
            current.addAll(list);
            cartesianProductHelper(lists, depth + 1, current, result);
            for (int i = 0; i < list.size(); i++) current.remove(current.size() - 1);
        }
    }
}

