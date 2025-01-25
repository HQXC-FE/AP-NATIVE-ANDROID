package com.xtree.lottery.rule.recent.decision;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Rule(name = "Baccarat Leopard Rule", description = "Checks for baccarat leopard condition and processes history codes")
public class BaccaratLeopardRule {

    @Priority
    public int getPriority() {
        return -72000;
    }

    @Condition
    public boolean when(Facts facts) {
        List<String> ruleSuite = facts.get("ruleSuite");
        return ruleSuite != null && ruleSuite.contains("BACCARAT_3");
    }

    @Action
    public void then(Facts facts) {
        try {
            List<Map<String, Object>> historyCodes = facts.get("historyCodes");
            if (historyCodes == null) return;

            for (Map<String, Object> history : historyCodes) {
                List<Integer> workCode = (List<Integer>) history.get("workCode");
                List<Map<String, Object>> form = new ArrayList<>();

                if (new HashSet<>(workCode.subList(0, 3)).size() == 1) {
                    form.add(Map.of("label", "庄", "className", "ball b_ff0000"));
                }
                if (new HashSet<>(workCode.subList(2, 5)).size() == 1) {
                    form.add(Map.of("label", "闲", "className", "ball b_4c8bda"));
                }

                history.put("form", form);
            }
        } catch (Exception e) {
            CfLog.e("Error in BaccaratLeopardRule: " + e.getMessage());
        }
    }
}

