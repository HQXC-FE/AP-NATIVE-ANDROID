package com.xtree.lottery.rule.betting.prepare;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Rule(name = "Format BOX Type", description = "Formats the codes when type is 'box' and lottery type is 'ssc'")
public class FormatBoxTypeRule {

    @Priority
    public int getPriority() {
        return -89800;
    }

    @Condition
    public boolean when(Facts facts) {
        String lotteryType = facts.get("lotteryType");
        Map<String, Object> currentMethod = facts.get("currentMethod");
        return "ssc".equals(lotteryType)
                && currentMethod.containsKey("selectarea")
                && "box".equals(((Map<?, ?>) currentMethod.get("selectarea")).get("type"));
    }

    @Action
    public void then(Facts facts) {
        try {
            Map<String, List<String>> bet = facts.get("bet");
            List<String> betCodes = bet.get("codes");
            List<List<String>> formatCodes = new ArrayList<>();

            for (String code : betCodes) {
                List<String> singleCode = new ArrayList<>();
                singleCode.add(code);
                formatCodes.add(singleCode);
            }

            facts.put("formatCodes", formatCodes);
        } catch (Exception e) {
            CfLog.e(e.getMessage());
        }
    }
}
