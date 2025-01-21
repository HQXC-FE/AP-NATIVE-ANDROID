package com.xtree.lottery.rule.betting.after;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Rule(name = "Lottery Position Selection Rule", description = "Handle optional position selection for SSC lottery")
public class PositionChosenRule {

    @Priority
    public int getPriority() {
        return -8845;
    }

    @Condition
    public boolean when(Facts facts) {
        String lotteryType = facts.get("lotteryType");
        Object currentMethod = facts.get("currentMethod");

        // Check if currentMethod.selectarea.selPosition exists
        return "ssc".equals(lotteryType) &&
                currentMethod instanceof Map &&
                ((Map<?, ?>) currentMethod).containsKey("selectarea") &&
                ((Map<?, ?>) ((Map<?, ?>) currentMethod).get("selectarea")).get("selPosition") != null;
    }

    @Action
    public void then(Facts facts) {
        try {
            Map<String, List<Boolean>> bet = facts.get("bet");
            List<Boolean> posChooseList = bet.get("poschoose");
            List<Integer> selectedPositions = new ArrayList<>();

            // Collect indices of true values from posChooseList
            if (posChooseList != null) {
                for (int i = 0; i < posChooseList.size(); i++) {
                    if (Boolean.TRUE.equals(posChooseList.get(i))) {
                        selectedPositions.add(i + 1); // Index + 1 as per the original logic
                    }
                }
            }

            // Convert selected positions to a comma-separated string
            String posChoose = String.join(",", selectedPositions.stream()
                    .map(String::valueOf)
                    .toList());

            // Update the facts
            facts.put("poschoose", posChoose);
        } catch (Exception e) {
            CfLog.e(e.getMessage());
        }
    }
}