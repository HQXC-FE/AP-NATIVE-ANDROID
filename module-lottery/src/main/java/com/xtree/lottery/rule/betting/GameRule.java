package com.xtree.lottery.rule.betting;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GameRule {
    public List<String> matcherNames;
    public String regex;
    public String filter;
    public String join;
    public boolean hasZero;
    public String description;
    private final Pattern DELIMITER_PATTERN = Pattern.compile("(,|;|，|；|\\n)");
    private final Pattern DELIMITER_PATTERN_2 = Pattern.compile("(,| |;|\\t|，|　|；|\\n)");



    public GameRule(List<String> matcherNames, String regex, String filter, String join, boolean hasZero, String description) {
        this.matcherNames = matcherNames;
        this.regex = regex;
        this.filter = filter;
        this.join = join;
        this.hasZero = hasZero;
        this.description = description;
    }

    public List<String> filter(String codes, String filter) {

        switch (filter) {
            case "rankFilter":
            case "choose5Filter":
                return Arrays.stream(DELIMITER_PATTERN.split(codes))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty() && !isDelimiter(s))
                        .collect(Collectors.toList());
            case "k3Filter":
                return Arrays.stream(DELIMITER_PATTERN_2.split(codes))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .map(item -> item.chars().mapToObj(c -> String.valueOf((char) c)).sorted().collect(Collectors.joining()))
                        .filter(item -> item.chars().distinct().count() == 3)
                        .collect(Collectors.toList());
            case "dif2Filter":
            case "sk2Filter":
                return Arrays.stream(DELIMITER_PATTERN_2.split(codes))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .map(item -> item.chars().mapToObj(c -> String.valueOf((char) c)).sorted().collect(Collectors.joining()))
                        .filter(item -> item.chars().distinct().count() == 2)
                        .collect(Collectors.toList());
            default:
                return Arrays.stream(DELIMITER_PATTERN_2.split(codes))
                        .map(String::trim)
                        .collect(Collectors.toList());
        }
    }

    private static boolean isDelimiter(String s) {
        return s.equals(",") || s.equals(";") || s.equals("，") ||
                s.equals("；") || s.equals("\n");
    }
}
