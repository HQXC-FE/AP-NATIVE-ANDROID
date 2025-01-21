package com.xtree.lottery.rule.betting.decision;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Rule(name = "MultiCombinationRule", description = "组选玩法规则")
public class MultiCombinationRule {

    @Priority
    public int getPriority() {
        return -19700;
    }

    @Condition
    public boolean when(Facts facts) {
        List<String> ruleSuite = facts.get("ruleSuite");
        return ruleSuite.contains("multi-combination");
    }

    @Action
    public void then(Facts facts) {
        try {
            List<Object> formatCodes = facts.get("formatCodes");
            Map<String, Map<String, List<Map<String, Object>>>> currentMethod = facts.get("currentMethod");
            Map<String, List<Map<String, Object>>> selectarea = currentMethod.get("selectarea");  // selectarea.layout 结构化为 Map
            List<Map<String, Object>> layout = selectarea.get("layout");
            boolean noFinish = false;

            // 检查是否有任何行未达到最小选择数
            for (Map<String, Object> item : layout) {
                int place = Integer.parseInt((String) item.get("place"));
                int minChosen = Integer.parseInt((String) item.get("minchosen"));

                if (formatCodes.get(0) instanceof String) {
                    if ((formatCodes).size() < minChosen) {
                        noFinish = true;
                        break;
                    }
                } else if (formatCodes.get(0) instanceof List) {
                    if (((List<String>) formatCodes.get(place)).size() < minChosen) {
                        noFinish = true;
                        break;
                    }
                }
            }

            if (noFinish) {
                facts.put("num", 0);
            } else {
                List<List<List<String>>> eachCombination = new ArrayList<>();
                if (formatCodes.get(0) instanceof String) {
                    // 计算组合数
                    eachCombination = IntStream.range(0, 1)
                            .mapToObj(index -> {
                                int minChosen = Integer.parseInt((String) layout.get(index).get("minchosen"));
                                Object element = formatCodes.get(index);

                                // 检查类型：如果是 List<String>，直接使用；如果是 String，则将其包装成 List<String>
                                List<String> currentList = new ArrayList<>();

                                for (Object item : formatCodes) {
                                    currentList.add((String) item);
                                }

                                // 调用 getCombinations 计算组合
                                return getCombinations(currentList, minChosen);
                            })
                            .collect(Collectors.toList());
                } else if (formatCodes.get(0) instanceof List) {
                    // 计算组合数
                    eachCombination = IntStream.range(0, formatCodes.size())
                            .mapToObj(index -> {
                                int minChosen = Integer.parseInt((String) layout.get(index).get("minchosen"));
                                Object element = formatCodes.get(index);

                                // 检查类型：如果是 List<String>，直接使用；如果是 String，则将其包装成 List<String>
                                List<String> currentList;
                                currentList = (List<String>) element;  // 如果是 List<String>，直接赋值
                                // 调用 getCombinations 计算组合
                                return getCombinations(currentList, minChosen);
                            })
                            .collect(Collectors.toList());
                }

                List<List<String>> allCombination = cartesianProduct(eachCombination);

                long num = allCombination.stream()
                        .filter(item -> item.size() == new HashSet<>(item).size())  // 去重判断
                        .count();

                facts.put("num", (int) num);

            }
        } catch (Exception e) {
            CfLog.e(e.getMessage());
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

