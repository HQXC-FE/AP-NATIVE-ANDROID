package com.xtree.lottery.rule.decision;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

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
    public boolean when(Facts facts) {
        List<String> ruleSuite = facts.get("ruleSuite");
        return ruleSuite.contains("multi-combination");
    }

    @Action
    public void then(Facts facts) {
        List<List<String>> formatCodes = facts.get("formatCodes");
        List<Map<String, Object>> layout = facts.get("layout");  // selectarea.layout 结构化为 Map
        boolean noFinish = false;

        // 检查是否有任何行未达到最小选择数
        for (Map<String, Object> item : layout) {
            int place = (int) item.get("place");
            int minChosen = (int) item.get("minchosen");

            if (formatCodes.get(place).size() < minChosen) {
                noFinish = true;
                break;
            }
        }

        if (noFinish) {
            facts.put("num", 0);
        } else {
            // 计算组合数
            List<List<List<String>>> eachCombination = IntStream.range(0, formatCodes.size())
                    .mapToObj(index -> {
                        int minChosen = (int) layout.get(index).get("minchosen");
                        return getCombinations(formatCodes.get(index), minChosen);
                    }).collect(Collectors.toList());

            List<List<String>> allCombination = cartesianProduct(eachCombination);

            long num = allCombination.stream()
                    .filter(item -> item.size() == new HashSet<>(item).size())  // 去重判断
                    .count();

            facts.put("num", (int) num);
        }
    }

    // 获取组合的方法
    private List<List<String>> getCombinations(List<String> items, int k) {
        List<List<String>> result = new ArrayList<>();
        combine(items, new ArrayList<>(), 0, k, result);
        return result;
    }

    private void combine(List<String> items, List<String> temp, int start, int k, List<List<String>> result) {
        if (temp.size() == k) {
            result.add(new ArrayList<>(temp));
            return;
        }
        for (int i = start; i < items.size(); i++) {
            temp.add(items.get(i));
            combine(items, temp, i + 1, k, result);
            temp.remove(temp.size() - 1);
        }
    }

    // 笛卡尔积（所有行组合项合并）
    private List<List<String>> cartesianProduct(List<List<List<String>>> lists) {
        return lists.stream().reduce((a, b) ->
                a.stream().flatMap(x -> b.stream().map(y -> {
                    List<String> newList = new ArrayList<>(x);
                    newList.addAll(y);
                    return newList;
                })).collect(Collectors.toList())
        ).orElse(Collections.emptyList());
    }
}

