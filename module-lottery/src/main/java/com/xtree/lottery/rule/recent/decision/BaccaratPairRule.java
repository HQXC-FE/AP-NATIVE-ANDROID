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

@Rule(name = "Baccarat Pair Rule", description = "Calculates Baccarat pairs for banker and player if ruleSuite includes 'BACCARAT_2'")
public class BaccaratPairRule {

    @Priority
    public int getPriority() {
        return -73000; // 对应 JavaScript 中的优先级
    }

    @Condition
    public boolean when(Facts facts) {
        List<String> ruleSuite = facts.get("ruleSuite");
        return ruleSuite.contains("BACCARAT_2");
    }

    @Action
    public void then(Facts facts) {
        try {
            // 设置标题
            facts.put("title", "形态");

            // 获取历史记录数据
            List<Map<String, Object>> historyCodes = facts.get("historyCodes");

            // 遍历历史记录，计算百家乐对子结果
            for (Map<String, Object> history : historyCodes) {
                String code = (String) history.get("code");
                List<Integer> workCode = (List<Integer>) history.get("workCode");
                List<Map<String, Object>> form = (List<Map<String, Object>>) history.get("form");

                if (code == null || code.isEmpty()) {
                    form.add(createFormEntry("--", "c_ff0000"));
                    continue;
                }

                // 判断庄对
                if (workCode.get(0).equals(workCode.get(1))) {
                    form.add(createFormEntry("庄", "#ff0000"));
                }

                // 判断闲对
                if (workCode.get(2).equals(workCode.get(3))) {
                    form.add(createFormEntry("闲", "ball b_4c8bda"));
                }
            }
        } catch (Exception e) {
            CfLog.e("Error in BaccaratPairRule: " + e.getMessage());
        }
    }

    private Map<String, Object> createFormEntry(String label, String className) {
        Map<String, Object> formEntry = new HashMap<>();
        formEntry.put("label", label);
        formEntry.put("className", className);
        return formEntry;
    }
}
