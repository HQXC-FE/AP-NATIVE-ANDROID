package com.xtree.lottery.rule.decision;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
            Object rawFormatCodes = facts.get("formatCodes");
            String matcherName = facts.get("matcherName");
            Map<String, Object> attached = facts.get("attached");
            String number = (String) attached.get("number");
            List<String> flagList = (List<String>) attached.get("flags");
            List<String> message = new ArrayList<>();

            // 标准化 formatCodes 到 List<String>
            List<String> formatCodes = normalizeFormatCodes(rawFormatCodes);

            SpecialSingle specialSingle = SpecialSingle.getByName(matcherName);

            // 单式去重
            List<String> realCode = filterCodes(formatCodes, specialSingle);
            List<String> uniqueCode = new ArrayList<>(new HashSet<>(realCode));

            if (realCode.size() != uniqueCode.size()) {
                message.add("以下号码重复，已进行自动去重");
                message.add(realCode.stream()
                        .filter(code -> Collections.frequency(realCode, code) > 1)
                        .distinct()
                        .collect(Collectors.joining(",")));
            }

            facts.put("formatCodes", uniqueCode);
            facts.put("singleDesc", specialSingle.name());

            // 正则过滤
            List<String> errorCodes = new ArrayList<>();
            List<String> currentCodes = new ArrayList<>();
            Pattern regex = Pattern.compile(specialSingle.getRegex().replace("$", number));

            for (String code : uniqueCode) {
                if (!regex.matcher(code).matches()) {
                    errorCodes.add(code);
                } else if (specialSingle.hasZero() && isValidWithZero(code, specialSingle.getSortSplit())) {
                    currentCodes.add(code);
                } else {
                    currentCodes.add(code);
                }
            }

            if (!errorCodes.isEmpty()) {
                message.add("以下号码错误，已进行自动过滤");
                message.add(String.join(",", errorCodes));
            }

            facts.put("formatCodes", currentCodes);

            // 组选去重
            if (flagList != null && flagList.contains("group")) {
                handleGroupCodes(currentCodes, specialSingle, message, facts);
            }

            facts.put("num", currentCodes.size());
            facts.put("message", message);
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
            return Arrays.stream(codes.split("[,;\\s]+"))
                    .filter(code -> !code.trim().isEmpty())
                    .map(String::trim)
                    .collect(Collectors.toList());
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

    private List<String> filterCodes(List<String> codes, SpecialSingle specialSingle) {
        return codes.stream()
                .map(String::trim)
                .filter(code -> !code.isEmpty())
                .collect(Collectors.toList());
    }

    private boolean isValidWithZero(String code, String split) {
        List<String> parts = Arrays.asList(code.split(split));
        return parts.size() == new HashSet<>(parts).size();
    }

    private void handleGroupCodes(List<String> currentCodes, SpecialSingle specialSingle, List<String> message, Facts facts) {
        String sortSplit = specialSingle.getSortSplit();
        List<String> sortedCodes = currentCodes.stream()
                .map(code -> Arrays.stream(code.split(sortSplit)).sorted().collect(Collectors.joining(sortSplit)))
                .distinct()
                .collect(Collectors.toList());

        List<String> removedCodes = currentCodes.stream()
                .filter(code -> !sortedCodes.contains(code))
                .collect(Collectors.toList());

        if (!removedCodes.isEmpty()) {
            message.add("已经过滤以下号码");
            message.add(String.join(",", removedCodes));
        }

        facts.put("formatCodes", sortedCodes);
    }

    public enum SpecialSingle {
        RANK("rank", "(0[1-9]\\s|10\\s){$}(0[1-9]|10){1}", true, " "),
        CHOOSE5("choose5", "(0[1-9]\\s|10\\s|11\\s){$}(0[1-9]|10|11){1}", true, " "),
        K3("k3", "(?!\\d*?(\\d)\\d*?\\1)[1-6\\s]{$}", false, ""),
        DIF2("dif2", "(?!\\d*?(\\d)\\d*?\\1)[1-6\\s]{$}", false, ""),
        SK2("sk2", "[1-6\\s]{$}", false, ""),
        DEFAULT("default", "[0-9\\s]{$}", false, "");

        private final String name;
        private final String regex;
        private final boolean hasZero;
        private final String sortSplit;

        SpecialSingle(String name, String regex, boolean hasZero, String sortSplit) {
            this.name = name;
            this.regex = regex;
            this.hasZero = hasZero;
            this.sortSplit = sortSplit;
        }

        public String getRegex() {
            return regex;
        }

        public boolean hasZero() {
            return hasZero;
        }

        public String getSortSplit() {
            return sortSplit;
        }

        public static SpecialSingle getByName(String name) {
            return Arrays.stream(values())
                    .filter(single -> single.name.equals(name))
                    .findFirst()
                    .orElse(DEFAULT);
        }
    }
}