package com.xtree.lottery.rule.betting.after;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.List;
import java.util.Map;

@Rule(name = "Set Funding Mode", description = "Set the funding mode based on lottery type")
public class SetFundingModeRule {

    @Priority
    public int getPriority() {
        return -8849;
    }

    @Condition
    public boolean when(Facts facts) {
        String lotteryType = facts.get("lotteryType");
        Object currentMethod = facts.get("currentMethod");
        Boolean mode = facts.get("mode");

        // Ensure `money_modes` is present and has length > 0
        List<Map<String, Object>> modes = currentMethod instanceof Map
                ? (List<Map<String, Object>>) ((Map<?, ?>) currentMethod).get("money_modes")
                : null;

        return "ssc".equals(lotteryType) && mode == null && modes != null && !modes.isEmpty();
    }

    @Action
    public void then(Facts facts) {
        try {
            Map<String, Object> bet = facts.get("bet");
            Object currentMethod = facts.get("currentMethod");
            List<Map<String, Object>> modes = (List<Map<String, Object>>) ((Map<?, ?>) currentMethod).get("money_modes");

            // Find mode by modeid
            Map<String, Object> betMode = (Map<String, Object>) bet.get("mode");
            Map<String, Object> selectedMode = modes.stream()
                    .filter(mode -> mode.get("modeid").equals(betMode.get("modeid")))
                    .findFirst()
                    .orElse(null);

            facts.put("mode", selectedMode);
        } catch (Exception e) {
            CfLog.e(e.getMessage());
        }
    }
}
