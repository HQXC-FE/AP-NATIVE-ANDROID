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

@Rule(name = "SumPlusRule", description = "和值规则")
public class SumPlusRule {

    @Priority
    public int getPriority() {
        return -19600;
    }

    @Condition
    public boolean when(Facts facts) {
        List<String> ruleSuite = facts.get("ruleSuite");
        return ruleSuite.contains("sum-plus");
    }

    @Action
    public void then(Facts facts) {
        try {
            Map<String, Object> attached = facts.get("attached");
            Object formatCodesObject = facts.get("formatCodes");
            List<List<String>> formatCodes = normalizeFormatCodes(formatCodesObject);
            List<Object> newFormatCodes = new ArrayList<>();

            // 初始化 scope
            List<Integer> scope = (List<Integer>) attached.getOrDefault("scope", IntStream.rangeClosed(0, 9).boxed().collect(Collectors.toList()));

            // 获取待组合二维数组
            int number = 0;
            if (attached.get("number") != null) {
                number = Integer.parseInt((String) attached.get("number"));
            }
            boolean isTail;
            List<String> flag = new ArrayList<>();
            List<List<Integer>> pendingArr = Collections.nCopies(number, scope);

            // 获取全部组合类型
            List<List<Integer>> allCombination = cartesianProduct(pendingArr);
            if (attached.get("isTail") != null) {
                isTail = (boolean) attached.get("isTail");
            } else {
                isTail = false;
            }
            if (attached.get("flags") != null) {
                flag = (List<String>) attached.get("flags");
            }

            int num = 0;

            for (String codeStr : formatCodes.get(0)) {
                int code;
                if (codeStr != null && !codeStr.isEmpty() && !codeStr.equals(" ")) {
                    code = Integer.parseInt(codeStr);
                } else {
                    break;
                }
                List<List<Integer>> eachCombination = new ArrayList<>();

                for (List<Integer> codeSet : allCombination) {
                    int sum = codeSet.stream().mapToInt(Integer::intValue).sum();
                    if (!isTail && sum == code || isTail && sum % 10 == code) {
                        eachCombination.add(codeSet.stream().sorted().collect(Collectors.toList()));
                    }
                }

                if (flag.contains("group")) {
                    // 组选和值
                    eachCombination = eachCombination.stream()
                            .filter(item -> new HashSet<>(item).size() != 1)
                            .collect(Collectors.toList());

                    num += eachCombination.stream()
                            .distinct()
                            .collect(Collectors.toList())
                            .size();
                } else {
                    num += eachCombination.size();
                }
            }

            // 排序
            if (formatCodes != null && !formatCodes.isEmpty() && formatCodes.get(0) != null) {
                List<String> firstList = formatCodes.get(0);
                try {
                    List<String> sortedList = firstList.stream()
                            .filter(item -> item != null && !item.trim().isEmpty()) // 过滤 null 或空字符串
                            .map(Integer::parseInt) // 将字符串转换为整数
                            .sorted() // 排序
                            .map(String::valueOf) // 转回字符串
                            .collect(Collectors.toList());
                    newFormatCodes.add(sortedList); // 更新第一个列表
                } catch (Exception e) {
                    CfLog.e(e.getMessage());
                }
            }

            facts.put("formatCodes", newFormatCodes);
            facts.put("num", num);
        } catch (Exception e) {
            CfLog.e(e.getMessage());
        }
    }

    /**
     * 归一化 formatCodes 为 List<List<String>>
     */
    private List<List<String>> normalizeFormatCodes(Object formatCodesObject) {
        if (formatCodesObject instanceof String) {
            // 如果是单个字符串，将其转为 List<List<String>>
            return Collections.singletonList(Collections.singletonList((String) formatCodesObject));
        } else if (formatCodesObject instanceof List) {
            List<?> list = (List<?>) formatCodesObject;
            if (!list.isEmpty() && list.get(0) instanceof String) {
                // 如果是 List<String>，转为 List<List<String>>
                return Collections.singletonList(((List<String>) list).stream()
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.toList()));
            } else if (!list.isEmpty() && list.get(0) instanceof List) {
                // 如果是 List<List<String>>，直接返回
                return (List<List<String>>) list;
            }
        }
        throw new IllegalArgumentException("Invalid formatCodes type: " + formatCodesObject.getClass().getSimpleName());
    }

    /**
     * 计算笛卡尔积
     */
    private List<List<Integer>> cartesianProduct(List<List<Integer>> lists) {
        List<List<Integer>> result = new ArrayList<>();
        cartesianProductHelper(lists, result, 0, new ArrayList<>());
        return result;
    }

    private void cartesianProductHelper(List<List<Integer>> lists, List<List<Integer>> result, int depth, List<Integer> current) {
        if (depth == lists.size()) {
            result.add(new ArrayList<>(current));
            return;
        }
        for (int i : lists.get(depth)) {
            current.add(i);
            cartesianProductHelper(lists, result, depth + 1, current);
            current.remove(current.size() - 1);
        }
    }

    /**
     * 对列表进行排序
     */
    private List<Integer> sortList(List<Integer> list) {
        return list.stream().sorted().collect(Collectors.toList());
    }
}

