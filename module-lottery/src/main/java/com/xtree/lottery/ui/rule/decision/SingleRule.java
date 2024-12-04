package com.xtree.lottery.ui.rule.decision;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Rule(name = "SingleRule", description = "通用单式规则")
public class SingleRule {

    @Priority
    public int getPriority() {
        return 19900;
    }

    @Condition
    public boolean when(Map<String, Object> facts) {
        List<String> ruleSuite = (List<String>) facts.get("ruleSuite");
        return ruleSuite.contains("single");
    }

    @Action
    public void then(Map<String, Object> facts) {
        List<String> formatCodes = (List<String>) facts.get("formatCodes");
        Map<String, Object> attached = (Map<String, Object>) facts.get("attached");

        // 获取匹配规则
        Map<String, Object> singleRule = getSingleRule((String) facts.get("matcherName"));

        // 去重逻辑
        List<String> uniqueCodes = formatCodes.stream()
                .distinct()
                .collect(Collectors.toList());

        if (formatCodes.size() != uniqueCodes.size()) {
            facts.put("message", Arrays.asList("以下号码重复，已进行自动去重", String.join(",", getDuplicates(formatCodes))));
        }

        facts.put("formatCodes", uniqueCodes);

        // 正则过滤逻辑
        String regexPattern = (String) singleRule.get("regex") + attached.get("number");
        Pattern regex = Pattern.compile(regexPattern);

        List<String> errorCodes = new ArrayList<>();
        List<String> currentCodes = new ArrayList<>();

        for (String code : uniqueCodes) {
            if (!regex.matcher(code).matches()) {
                errorCodes.add(code);
            } else if (!Boolean.TRUE.equals(singleRule.get("hasZero")) || hasUniqueSortedItems(code)) {
                currentCodes.add(code);
            }
        }

        if (!errorCodes.isEmpty()) {
            facts.put("message", Arrays.asList("以下号码错误，已进行自动过滤", String.join(",", errorCodes)));
        }

        facts.put("formatCodes", currentCodes);
        facts.put("num", currentCodes.size());
    }

    private Map<String, Object> getSingleRule(String matcherName) {
        // 模拟从 SPECIAL_SINGLE 中获取对应规则。你可以根据实际逻辑替换。
        return new HashMap<>(); // 返回实际规则。
    }

    private boolean hasUniqueSortedItems(String code) {
        List<String> items = Arrays.stream(code.split(" ")).sorted().distinct().collect(Collectors.toList());
        return items.size() == code.split(" ").length;
    }

    private List<String> getDuplicates(List<String> list) {
        return list.stream()
                .collect(Collectors.groupingBy(e -> e, Collectors.counting()))
                .entrySet()
                .stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}