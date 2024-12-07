package com.xtree.lottery.rule.decision;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Rule(name = "Special Single Rule", description = "Processes special single rules for lottery")
public class SpecialSingleRule {

    private static final Map<String, RuleDefinition> RULE_DEFINITIONS = createRuleDefinitions();

    @Priority
    public int getPriority() {
        return 19900;
    }

    @Condition
    public boolean when(Facts facts) {
        String matcherName = facts.get("matcherName");
        return matcherName != null && RULE_DEFINITIONS.containsKey(matcherName);
    }

    @Action
    public void then(Facts facts) {
        String matcherName = facts.get("matcherName");
        String codes = facts.get("formatCodes");
        Map<String, List<String>> attached = facts.get("attached");
        int attachedNumber = Integer.getInteger(attached.get("number").get(0));
        List<String> flags = attached.get("number");

        RuleDefinition rule = RULE_DEFINITIONS.getOrDefault(matcherName, RULE_DEFINITIONS.get("default"));

        // Filter and deduplicate codes
        List<String> filteredCodes = rule.filter(codes);
        List<String> uniqueCodes = filteredCodes.stream().distinct().collect(Collectors.toList());

        if (filteredCodes.size() != uniqueCodes.size()) {
            System.out.println("以下号码重复，已进行自动去重: " +
                    filteredCodes.stream()
                            .collect(Collectors.groupingBy(c -> c, Collectors.counting()))
                            .entrySet().stream()
                            .filter(entry -> entry.getValue() > 1)
                            .map(Map.Entry::getKey)
                            .collect(Collectors.joining(", "))
            );
        }

        facts.put("formatCodes", uniqueCodes);
        facts.put("singleDesc", rule.getDesc());

        // Validate codes against regex
        List<String> errorCodes = new ArrayList<>();
        List<String> validCodes = new ArrayList<>();

        Pattern regexPattern = Pattern.compile("^" + rule.getRegex(attachedNumber) + "$");

        for (String code : uniqueCodes) {
            if (!regexPattern.matcher(code).matches()) {
                errorCodes.add(code);
            } else if (!rule.isHasZero() || Arrays.stream(code.split(" ")).distinct().count() == code.split(" ").length) {
                validCodes.add(code);
            } else {
                errorCodes.add(code);
            }
        }

        if (!errorCodes.isEmpty()) {
            System.out.println("以下号码错误，已进行自动过滤: " + String.join(", ", errorCodes));
        }

        facts.put("formatCodes", validCodes);

        // Group and deduplicate for "group" flag
        if (flags.contains("group")) {
            List<List<String>> groupedCodes = validCodes.stream()
                    .map(code -> Arrays.stream(code.split(rule.getSortSplit() == null ? " " : rule.getSortSplit()))
                            .sorted()
                            .collect(Collectors.toList()))
                    .distinct()
                    .collect(Collectors.toList());

            List<String> repeatCodes = groupedCodes.stream()
                    .filter(group -> group.stream().distinct().count() == 1)
                    .map(group -> String.join(" ", group))
                    .collect(Collectors.toList());

            List<String> finalCodes = groupedCodes.stream()
                    .filter(group -> !repeatCodes.contains(String.join(" ", group)))
                    .map(group -> String.join(rule.getSortSplit() == null ? " " : rule.getSortSplit(), group))
                    .collect(Collectors.toList());

            List<String> removedCodes = validCodes.stream()
                    .filter(code -> !finalCodes.contains(code))
                    .collect(Collectors.toList());

            if (!removedCodes.isEmpty()) {
                System.out.println("已经过滤以下号码: " + String.join(", ", removedCodes));
            }

            facts.put("formatCodes", finalCodes);
        }

        facts.put("num", validCodes.size());
    }

    private static Map<String, RuleDefinition> createRuleDefinitions() {
        Map<String, RuleDefinition> rules = new HashMap<>();

        rules.put("default", new RuleDefinition(
                Collections.emptyList(),
                "[0-9\\s]{$}",
                false,
                "每注号码之间请用一个空格【 】、逗号【，】或者分号【；】隔开",
                " "
        ));

        // Add additional rules here
        return rules;
    }

    private static class RuleDefinition {
        private List<String> matcherNames;
        private String regex;
        private boolean hasZero;
        private String desc;
        private String sortSplit;

        public RuleDefinition(List<String> matcherNames, String regex, boolean hasZero, String desc, String sortSplit) {
            this.matcherNames = matcherNames;
            this.regex = regex;
            this.hasZero = hasZero;
            this.desc = desc;
            this.sortSplit = sortSplit;
        }

        public List<String> filter(String codes) {
            return Arrays.stream(codes.split("[,;，；\\n\\s]+"))
                    .filter(item -> !item.trim().isEmpty())
                    .map(String::trim)
                    .collect(Collectors.toList());
        }

        public String getDesc() {
            return desc;
        }

        public boolean isHasZero() {
            return hasZero;
        }

        public String getRegex(int number) {
            return regex.replace("{$}", String.valueOf(number));
        }

        public String getSortSplit() {
            return sortSplit;
        }
    }
}
