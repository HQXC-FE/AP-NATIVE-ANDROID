package com.xtree.lottery.rule.ending;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Rule(name = "SSC Final Merge", description = "SSC最终合并，形成输出对象")
public class SSCFinalMergeRule {

    @Priority
    public int getPriority() {
        return 100;
    }

    @Condition
    public boolean when(Map<String, Object> facts) {
        return "ssc".equals(facts.get("lotteryType"));
    }

    @Action
    public void then(Map<String, Object> facts) {
        Map<String, Object> forBet = new HashMap<>();
        List<String> betCodes = new ArrayList<>();

        Map<String, Object> currentMethod = (Map<String, Object>) facts.get("currentMethod");
        List<List<String>> formatCodes = (List<List<String>>) facts.get("formatCodes");

        forBet.put("methodid", currentMethod.get("methodid"));

        if (Arrays.asList("input", "box").contains(currentMethod.get("selectarea.type"))) {
            forBet.put("codes", formatCodes.stream()
                    .map(item -> String.join((String) facts.getOrDefault("sortSplit", "")))
                    .collect(Collectors.joining("&")));
        } else {
            forBet.put("codes", formatCodes.stream()
                    .map(item -> String.join("&", item))
                    .collect(Collectors.joining("|")));
        }

        forBet.put("omodel", Optional.ofNullable(facts.get("prize")).map(Object::toString).map(Integer::parseInt).orElse(1));
        forBet.put("mode", ((Map<String, Object>) facts.get("mode")).get("modeid"));
        forBet.put("times", facts.get("times"));
        forBet.put("poschoose", facts.getOrDefault("poschoose", null));
        forBet.put("menuid", currentMethod.get("menuid"));
        forBet.put("type", Optional.ofNullable((String) currentMethod.get("selectarea.originType"))
                .orElse((String) currentMethod.get("selectarea.type")));
        forBet.put("nums", facts.get("num"));
        forBet.put("money", facts.get("money"));
        forBet.put("solo", Optional.ofNullable((Boolean) facts.get("solo")).orElse(false));

        List<String> betCodeDisplay = new ArrayList<>();
        formatCodes.forEach(item -> {
            if (item instanceof List && !"X".equals(currentMethod.get("show_str"))) {
                betCodeDisplay.add(String.join((String) currentMethod.get("code_sp"), item));
            }
//            if (item instanceof String || "X".equals(currentMethod.get("show_str"))) {
//                betCodeDisplay.add((String) item);
//            }
        });

        Map<String, Object> forDisplay = new HashMap<>();
        forDisplay.put("prize", "");

        if (facts.get("prize") != null) {
            String targetLabel = ((List<Map<String, Object>>) currentMethod.get("prize_group")).stream()
                    .filter(group -> Objects.equals(group.get("value"), Integer.parseInt(facts.get("prize").toString())))
                    .map(group -> (String) group.get("label"))
                    .findFirst().orElse("");

            if (targetLabel.contains("~")) {
                forDisplay.put("prize", "模式:" + targetLabel.split(" ")[1]);
            } else {
                forDisplay.put("prize", "模式:" + targetLabel.split("-")[0].split(" ")[1]);
            }
        }

        forDisplay.put("mode", ((Map<String, Object>) facts.get("mode")).get("name"));
        forDisplay.put("rate", ((Map<String, Object>) facts.get("mode")).get("rate"));
        forDisplay.put("times", facts.get("times") + "倍");
        forDisplay.put("money", facts.get("money"));
        forDisplay.put("num", facts.get("num"));
        forDisplay.put("methodName", "[" + currentMethod.get("cate_title") + "_" + currentMethod.get("desc") + "]");
        forDisplay.put("codes", betCodeDisplay);
        forDisplay.put("prize_group", currentMethod.get("prize_group"));
        forDisplay.put("prize_level", currentMethod.get("prize_level"));
        forDisplay.put("solo", Optional.ofNullable((Boolean) facts.get("solo")).orElse(false));
        forDisplay.put("relationMethods", facts.getOrDefault("relationMethods", null));

        if (Boolean.TRUE.equals(facts.get("CONFIG.SUPPORT_BET_BONUS"))) {
            forDisplay.put("currentPrize", facts.get("currentPrize"));
            forDisplay.put("currentBonus", facts.get("currentBonus"));
        }

        forDisplay.put("money_modes", currentMethod.get("money_modes"));
        forDisplay.put("minPrize", facts.get("minPrize"));
        forDisplay.put("singleDesc", facts.getOrDefault("singleDesc", null));

        if (Arrays.asList("input", "box").contains(currentMethod.get("selectarea.type"))) {
            forDisplay.put("codes", String.join((String) currentMethod.get("code_sp"), betCodeDisplay));
        } else {
            List<String> codes = new ArrayList<>();
            int codeIndex = 0;

            for (String item : ((String) currentMethod.get("show_str")).split(",")) {
                if ("X".equals(item)) {
                    codes.add(betCodeDisplay.get(codeIndex++));
                } else {
                    codes.add(item);
                }
            }

            String code_sp = Optional.ofNullable((String) currentMethod.get("code_sp")).filter(sp -> !sp.isEmpty()).orElse(",");
            forDisplay.put("codes", String.join(code_sp, codes));
        }

        forDisplay.put("codes", Arrays.stream(((String) forDisplay.get("codes")).split(","))
                .map(code -> code)
                .collect(Collectors.joining(",")));

        forBet.put("desc", (String)forDisplay.get("methodName") + forDisplay.get("codes"));

        Map<String, Object> done = new HashMap<>();
        done.put("display", forDisplay);
        done.put("submit", forBet);
        done.put("message", facts.get("message"));

        facts.put("done", done);
    }
}
