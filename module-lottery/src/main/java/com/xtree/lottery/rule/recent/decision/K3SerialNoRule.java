package com.xtree.lottery.rule.recent.decision;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.List;
import java.util.Map;

@Rule(name = "K3 Serial No", description = "Check if history codes contain serial numbers")
public class K3SerialNoRule {

    @Priority
    public int getPriority() {
        return -68000;
    }

    @Condition
    public boolean when(Facts facts) {
        List<String> ruleSuite = facts.get("ruleSuite");
        return ruleSuite.contains("K3_SERIAL_NO");
    }

    @Action
    public void then(Facts facts) {
        try {
            List<Map<String, Object>> historyCodes = facts.get("historyCodes");

            for (Map<String, Object> history : historyCodes) {
                List<Integer> workCode = (List<Integer>) history.get("workCode");
                List<Map<String, String>> form = (List<Map<String, String>>) history.get("form");

                if (workCode.get(0) + 1 == workCode.get(1) && workCode.get(1) + 1 == workCode.get(2)) {
                    form.add(Map.of("label", "三连号", "className", "c_7c73ff"));
                } else {
                    form.add(Map.of("label", "-", "className", "c_ff0000"));
                }
            }
        } catch (Exception e) {
            CfLog.e("Error in K3SerialNoRule: " + e.getMessage());
        }
    }
}