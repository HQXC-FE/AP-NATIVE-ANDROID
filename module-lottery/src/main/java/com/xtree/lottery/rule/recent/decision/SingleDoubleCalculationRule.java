package com.xtree.lottery.rule.recent.decision;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.List;
import java.util.Map;

@Rule(name = "Single Double Calculation", description = "Calculates single/double and big/small values if ruleSuite includes 'SINGLE_DOUBLE'")
public class SingleDoubleCalculationRule {

    @Priority
    public int getPriority() {
        return -78000;
    }

    @Condition
    public boolean when(Facts facts) {
        List<String> rules = facts.get("ruleSuite");
        return rules != null && rules.contains("SINGLE_DOUBLE");
    }

    @Action
    public void then(Facts facts) {
        try {
            List<Map<String, Object>> historyCodes = facts.get("historyCodes");
            Map<String, String> currentMethod = facts.get("currentMethod");
            String methodName = (currentMethod.get("categoryName") + currentMethod.get("methodName")).trim();

            if ("大小单双总和".equals(methodName) || methodName.contains("和值")) {
                facts.put("title", "和值");
            } else if ("大小单双前二".equals(methodName)) {
                facts.put("title", "万位 千位");
            } else if ("大小单双后二".equals(methodName)) {
                facts.put("title", "十位 个位");
            } else if ("大小单双前三".equals(methodName)) {
                facts.put("title", "万位 千位 百位");
            } else if ("大小单双后三".equals(methodName)) {
                facts.put("title", "百位 十位 个位");
            } else if ("大小单双中三".equals(methodName)) {
                facts.put("title", "千位 百位 十位");
            } else if (methodName.contains("大小个数") || methodName.contains("单双个数")) {
                facts.put("title", "形态");
            }

            if (historyCodes != null) {
                for (Map<String, Object> history : historyCodes) {
                    List<Integer> workCode = (List<Integer>) history.get("workCode");
                    if (workCode == null || workCode.isEmpty()) {
                        addForm(history, "--", "#ff0000");
                        continue;
                    }

                    int sum = workCode.stream().mapToInt(Integer::intValue).sum();

                    if ("大小单双总和".equals(methodName)) {
                        handleTotalSum(history, sum);
                    } else if (methodName.contains("和值")) {
                        handleSumValues(history, sum, methodName);
                    } else {
                        handleIndividualValues(history, workCode, methodName);
                    }

                    if (methodName.contains("大小个数")) {
                        handleSizeCounts(history, workCode);
                    }

                    if (methodName.contains("单双个数")) {
                        handleSingleDoubleCounts(history, workCode);
                    }
                }
            }
        } catch (Exception e) {
            CfLog.e("Error in SingleDoubleCalculationRule: " + e.getMessage());
        }
    }

    private void handleTotalSum(Map<String, Object> history, int sum) {
        if (sum > 22) {
            addForm(history, "大", "#f95016");
        } else {
            addForm(history, "小", "#476efe");
        }

        if (sum % 2 == 1) {
            addForm(history, "单", "#f95016");
        } else {
            addForm(history, "双", "#476efe");
        }
    }

    private void handleSumValues(Map<String, Object> history, int sum, String methodName) {
        if ("大小单双五星和值".equals(methodName)) {
            if (sum > 22) {
                addForm(history, "大", "#f95016");
            } else {
                addForm(history, "小", "#476efe");
            }
        } else {
            if (sum > 13) {
                addForm(history, "大", "#f95016");
            } else {
                addForm(history, "小", "#476efe");
            }
        }

        if (sum % 2 == 1) {
            addForm(history, "单", "#f95016");
        } else {
            addForm(history, "双", "#476efe");
        }
    }

    private void handleIndividualValues(Map<String, Object> history, List<Integer> workCode, String methodName) {
        for (int item : workCode) {
            if (item > 4) {
                addForm(history, "大", "#f95016");
            } else {
                addForm(history, "小", "#476efe");
            }

            if (item % 2 == 1) {
                addForm(history, "单", "#f95016");
            } else {
                addForm(history, "双", "#476efe");
            }
        }
    }

    private void handleSizeCounts(Map<String, Object> history, List<Integer> workCode) {
        int bigCount = 0;
        int smallCount = 0;

        for (int item : workCode) {
            if (item > 4) {
                bigCount++;
            } else {
                smallCount++;
            }
        }

        if (bigCount == 0) {
            addForm(history, "全小", "#476efe");
        } else if (smallCount == 0) {
            addForm(history, "全大", "#f95016");
        } else {
            addForm(history, bigCount + "大", "#f95016");
            addForm(history, smallCount + "小", "#476efe");
        }
    }

    private void handleSingleDoubleCounts(Map<String, Object> history, List<Integer> workCode) {
        int singleCount = 0;
        int doubleCount = 0;

        for (int item : workCode) {
            if (item % 2 == 1) {
                singleCount++;
            } else {
                doubleCount++;
            }
        }

        if (singleCount == 0) {
            addForm(history, "全双", "#476efe");
        } else if (doubleCount == 0) {
            addForm(history, "全单", "#f95016");
        } else {
            addForm(history, singleCount + "单", "#f95016");
            addForm(history, doubleCount + "双", "#476efe");
        }
    }

    private void addForm(Map<String, Object> history, String label, String className) {
        List<Map<String, String>> form = (List<Map<String, String>>) history.computeIfAbsent("form", key -> new java.util.ArrayList<>());
        form.add(Map.of("label", label, "className", className));
    }
}
