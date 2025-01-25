package com.xtree.lottery.rule.recent.decision;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.List;
import java.util.Map;

@Rule(name = "Sum Calculation", description = "Calculates the sum of workCode if ruleSuite includes 'SUM'")
public class SumCalculationRule {

    @Priority
    public int getPriority() {
        return -79000;
    }

    @Condition
    public boolean when(Facts facts) {
        List<String> rules = facts.get("ruleSuiteRules");
        return rules != null && rules.contains("SUM");
    }

    @Action
    public void then(Facts facts) {
        try {
            List<Map<String, Object>> historyCodes = facts.get("historyCodes");
            Map<String, String> currentMethod = facts.get("currentMethod"); // 获取当前方法
            String methodName = (currentMethod.get("categoryName") + currentMethod.get("methodName")).trim();
            facts.put("title", "和值");

            if (historyCodes != null) {
                for (Map<String, Object> history : historyCodes) {
                    List<Integer> workCode = (List<Integer>) history.get("workCode");
                    if (workCode == null || workCode.isEmpty()) {
                        continue;
                    }

                    long unionWorkCode = workCode.stream().distinct().count();
                    int sum = workCode.stream().mapToInt(Integer::intValue).sum();

                    if (Double.isNaN(sum)) {
                        addForm(history, "--", "b_f8aa46");
                        continue;
                    }

                    if (methodName.contains("直选和值")) {
                        addForm(history, String.valueOf(sum), "b_f8aa46");
                        continue;
                    }

                    if (unionWorkCode > 1 || workCode.size() == 2) {
                        addForm(history, String.valueOf(sum), "b_f8aa46");
                    }
                }
            }
        } catch (Exception e) {
            CfLog.e("Error in SumCalculationRule: " + e.getMessage());
        }
    }

    private void addForm(Map<String, Object> history, String label, String className) {
        List<Map<String, String>> form = (List<Map<String, String>>) history.computeIfAbsent("form", key -> new java.util.ArrayList<>());
        form.add(Map.of("label", label, "className", className));
    }
}
