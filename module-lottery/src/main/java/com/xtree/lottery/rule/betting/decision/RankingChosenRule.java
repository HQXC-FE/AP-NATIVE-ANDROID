package com.xtree.lottery.rule.betting.decision;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
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
                boolean noFinish = false;
                for (List<?> list : formatCodes) {
                    if (list.isEmpty()) {
                        noFinish = true;
                        break; // 发现空列表后立即退出循环，提升效率
                    }
                }
                if (noFinish) {
                    num = 0;
                } else {
                    // 计算笛卡尔积
                    List<List<String>> allCombination = cartesianProduct(formatCodes);

                    // 过滤掉有重复元素的组合
                    Iterator<List<String>> iterator = allCombination.iterator();
                    while (iterator.hasNext()) {
                        List<String> item = iterator.next();
                        if (item.size() != new HashSet<>(item).size()) {
                            iterator.remove(); // 通过迭代器安全删除元素
                        }
                    }
                    // 计算组合数量
                    num = allCombination.size();
                }
            }

            facts.put("num", num);
        } catch (Exception e) {
            System.err.println("Error in RankingChosenRule: " + e.getMessage());
        }
    }

    // 计算笛卡尔积
    private static List<List<String>> cartesianProduct(List<List<String>> lists) {
        List<List<String>> result = new ArrayList<>();
        cartesianRecursive(lists, result, new ArrayList<>(), 0);
        return result;
    }

    private static void cartesianRecursive(List<List<String>> lists, List<List<String>> result, List<String> temp, int depth) {
        if (depth == lists.size()) {
            result.add(new ArrayList<>(temp));
            return;
        }
        for (String item : lists.get(depth)) {
            temp.add(item);
            cartesianRecursive(lists, result, temp, depth + 1);
            temp.remove(temp.size() - 1); // 回溯
        }
    }
}

