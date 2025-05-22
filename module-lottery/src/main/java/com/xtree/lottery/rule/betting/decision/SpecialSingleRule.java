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
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
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
            List<String> uniqueCode = new ArrayList<>(new LinkedHashSet<>(realCode)); // 用 LinkedHashSet 保留順序

            if (realCode.size() != uniqueCode.size()) {
                List<String> message = facts.get("message");
                message.add("以下号码重复，已进行自动去重");
                message.add(realCode.stream()
                        .collect(Collectors.groupingBy(code -> code, Collectors.counting()))
                        .entrySet().stream()
                        .filter(entry -> entry.getValue() > 1)
                        .map(Map.Entry::getKey)
                        .collect(Collectors.joining(",")));
                facts.put("message", message);
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
                List<String> message = facts.get("message");
                message.add("以下号码错误，已进行自动过滤");
                message.add(String.join(",", errorCodes));
                facts.put("message", message);
            }

            facts.put("formatCodes", currentCodes);

            // 组选去重
            List<String> attachedFlags = (List<String>) attached.get("flags");
            if (attachedFlags != null && attachedFlags.contains("group")) {
                String sortSplit = singleRule.join;

                // 对号码进行排序和去重
                List<List<String>> sortedCodes = currentCodes.stream()
                        .map(code -> {
                            String[] parts;
                            // 如果不包含分隔符，就不拆了，直接当一整段处理
                            if (!code.contains(" ") && !code.contains(",")) {
                                parts = new String[] { code };
                            } else {
                                parts = code.split(sortSplit); // 使用正则切分空格或逗号
                            }
                            return Arrays.stream(parts)
                                    .filter(s -> !s.isEmpty())
                                    .sorted()
                                    .collect(Collectors.toList());
                        })
                        .distinct()
                        .sorted((list1, list2) -> {
                            String s1 = String.join(sortSplit, list1);
                            String s2 = String.join(sortSplit, list2);
                            return s1.compareTo(s2);
                        })
                        .collect(Collectors.toList());


                List<List<String>> repeatCodes = new ArrayList<>();
                if (!attachedFlags.contains("banco")) {
                    for (List<String> code : sortedCodes) {
                        // 如果所有号码都相同，加入重复列表
                        if (new HashSet<>(code).size() == 1) {
                            repeatCodes.add(code);
                        }
                    }
                }

                // 计算最终的有效号码
                List<String> finalCodes = sortedCodes.stream()
                        .filter(code -> !repeatCodes.contains(code))
                        .map(code -> code.stream().map(String::valueOf).collect(Collectors.joining(" ")))
                        .collect(Collectors.toList());

                // 找出被移除的号码
                List<String> removedCodes = currentCodes.stream()
                        .filter(code -> !finalCodes.contains(code))
                        .collect(Collectors.toList());

                if (!removedCodes.isEmpty()) {
                    List<String> message = facts.get("message");
                    message.add("已经过滤以下号码");
                    message.add(String.join(",", removedCodes));
                    facts.put("message", message);
                }

                // 更新 formatCodes
                facts.put("formatCodes", finalCodes);
            }

            facts.put("num", ((List<String>) facts.get("formatCodes")).size());
        } catch (Exception e) {
            CfLog.e("Error in SpecialSingleRule: " + e.getMessage());
        }
    }

    /**
     * 统一格式化 formatCodes 确保返回 List<String>
     */
    private List<String> normalizeFormatCodes(Object rawFormatCodes) {
        if (rawFormatCodes instanceof String) {
            return Collections.singletonList(((String) rawFormatCodes).trim());
        } else if (rawFormatCodes instanceof List) {
            List<?> codes = (List<?>) rawFormatCodes;
            if (!codes.isEmpty() && codes.get(0) instanceof List) {
                return ((List<List<String>>) codes).stream()
                        .flatMap(List::stream)
                        .map(String::trim)
                        .collect(Collectors.toList());
            } else {
                return codes.stream()
                        .map(String.class::cast)
                        .map(String::trim)
                        .collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }
}