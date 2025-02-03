package com.xtree.lottery.rule.recent.decision;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Rule(name = "Dragon Tiger Battle Rule", description = "Calculates Dragon Tiger Battle shape if ruleSuite includes 'DRAGON_TIGER_PK'")
public class DragonTigerBattleRule {

    @Priority
    public int getPriority() {
        return -75000; // 对应 JavaScript 中的优先级
    }

    @Condition
    public boolean when(Facts facts) {
        List<String> ruleSuite = facts.get("ruleSuite");
        return ruleSuite.contains("DRAGON_TIGER_PK");
    }

    @Action
    public void then(Facts facts) {
        try {
            // 设置标题
            facts.put("title", "形态");

            // 获取当前方法的名称
            Map<String, String> currentMethod = facts.get("currentMethod");
            String methodName = (currentMethod.get("categoryName") + currentMethod.get("methodName")).trim();

            // 获取历史记录数据
            List<Map<String, Object>> historyCodes = facts.get("historyCodes");

            // 遍历历史记录，计算龙虎斗形态
            for (Map<String, Object> history : historyCodes) {
                String code = (String) history.get("code");
                List<Integer> workCode = (List<Integer>) history.get("workCode");
                List<Map<String, Object>> form = (List<Map<String, Object>>) history.get("form");

                if (code == null || code.isEmpty()) {
                    form.add(createFormEntry("--", "#ff0000"));
                    continue;
                }

                int diff = workCode.get(0) - workCode.get(1);

                if (diff == 0) {
                    form.add(createFormEntry("-", "#008000"));
                } else if (diff > 0) {
                    String label = "龙虎斗玄麟斗".equals(methodName) ? "玄" : "龙";
                    form.add(createFormEntry(label, "#008000"));
                } else {
                    String label = "龙虎斗玄麟斗".equals(methodName) ? "麟" : "虎";
                    form.add(createFormEntry(label, "#4c8bda"));
                }
            }
        } catch (Exception e) {
            CfLog.e("Error in DragonTigerBattleRule: " + e.getMessage());
        }
    }

    private Map<String, Object> createFormEntry(String label, String className) {
        Map<String, Object> formEntry = new HashMap<>();
        formEntry.put("label", label);
        formEntry.put("className", className);
        return formEntry;
    }
}
