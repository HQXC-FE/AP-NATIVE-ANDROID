package com.xtree.lottery.rule.recent.match;

import com.xtree.base.utils.CfLog;
import com.xtree.lottery.rule.recent.RecentMatchers;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.List;
import java.util.Map;

@Rule(name = "Generate Rule Suite", description = "Generates the rule suite if it is not already present.")
public class GenerateRuleSuiteRule {

    @Priority
    public int getPriority() {
        return -100000;
    }

    @Condition
    public boolean when(Facts facts) {
        // 判斷是否已經有 ruleSuite
        return !facts.asMap().containsKey("ruleSuite");
    }

    @Action
    public void then(Facts facts) {
        try {
            // 獲取 currentMethod 對象並處理其屬性
            Map<String, Object> currentMethod = facts.get("currentMethod");
            String cateName = (String) currentMethod.get("cateName");
            String methodName = (String) currentMethod.get("methodName");
            String alias = (String) currentMethod.get("flag");

            // 拼接並生成 methodName
            String methodFullName = (cateName + methodName).trim();

            // 使用 Mathchers.method2RuleSuite 方法生成 ruleSuite
            List<String> ruleSuite = RecentMatchers.method2RuleSuite(methodFullName, alias);

            // 將生成的 ruleSuite 放入 Facts
            facts.put("ruleSuite", ruleSuite);
        } catch (Exception e) {
            // 捕獲並記錄異常
            CfLog.e("Error in GenerateRuleSuiteRule: " + e.getMessage());
        }
    }
}
