package com.xtree.lottery.rule.prepare;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Rule(name = "Format Data with Layout", description = "Formats the codes based on the layout if lottery type is 'ssc'")
public class FormatDataWithLayoutRule {

    @Priority
    public int getPriority() {
        return 90000;
    }

    @Condition
    public boolean when(Facts facts) {
        String lotteryType = facts.get("lotteryType");
        Map<String, Object> currentMethod = facts.get("currentMethod");
        return "ssc".equals(lotteryType) && currentMethod.containsKey("selectarea")
                && currentMethod.get("selectarea") instanceof Map
                && ((Map<?, ?>) currentMethod.get("selectarea")).containsKey("layout");
    }

    @Action
    public void then(Facts facts) {
        Map<String, Object> currentMethod = facts.get("currentMethod");
        List<Map<String, Object>> layout = (List<Map<String, Object>>) ((Map<?, ?>) currentMethod.get("selectarea")).get("layout");
        List<List<String>> betCodes = (List<List<String>>) facts.get("betCodes");

        Map<String, List<String>> formatCodes = facts.get("formatCodes");

        for (int index = 0; index < layout.size(); index++) {
            Map<String, Object> item = layout.get(index);
            String place = (String) item.get("place");
            List<String> codesAtPlace = formatCodes.getOrDefault(place, new ArrayList<>());
            codesAtPlace.addAll(betCodes.get(index));
            formatCodes.put(place, codesAtPlace);
        }

        facts.put("formatCodes", formatCodes);
    }
}
