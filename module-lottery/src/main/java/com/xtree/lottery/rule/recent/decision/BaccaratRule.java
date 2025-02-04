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

@Rule(name = "Baccarat Banker Player Tie Rule", description = "Calculates Baccarat Banker/Player/Tie if ruleSuite includes 'BACCARAT_1'")
public class BaccaratRule {

    @Priority
    public int getPriority() {
        return -74000; // 对应 JavaScript 中的优先级
    }

    @Condition
    public boolean when(Facts facts) {
        List<String> rules = facts.get("ruleSuite");
        return rules != null && rules.contains("BACCARAT_1");
    }

    @Action
    public void then(Facts facts) {
        try {
            // 设置标题
            facts.put("title", "形态");

            // 获取历史记录数据
            List<Map<String, Object>> historyCodes = facts.get("historyCodes");

            // 遍历历史记录，计算百家乐庄闲和结果
            for (Map<String, Object> history : historyCodes) {
                String code = (String) history.get("code");
                List<Integer> workCode = (List<Integer>) history.get("workCode");
                List<Map<String, Object>> form = (List<Map<String, Object>>) history.get("form");

                if (code == null || code.isEmpty()) {
                    form.add(createFormEntry("--", "#ff0000"));
                    continue;
                }

                // 计算庄闲和结果
                int bankerScore = (workCode.get(0) + workCode.get(1)) % 10;
                int playerScore = (workCode.get(2) + workCode.get(3)) % 10;
                int result = bankerScore - playerScore;

                if (result > 0) {
                    form.add(createFormEntry("庄", "#ff0000"));
                } else if (result == 0) {
                    form.add(createFormEntry("和", "#008000"));
                } else {
                    form.add(createFormEntry("闲", "#4c8bda"));
                }
            }
        } catch (Exception e) {
            CfLog.e("Error in BaccaratRule: " + e.getMessage());
        }
    }

    private Map<String, Object> createFormEntry(String label, String className) {
        Map<String, Object> formEntry = new HashMap<>();
        formEntry.put("label", label);
        formEntry.put("className", className);
        return formEntry;
    }
}
