package com.xtree.lottery.rule.recent.decision;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Rule(name = "Five in One Rule", description = "Processes five-in-one rule for history codes")
public class FiveInOneRule {

    @Priority
    public int getPriority() {
        return -70000;
    }

    @Condition
    public boolean when(Facts facts) {
        List<String> ruleSuite = facts.get("ruleSuite");
        return ruleSuite != null && ruleSuite.contains("5IN1");
    }

    @Action
    public void then(Facts facts) {
        try {
            List<Map<String, Object>> historyCodes = facts.get("historyCodes");
            for (Map<String, Object> history : historyCodes) {
                String code = (String) history.get("code");
                List<Map<String, String>> form = (List<Map<String, String>>) history.get("form");
                List<Integer> workCode = (List<Integer>) history.get("workCode");

                int unionWorkCode = new HashSet<>(workCode).size();
                long groupBySize = workCode.stream()
                        .collect(Collectors.groupingBy(k -> k, Collectors.counting()))
                        .values().stream()
                        .filter(size -> size > 1)
                        .count();

                if (unionWorkCode == 1 && code != null && !code.isEmpty()) {
                    form.add(createFormItem("组选1", "classNames.c_ff0000"));
                }
                if (unionWorkCode == 2 && groupBySize == 1) {
                    form.add(createFormItem("组选5", "classNames.c_ff33ff"));
                }
                if (unionWorkCode == 2 && groupBySize == 2) {
                    form.add(createFormItem("组选10", "classNames.c_006dfe"));
                }
                if (unionWorkCode == 3 && groupBySize == 1) {
                    form.add(createFormItem("组选20", "classNames.c_cc8b1e"));
                }
                if (unionWorkCode == 3 && groupBySize == 2) {
                    form.add(createFormItem("组选30", "classNames.c_229e6d"));
                }
                if (unionWorkCode == 4 && groupBySize == 1) {
                    form.add(createFormItem("组选60", "classNames.c_1291bb"));
                }
                if (unionWorkCode == 5) {
                    form.add(createFormItem("组选120", "classNames.c_7c73ff"));
                }
            }
        } catch (Exception e) {
            CfLog.e("Error in FiveInOneRule: " + e.getMessage());
        }
    }

    private Map<String, String> createFormItem(String label, String className) {
        Map<String, String> formItem = new HashMap<>();
        formItem.put("label", label);
        formItem.put("className", className);
        return formItem;
    }
}
