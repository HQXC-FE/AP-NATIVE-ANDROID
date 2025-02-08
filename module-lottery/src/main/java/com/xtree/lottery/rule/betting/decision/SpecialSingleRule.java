package com.xtree.lottery.rule.betting.decision;

import com.xtree.base.utils.CfLog;
import com.xtree.lottery.rule.SpecialSingle;
import com.xtree.lottery.rule.betting.GameRule;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Rule(name = "Common Single Rule", description = "Handle single codes for lottery")
public class SpecialSingleRule {

    @Priority
    public int getPriority() {
        return -19900;
    }

    @Condition
    public boolean when(Facts facts) {
        List<String> ruleSuite = facts.get("ruleSuite");
        return ruleSuite.contains("single");
    }

    @Action
    public void then(Facts facts) {
        try {
            List<String> formatCodes = normalizeFormatCodes(facts.get("formatCodes"));
            String matcherName = facts.get("matcherName");
            Map<String, GameRule> specialSingle = SpecialSingle.getSingleMethod();
            Map<String, Object> attached = facts.get("attached");

            GameRule singleRule = specialSingle.values().stream()
                    .filter(rule -> rule.matcherNames.contains(matcherName))
                    .findFirst()
                    .orElse(specialSingle.get("default"));

            //去重
            List<String> realCode = singleRule.filter(String.join(",", formatCodes), singleRule.filter);
            List<String> uniqueCode = new ArrayList<>(new HashSet<>(realCode));
            List<String> message = facts.get("message");

            if (realCode.size() != uniqueCode.size()) {
                message.add("以下号码重复，已进行自动去重");
                message.add(realCode.stream()
                        .collect(Collectors.groupingBy(code -> code, Collectors.counting()))
                        .entrySet().stream()
                        .filter(entry -> entry.getValue() > 1)
                        .map(Map.Entry::getKey)
                        .collect(Collectors.joining(",")));
            }

            facts.put("formatCodes", uniqueCode);
            facts.put("singleDesc", singleRule.description);

            //正则过滤
            List<String> errorCodes = new ArrayList<>();
            List<String> currentCodes = new ArrayList<>();

            String regex = singleRule.regex.replace("$", (String) attached.get("number"));
            Pattern pattern = Pattern.compile("^" + regex + "$");

            for (String item : uniqueCode) {
                if (!pattern.matcher(item).matches()) {
                    errorCodes.add(item);
                } else if (!singleRule.hasZero ||
                        Arrays.stream(item.split(" ")).distinct().count() == item.split(" ").length) {
                    currentCodes.add(item);
                } else {
                    errorCodes.add(item);
                }
            }

            if (!errorCodes.isEmpty()) {
                message.add("以下号码错误，已进行自动过滤");
                message.add(String.join(",", errorCodes));
            }

            facts.put("formatCodes", currentCodes);

            // 组选去重
            List<String> attachedFlags = (List<String>) attached.get("flag");
            if (attachedFlags != null && attachedFlags.contains("group")) {
                String sortSplit = singleRule.join;

                List<List<String>> sortedCodes = currentCodes.stream()
                        .map(code -> Arrays.asList(code.split(sortSplit)))
                        .map(list -> list.stream().sorted().collect(Collectors.toList()))
                        .distinct()
                        .collect(Collectors.toList());

                List<List<String>> repeatCodes = new ArrayList<>();
                if (!attachedFlags.contains("banco")) {
                    for (List<String> code : sortedCodes) {
                        if (new HashSet<>(code).size() == 1) {
                            repeatCodes.add(code);
                        }
                    }
                }

                List<List<String>> finalCodes = sortedCodes.stream()
                        .filter(code -> !repeatCodes.contains(code))
                        .collect(Collectors.toList());

                List<String> removedCodes = currentCodes.stream()
                        .filter(code -> !finalCodes.contains(Arrays.asList(code.split(sortSplit))))
                        .collect(Collectors.toList());

                if (!removedCodes.isEmpty()) {
                    message.add("已经过滤以下号码");
                    message.add(String.join(",", removedCodes));
                }

                facts.put("formatCodes", finalCodes.stream()
                        .map(list -> String.join(sortSplit, list))
                        .collect(Collectors.toList()));
            }

            facts.put("num", ((List<String>) facts.get("formatCodes")).size());
        } catch (Exception e) {
            CfLog.e(e.getMessage());
        }
    }

    /**
     * 将 formatCodes 统一解析为 List<String>
     */
    private List<String> normalizeFormatCodes(Object rawFormatCodes) {
        if (rawFormatCodes instanceof String) {
            String codes = (String) rawFormatCodes;
            return new ArrayList<String>() {
                {
                    add(codes);
                }
            };
        } else if (rawFormatCodes instanceof List) {
            List<?> codes = (List<?>) rawFormatCodes;
            if (!codes.isEmpty() && codes.get(0) instanceof List) {
                // 处理 List<List<String>>
                return ((List<List<String>>) codes).stream()
                        .flatMap(List::stream)
                        .map(String::trim)
                        .collect(Collectors.toList());
            } else {
                // 处理 List<String>
                return codes.stream()
                        .map(String.class::cast) // 显式类型转换
                        .map(String::trim) // 调用 String 的 trim 方法
                        .collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }
}