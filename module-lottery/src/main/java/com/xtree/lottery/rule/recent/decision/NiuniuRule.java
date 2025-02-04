package com.xtree.lottery.rule.recent.decision;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@Rule(name = "Niuniu", description = "Analyze Niuniu shape based on history codes")
public class NiuniuRule {

    @Priority
    public int getPriority() {
        return -66000;
    }

    @Condition
    public boolean when(Facts facts) {
        List<String> ruleSuite = facts.get("ruleSuite");
        return ruleSuite != null && ruleSuite.contains("NIUNIU");
    }

    @Action
    public void then(Facts facts) {
        try {
            List<Map<String, Object>> historyCodes = facts.get("historyCodes");
            for (Map<String, Object> history : historyCodes) {
                List<Integer> workCode = (List<Integer>) history.get("workCode");
                List<List<Integer>> combinations = generateCombinations(workCode, 3);
                List<Integer> niuArr = combinations.stream()
                        .filter(combo -> combo.stream().mapToInt(Integer::intValue).sum() % 10 == 0)
                        .findFirst()
                        .orElse(Collections.emptyList());

                List<Map<String, Object>> form = new ArrayList<>();
                if (niuArr.isEmpty()) {
                    form.add(Map.of("label", "无牛", "className", "#476efe"));
                    history.put("form", form);
                    history.put("displayCode", mapToDisabled(workCode));
                    continue;
                }

                List<Integer> niuIndex = findIndices(workCode, niuArr);
                int niuNum = (workCode.stream().mapToInt(Integer::intValue).sum() - niuArr.stream().mapToInt(Integer::intValue).sum()) % 10;

                if (niuNum == 0) {
                    form.add(Map.of("label", "牛牛", "className", "#f95016"));
                } else {
                    form.add(Map.of("label", "牛", "className", "#476efe"));
                    form.add(Map.of("label", niuNum, "className", "#f95016"));
                }

                form.add(Map.of("label", " ", "className", "#000000"));

                if (niuNum > 5 || niuNum == 0) {
                    form.add(Map.of("label", "牛", "className", "#476efe"));
                    form.add(Map.of("label", "大", "className", "#f95016"));
                } else {
                    form.add(Map.of("label", "牛", "className", "#476efe"));
                    form.add(Map.of("label", "小", "className", "#f95016"));
                }

                form.add(Map.of("label", " ", "className", "#000000"));

                if (niuNum % 2 == 1) {
                    form.add(Map.of("label", "牛", "className", "#476efe"));
                    form.add(Map.of("label", "单", "className", "#f95016"));
                } else {
                    form.add(Map.of("label", "牛", "className", "#476efe"));
                    form.add(Map.of("label", "双", "className", "#f95016"));
                }

                history.put("form", form);
                history.put("displayCode", mapToDisplay(workCode, niuIndex));
            }
        } catch (Exception e) {
            CfLog.e("Error in NiuniuRule: " + e.getMessage());
        }
    }

    private List<List<Integer>> generateCombinations(List<Integer> array, int size) {
        List<List<Integer>> combinations = new ArrayList<>();
        int n = array.size();
        int[] indices = IntStream.range(0, size).toArray();
        while (indices[size - 1] < n) {
            combinations.add(IntStream.of(indices).mapToObj(array::get).toList());
            int t = size - 1;
            while (t != 0 && indices[t] == n - size + t) t--;
            indices[t]++;
            for (int i = t + 1; i < size; i++) indices[i] = indices[i - 1] + 1;
        }
        return combinations;
    }

    private List<Map<String, String>> mapToDisabled(List<Integer> workCode) {
        return workCode.stream()
                .map(item -> Map.of("codes", item.toString(), "className", "disabled"))
                .toList();
    }

    private List<Map<String, String>> mapToDisplay(List<Integer> workCode, List<Integer> niuIndex) {
        return IntStream.range(0, workCode.size())
                .mapToObj(i -> Map.of(
                        "codes", workCode.get(i).toString(),
                        "className", niuIndex.contains(i) ? "active" : "disabled"))
                .toList();
    }

    private List<Integer> findIndices(List<Integer> workCode, List<Integer> niuArr) {
        List<Integer> indices = new ArrayList<>();
        List<Integer> prevItems = new ArrayList<>();
        int startIndex = 0;
        for (Integer item : niuArr) {
            int index = IntStream.range(startIndex, workCode.size())
                    .filter(i -> workCode.get(i).equals(item) && !prevItems.contains(i))
                    .findFirst().orElse(-1);
            indices.add(index);
            prevItems.add(index);
            startIndex = index + 1;
        }
        return indices;
    }
}