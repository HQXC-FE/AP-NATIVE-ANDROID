package com.xtree.lottery.rule.betting.ending;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Rule(name = "SSC Final Merge Rule", description = "Final merge for SSC, creates output object")
public class SSCFinalMergeRule {

    @Priority
    public int getPriority() {
        return -100;
    }

    @Condition
    public boolean when(Facts facts) {
        String lotteryType = facts.get("lotteryType");
        return "ssc".equals(lotteryType);
    }

    @Action
    public void then(Facts facts) {
        try {
            Map<String, Object> currentMethod = facts.get("currentMethod");
            Map<String, Object> mode = facts.get("mode");
            List<Object> formatCodes = new ArrayList<>();
            Map<String, String> selectarea = (Map<String, String>) currentMethod.get("selectarea");
            String prize = facts.get("prize") != null ? facts.get("prize").toString() : null;
            int times = facts.get("times");
            double money = facts.get("money");
            int num = facts.get("num");
            String sortSplit = facts.get("sortSplit") != null ? facts.get("sortSplit").toString() : "";

            // Betting submission object
            Map<String, Object> forBet = new HashMap<>();
            forBet.put("methodid", currentMethod.get("methodid"));

            // Generate codes for submission
            if (facts.get("formatCodes") instanceof List) {
                if (!((List<Object>) facts.get("formatCodes")).isEmpty()) {
                    formatCodes = facts.get("formatCodes");
                    if (List.of("input", "box").contains(selectarea.get("type"))) {
                        if (formatCodes.get(0) instanceof List<?>) {
                            forBet.put("codes", formatCodes.stream()
                                    .map(item -> String.join(sortSplit, (List<String>) item))
                                    .collect(Collectors.joining("&")));
                        } else if (formatCodes.get(0) instanceof String) {
                            forBet.put("codes", formatCodes.stream()
                                    .map(item -> String.join(sortSplit, (String) item))
                                    .collect(Collectors.joining("&")));
                        }
                    } else {
                        if (formatCodes.get(0) instanceof List<?>) {
                            forBet.put("codes", formatCodes.stream()
                                    .map(item -> String.join("&", (List<String>) item))
                                    .collect(Collectors.joining("|")));
                        } else {
                            forBet.put("codes", formatCodes.stream()
                                    .filter(item -> item != null && !((String) item).isEmpty())
                                    .map(String::valueOf)
                                    .collect(Collectors.joining("&")));
                        }
                    }
                }
            }

            forBet.put("omodel", prize != null ? Integer.parseInt(prize) : 1);
            forBet.put("mode", mode.get("modeid"));
            forBet.put("times", times);
            forBet.put("poschoose", facts.get("poschoose"));
            forBet.put("menuid", currentMethod.get("menuid"));
            forBet.put("type", ((Map<?, ?>) currentMethod.get("selectarea")).get("originType") != null
                    ? ((Map<?, ?>) currentMethod.get("selectarea")).get("originType")
                    : ((Map<?, ?>) currentMethod.get("selectarea")).get("type"));
            forBet.put("nums", num);
            forBet.put("money", money);
            forBet.put("solo", facts.get("solo") != null ? facts.get("solo") : false);

            // Display object
            Map<String, Object> forDisplay = new HashMap<>();
            List<String> betCodes = new ArrayList<>();
            if (facts.get("formatCodes") instanceof List) {
                if (formatCodes.get(0) instanceof List<?>) {
                    betCodes = formatCodes.stream()
                            .map(item -> {
                                // 确保 `item` 是 List，并将其转换为字符串，同时使用指定分隔符
                                String codeSp = (String) currentMethod.get("code_sp");
                                codeSp = (codeSp != null && !codeSp.isEmpty()) ? codeSp : ",";
                                return ((List<?>) item).stream()
                                        .map(Object::toString)
                                        .collect(Collectors.joining(codeSp));
                            }).collect(Collectors.toList());
                } else if (formatCodes.get(0) instanceof String) {
                    for (Object item : formatCodes) {
                        if (!((String) item).isEmpty()) {
                            betCodes.add((String) item);
                        }
                    }
                }
            }

            forDisplay.put("prize", "");
            forDisplay.put("mode", mode.get("name"));
            forDisplay.put("rate", mode.get("rate"));
            forDisplay.put("times", times + "倍");
            forDisplay.put("money", money);
            forDisplay.put("num", num);
            forDisplay.put("methodName", "[" + currentMethod.get("cate_title") + "_" + currentMethod.get("desc") + "]");
            forDisplay.put("prize_group", currentMethod.get("prize_group"));
            forDisplay.put("prize_level", currentMethod.get("prize_level"));
            forDisplay.put("solo", facts.get("solo") != null ? facts.get("solo") : false);
            forDisplay.put("relationMethods", facts.get("relationMethods"));
            forDisplay.put("money_modes", currentMethod.get("money_modes"));
            forDisplay.put("minPrize", facts.get("minPrize"));
            forDisplay.put("singleDesc", facts.get("singleDesc"));

            // 处理不同类型的 codes 拼接逻辑
            String finalCodes = "";
            // Generate codes for display
            if (List.of("input", "box").contains(((Map<String, String>) currentMethod.get("selectarea")).get("type"))) {
                finalCodes = betCodes.stream()
                        .map(item -> String.join(sortSplit, item))
                        .collect(Collectors.joining((String) currentMethod.getOrDefault("code_sp", ",")));
            } else {
                // 如果类型不是 "input" 或 "box"，按照 show_str 和数组进行对应
                List<String> displayCodes = new ArrayList<>();
                String showStr = (String) currentMethod.get("show_str");
                List<String> showStrSplit = new ArrayList<>(Arrays.asList(showStr.split(",")));
                // 统计 showStr 中 X 的数量
                long xCount = showStrSplit.stream().filter(s -> "X".equals(s)).count();

                // 特殊情况：只有一个 X（表示：展示所有 betCodes，剩下的位置补 "-" 到固定长度）
                if (xCount == 1 && showStrSplit.size() != 1) {
                    int xIndex = showStrSplit.indexOf("X");

                    // 前部分保留原顺序，替换 X 为多个 betCodes
                    for (int i = 0; i < xIndex; i++) {
                        displayCodes.add(showStrSplit.get(i));
                    }

                    // 替换 X 为多个 betCodes（去除 , ;）
                    String endCode = String.join(" ", betCodes);

                    displayCodes.add((endCode).replace(",", " ").replace(";", ","));

                    for (int i = 0; i < 9; i++) {
                        displayCodes.add("-");
                    }
                    // 根据 code_sp 拼接最终结果
                    String codeSp = (String) currentMethod.getOrDefault("code_sp", ",");
                    finalCodes = String.join(codeSp.isBlank() ? "," : codeSp, displayCodes);
                } else {
                    if (xCount == 1) {
                        // 自动扩展 showStrSplit 如果 X 不够填入所有 betCodes
                        if (xCount < betCodes.size()) {
                            long extraNeeded = betCodes.size() - xCount;
                            for (int i = 0; i < extraNeeded; i++) {
                                showStrSplit.add("X");
                            }
                        }
                    }

                    // 正常情况：逐个处理 showStr 中的 X 和非 X
                    int codeIndex = 0;

                    for (String item : showStrSplit) {
                        if ("X".equals(item)) {
                            if (codeIndex < betCodes.size()) {
                                Object betCode = betCodes.get(codeIndex++);
                                if (betCode instanceof String && !((String) betCode).isEmpty()) {
                                    displayCodes.add(((String) betCode).replace(",", "").replace(";", ","));
                                } else {
                                    displayCodes.add("-");
                                }
                            } else {
                                displayCodes.add("-");
                            }
                        } else {
                            displayCodes.add(item);
                        }
                    }
                    // 根据 code_sp 拼接最终结果
                    String codeSp = (String) currentMethod.getOrDefault("code_sp", ",");
                    finalCodes = String.join(codeSp.isBlank() ? "," : codeSp, displayCodes);
                }
            }

            // 更新 forDisplay 的 codes 字段
            forDisplay.put("codes", finalCodes);

            // Add description to submission object
            forBet.put("desc", (String) forDisplay.get("methodName") + forDisplay.get("codes"));

            // Combine final result
            Map<String, Object> done = new HashMap<>();
            done.put("display", forDisplay);
            done.put("submit", forBet);
            done.put("message", facts.get("message"));

            facts.put("done", done);
        } catch (Exception e) {
            CfLog.e(e.getMessage());
        }
    }
}

