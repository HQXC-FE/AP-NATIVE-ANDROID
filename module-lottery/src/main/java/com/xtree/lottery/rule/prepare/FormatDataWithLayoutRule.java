package com.xtree.lottery.rule.prepare;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Rule(name = "Format Data with Layout", description = "Formats the codes based on the layout if lottery type is 'ssc'")
public class FormatDataWithLayoutRule {

    @Priority
    public int getPriority() {
        return -90000;
    }

    @Condition
    public boolean when(Facts facts) {
        String lotteryType = facts.get("lotteryType");
        Map<String, Object> currentMethod = facts.get("currentMethod");
        return "ssc".equals(lotteryType) && currentMethod.containsKey("selectarea")
                && currentMethod.get("selectarea") instanceof Map
                && ((Map<String, Object>) currentMethod.get("selectarea")).containsKey("layout");
    }

    @Action
    public void then(Facts facts) {
        Map<String, Object> currentMethod = facts.get("currentMethod");
        Map<String, Object> selectArea = (Map<String, Object>) currentMethod.get("selectarea");
        List<Map<String, String>> layout = (List<Map<String, String>>) selectArea.get("layout");
        Map<String, List<String>> bet = facts.get("bet");
        List<String> betCodes = bet.get("codes");


        int place;
        Map<Integer, String> formatCodes = new HashMap<>();

        for (int i = 0; i < layout.size(); i++) {
            Map<String, String> item = layout.get(i);
            Object placeObj = item.get("place");
            if (placeObj instanceof Integer) {
                place = (Integer) placeObj;
            } else if (placeObj instanceof String) {
                try {
                    place = Integer.parseInt((String) placeObj);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid place value: " + placeObj);
                }
            } else {
                throw new IllegalArgumentException("Unsupported type for place: " + placeObj.getClass());
            }
            formatCodes.put(place,  betCodes.get(i));
        }

        facts.put("formatCodes", formatCodes);
    }
}