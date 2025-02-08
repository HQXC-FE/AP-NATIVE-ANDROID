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
                                    .map(item -> String.join("&", (String) item))
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
                List<Object> finalFormatCodes = formatCodes;
                betCodes = formatCodes.stream()
                        .map(item -> {
                            if (item instanceof List<?>) {
                                // 确保 `item` 是 List，并将其转换为字符串，同时使用指定分隔符
                                String codeSp = (String) currentMethod.get("code_sp");
                                codeSp = (codeSp != null && !codeSp.isEmpty()) ? codeSp : ",";
                                return ((List<?>) item).stream()
                                        .map(Object::toString)
                                        .collect(Collectors.joining(codeSp));
                            } else if (item instanceof String) {
                                // 因为show_str的显示，所以如果是字符串应该把里面全部加入进去
                                String allCodes = "";
                                for (Object itemString : finalFormatCodes) {
                                    allCodes += allCodes.isEmpty() ? itemString : ";" + itemString;
                                }
                                return allCodes;
                            } else {
                                // 如果类型不符合预期，处理异常或返回默认值
                                return "";
                            }
                        })
                        .collect(Collectors.toList());
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
                List<String> showStrSplit = Arrays.asList(showStr.split(","));
                int codeIndex = 0;

                for (String item : showStrSplit) {
                    if ("X".equals(item)) {
                        if (codeIndex < betCodes.size()) {
                            Object betCode = betCodes.get(codeIndex++);
                            if (betCode instanceof String && !((String) betCode).isEmpty()) {
                                // 如果是 String，直接添加
                                displayCodes.add(((String) betCode).replace(",", "").replace(";", ","));
                            } else {
                                displayCodes.add("-");
                            }
                        } else {
                            displayCodes.add("-");
                        }
                    } else {
                        // 非 "X" 字符直接添加到 displayCodes
                        displayCodes.add(item);
                    }
                }

                // 根据 code_sp 拼接最终结果
                String codeSp = (String) currentMethod.getOrDefault("code_sp", ",");
                finalCodes = String.join(codeSp.isBlank() ? "," : codeSp, displayCodes);
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

