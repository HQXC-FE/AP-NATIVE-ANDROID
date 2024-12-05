package com.xtree.lottery.rule.ending;

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
        return 100;
    }

    @Condition
    public boolean when(Facts facts) {
        String lotteryType = facts.get("lotteryType");
        return "ssc".equals(lotteryType);
    }

    @Action
    public void then(Facts facts) {
        Map<String, Object> currentMethod = facts.get("currentMethod");
        Map<String, Object> mode = facts.get("mode");
        List<Object> formatCodes = facts.get("formatCodes");
        String prize = facts.get("prize") != null ? facts.get("prize").toString() : null;
        int times = facts.get("times");
        double money = facts.get("money");
        int num = facts.get("num");
        String sortSplit = facts.get("sortSplit") != null ? facts.get("sortSplit").toString() : "";

        // Betting submission object
        Map<String, Object> forBet = new HashMap<>();
        forBet.put("methodid", currentMethod.get("methodid"));

        // Generate codes for submission
        if (List.of("input", "box").contains(currentMethod.get("selectarea.type"))) {
            forBet.put("codes", formatCodes.stream()
                    .map(item -> String.join(sortSplit, (String) item))
                    .collect(Collectors.joining("&")));
        } else {
            forBet.put("codes", formatCodes.stream()
                    .map(item -> String.join("&", (String) item))
                    .collect(Collectors.joining("|")));
        }

        forBet.put("omodel", prize != null ? Integer.parseInt(prize) : 1);
        forBet.put("mode", mode.get("modeid"));
        forBet.put("times", times);
        forBet.put("poschoose", facts.get("poschoose"));
        forBet.put("menuid", currentMethod.get("menuid"));
        forBet.put("type", currentMethod.get("selectarea.originType") != null
                ? currentMethod.get("selectarea.originType")
                : currentMethod.get("selectarea.type"));
        forBet.put("nums", num);
        forBet.put("money", money);
        forBet.put("solo", facts.get("solo") != null ? facts.get("solo") : false);

        // Display object
        Map<String, Object> forDisplay = new HashMap<>();
        List<String> betCodes = formatCodes.stream()
                .map(item -> {
                    if (item instanceof List<?>) {
                        // 确保 `item` 是 List，并将其转换为字符串，同时使用指定分隔符
                        String codeSp = (String) currentMethod.get("code_sp");
                        codeSp = (codeSp != null && !codeSp.isEmpty()) ? codeSp : ",";
                        return ((List<?>) item).stream()
                                .map(Object::toString)
                                .collect(Collectors.joining(codeSp));
                    } else if (item instanceof String) {
                        // 如果是字符串，直接返回
                        return (String) item;
                    } else {
                        // 如果类型不符合预期，处理异常或返回默认值
                        return "";
                    }
                })
                .collect(Collectors.toList());
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

        // Generate codes for display
        if (List.of("input", "box").contains(currentMethod.get("selectarea.type"))) {
            forDisplay.put("codes", betCodes.stream()
                    .map(item -> String.join(sortSplit, item))
                    .collect(Collectors.joining((String) currentMethod.get("code_sp"))));
        } else {
            List<String> displayCodes = new ArrayList<>();
            String showStr = (String) currentMethod.get("show_str");
            List<String> showStrSplit = Arrays.asList(showStr.split(","));
            int codeIndex = 0;
            for (int i = 0; i < showStrSplit.size(); i++) {
                if ("X".equals(showStrSplit.get(i))) {
                    displayCodes.add(betCodes.get(codeIndex++));
                } else {
                    displayCodes.add(showStrSplit.get(i));
                }
            }
            String codeSp = currentMethod.get("code_sp") != null && !" ".equals(currentMethod.get("code_sp"))
                    ? (String) currentMethod.get("code_sp")
                    : ",";
            forDisplay.put("codes", String.join(codeSp, displayCodes));
        }

        // Translate codes
        List<String> translatedCodes = Arrays.stream(((String) forDisplay.get("codes")).split(","))
                .map(item -> translate(item))  // Replace `translate(item)` with your actual translation function
                .collect(Collectors.toList());
        forDisplay.put("codes", String.join(",", translatedCodes));

        // Add description to submission object
        forBet.put("desc", (String) forDisplay.get("methodName") + forDisplay.get("codes"));

        // Combine final result
        Map<String, Object> done = new HashMap<>();
        done.put("display", forDisplay);
        done.put("submit", forBet);
        done.put("message", facts.get("message"));

        facts.put("done", done);
    }

    private String translate(String item) {
        // Implement your translation logic here
        return item;
    }
}

