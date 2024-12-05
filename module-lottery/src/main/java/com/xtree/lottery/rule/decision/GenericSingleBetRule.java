package com.xtree.lottery.rule.decision;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Rule(name = "Generic Single Bet", description = "Handles single bets with de-duplication, filtering, and formatting")
public class GenericSingleBetRule {

    @Priority
    public int getPriority() {
        return 19900;
    }

    @Condition
    public boolean when(Facts facts) {
        List<String> ruleSuite = facts.get("ruleSuite");
        return ruleSuite.contains("single");
    }

    @Action
    public void then(Facts facts) {
        List<String> formatCodes = facts.get("formatCodes");
        String matcherName = facts.get("matcherName");
        Map<String, Object> attached = facts.get("attached");
        List<Map<String, Object>> specialSingle = facts.get("specialSingle");
        List<String> messages = facts.get("messages");

        // 找到对应的 singleRule
        Map<String, Object> singleRule = specialSingle.stream()
                .filter(rule -> ((List<String>) rule.get("matcherNames")).contains(matcherName))
                .findFirst()
                .orElse(specialSingle.stream()
                        .filter(rule -> "default".equals(rule.get("type")))
                        .findFirst()
                        .orElse(null));

        if (singleRule == null) {
            messages.add("未找到匹配的 singleRule 配置");
            return;
        }

        // 单式去重
        List<String> realCode = applyFilter(formatCodes, singleRule);
        List<String> uniqueCode = realCode.stream().distinct().toList();

        // 重复号码提示
        if (realCode.size() != uniqueCode.size()) {
            messages.add("以下号码重复，已进行自动去重");
            messages.add(getDuplicateCodes(realCode));
        }

        facts.put("singleDesc", singleRule.get("desc"));
        facts.put("formatCodes", uniqueCode);

        // 单式正则过滤
        String regexPattern = ((String) singleRule.get("regex")).replace("$", attached.get("number").toString());
        Pattern regex = Pattern.compile("^" + regexPattern + "$");
        List<String> errorCodes = new ArrayList<>();
        List<String> currentCodes = new ArrayList<>();

        for (String item : uniqueCode) {
            if (!regex.matcher(item).matches()) {
                errorCodes.add(item);
                continue;
            }
            if (!(Boolean) singleRule.get("hasZero")) {
                currentCodes.add(item);
            } else {
                if (hasUniqueElements(item.split(" "))) {
                    currentCodes.add(item);
                } else {
                    errorCodes.add(item);
                }
            }
        }

        if (!errorCodes.isEmpty()) {
            messages.add("以下号码错误，已进行自动过滤");
            messages.add(String.join(",", errorCodes));
        }

        facts.put("formatCodes", currentCodes);

        // 组选去重
        if (((List<String>) attached.get("flag")).contains("group")) {
            handleGroupCodes(facts, singleRule);
        }

        facts.put("num", currentCodes.size());
    }

    private List<String> applyFilter(List<String> codes, Map<String, Object> singleRule) {
        // 过滤逻辑，根据 singleRule 定义处理 codes
        // 示例：直接返回 codes，不进行复杂处理
        return codes;
    }

    private String getDuplicateCodes(List<String> codes) {
        Map<String, Long> grouped = codes.stream().collect(Collectors.groupingBy(code -> code, Collectors.counting()));
        return grouped.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.joining(","));
    }

    private boolean hasUniqueElements(String[] elements) {
        return Arrays.stream(elements).distinct().count() == elements.length;
    }

    private void handleGroupCodes(Facts facts, Map<String, Object> singleRule) {
        List<String> formatCodes = facts.get("formatCodes");
        String sortSplit = singleRule.get("sortSplit") != null ? (String) singleRule.get("sortSplit") : "";
        List<List<String>> sortedCodes = formatCodes.stream()
                .map(code -> Arrays.stream(code.split(sortSplit)).sorted().toList())
                .distinct()
                .toList();

        List<List<String>> repeatCodes = new ArrayList<>();
        if (!((List<String>) facts.get("attached.flag")).contains("banco")) {
            repeatCodes = sortedCodes.stream()
                    .filter(code -> new HashSet<>(code).size() == 1)
                    .toList();
        }

        List<List<String>> finalRepeatCodes = repeatCodes;
        List<List<String>> finalCodes = sortedCodes.stream()
                .filter(code -> !finalRepeatCodes.contains(code))
                .toList();

        List<String> removedCodes = formatCodes.stream()
                .filter(code -> !finalCodes.contains(Arrays.asList(code.split(sortSplit))))
                .toList();

        if (!removedCodes.isEmpty()) {
            List<String> messages = facts.get("messages");
            messages.add("已经过滤以下号码");
            messages.add(String.join(",", removedCodes));
        }

        facts.put("formatCodes", finalCodes.stream()
                .map(code -> String.join(sortSplit, code))
                .toList());
    }
}