package com.xtree.lottery.rule.recent.decision;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Rule(name = "Baccarat King Rule", description = "Checks for baccarat king condition and processes history codes")
public class BaccaratKingRule {

    @Priority
    public int getPriority() {
        return -71000;
    }

    @Condition
    public boolean when(Facts facts) {
        List<String> ruleSuite = facts.get("ruleSuite");
        return ruleSuite != null && ruleSuite.contains("BACCARAT_4");
    }

    @Action
    public void then(Facts facts) {
        try {
            List<Map<String, Object>> historyCodes = facts.get("historyCodes");
            if (historyCodes == null) return;

            for (Map<String, Object> history : historyCodes) {
                List<Integer> workCode = (List<Integer>) history.get("workCode");
                List<Map<String, Object>> form = new ArrayList<>();

                if (List.of(8, 9).contains((workCode.get(0) + workCode.get(1)) % 10)) {
                    form.add(Map.of("label", "庄", "className", "#ff0000"));
                }
                if (List.of(8, 9).contains((workCode.get(2) + workCode.get(3)) % 10)) {
                    form.add(Map.of("label", "闲", "className", "#4c8bda"));
                }

                history.put("form", form);
            }
        } catch (Exception e) {
            CfLog.e("Error in BaccaratKingRule: " + e.getMessage());
        }
    }
}
