package com.xtree.lottery.rule.betting.after;

import com.xtree.base.utils.CfLog;

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
import java.util.stream.Collectors;

@Rule(name = "Calculate SSC Profit Prize Modes", description = "Calculate the actual prize group values for SSC lottery")
public class CalculateSSCProfitPrizeModesRule {

    @Priority
    public int getPriority() {
        return -8800;
    }

    @Condition
    public boolean when(Facts facts) {
        String lotteryType = facts.get("lotteryType");
        Integer num = facts.get("num");
        return "ssc".equals(lotteryType) && num != null;
    }

    @Action
    public void then(Facts facts) {
        try {
            Map<String, Object> currentMethod = facts.get("currentMethod");
            List<Map<String, Object>> prizeGroup = (List<Map<String, Object>>) currentMethod.get("prize_group");
            List<Map<String, Object>> prizeLevel = (List<Map<String, Object>>) currentMethod.get("prize_level");

            List<Map<String, Object>> prizeModes = new ArrayList<>();

            // Case 1: Prize group is empty, use prize levels as range
            if (prizeGroup == null || prizeGroup.isEmpty()) {
                List<Double> values = prizeLevel.stream()
                        .flatMap(level -> level.values().stream().map(val -> Double.valueOf(val.toString())))
                        .sorted()
                        .collect(Collectors.toList());

                Map<String, Object> mode = new HashMap<>();
                mode.put("option", 1);
                mode.put("value", values);
                prizeModes.add(mode);
            }

            // Case 2: Prize group exists but is not a range
            if (prizeGroup != null && !prizeGroup.isEmpty() &&
                    !prizeGroup.get(0).get("label").toString().contains("~")) {

                prizeModes = prizeGroup.stream()
                        .map(item -> {
                            String[] parts = item.get("label").toString().split(" ")[1].split("-");
                            Map<String, Object> mode = new HashMap<>();
                            mode.put("option", prizeGroup.indexOf(item) + 1);
                            mode.put("value", Collections.singletonList(Double.valueOf(parts[0])));
                            return mode;
                        })
                        .collect(Collectors.toList());
            }

            // Case 3: Prize group exists and is a range
            if (prizeGroup != null && !prizeGroup.isEmpty() &&
                    prizeGroup.get(0).get("label").toString().contains("~")) {

                prizeModes = prizeGroup.stream()
                        .map(item -> {
                            String[] parts = item.get("label").toString().split(" ")[1].split("~");
                            List<Double> values = Arrays.stream(parts)
                                    .map(Double::valueOf)
                                    .sorted()
                                    .collect(Collectors.toList());
                            Map<String, Object> mode = new HashMap<>();
                            mode.put("option", prizeGroup.indexOf(item) + 1);
                            mode.put("value", values);
                            return mode;
                        })
                        .collect(Collectors.toList());
            }

            // Save prize modes back to facts
            facts.put("currentPrizeModes", prizeModes);
        } catch (Exception e) {
            CfLog.e(e.getMessage());
        }
    }
}

