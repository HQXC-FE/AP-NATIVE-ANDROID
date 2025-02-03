package com.xtree.lottery.rule.recent.decision;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.List;
import java.util.Map;

@Rule(name = "PK10 Single and Double", description = "Check if PK10 numbers are large/small or single/double")
public class PK10SingleDoubleRule {

    @Priority
    public int getPriority() {
        return -67000;
    }

    @Condition
    public boolean when(Facts facts) {
        List<String> ruleSuite = facts.get("ruleSuite");
        return ruleSuite.contains("PK10_SINGLE_DOUBLE");
    }

    @Action
    public void then(Facts facts) {
        try {
            List<Map<String, Object>> historyCodes = facts.get("historyCodes");

            for (Map<String, Object> history : historyCodes) {
                List<Integer> workCode = (List<Integer>) history.get("workCode");
                List<Map<String, String>> form = (List<Map<String, String>>) history.get("form");

                for (Integer item : workCode) {
                    // 大小判斷
                    if (item > 5) {
                        form.add(Map.of("label", "大", "className", "#f95016"));
                    } else {
                        form.add(Map.of("label", "小", "className", "#476efe"));
                    }
                    // 單雙判斷
                    if (item % 2 == 0) {
                        form.add(Map.of("label", "双", "className", "#476efe"));
                    } else {
                        form.add(Map.of("label", "单", "className", "#f95016"));
                    }
                }
            }
        } catch (Exception e) {
            CfLog.e("Error in PK10SingleDoubleRule: " + e.getMessage());
        }
    }
}