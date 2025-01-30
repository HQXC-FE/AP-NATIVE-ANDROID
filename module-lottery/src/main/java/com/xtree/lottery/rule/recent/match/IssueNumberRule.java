package com.xtree.lottery.rule.recent.match;

import com.xtree.base.utils.CfLog;
import com.xtree.lottery.rule.betting.Matchers;
import com.xtree.lottery.rule.recent.IssueRules;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.List;
import java.util.Map;

@Rule(name = "Issue Number Truncate and Highlight", description = "Truncates issue numbers and applies highlighting based on lottery alias")
public class IssueNumberRule {

    @Priority
    public int getPriority() {
        return -99900;
    }

    @Condition
    public boolean when(Facts facts) {
        // 从 Facts 中获取 currentMethod 并判断 flag
        Map<String, Object> currentMethod = facts.get("currentMethod");
        return currentMethod != null && !"mmc".equals(currentMethod.get("flag"));
    }

    @Action
    public void then(Facts facts) {
        try {
            // 获取当前方法和历史期号列表
            Map<String, Object> currentMethod = facts.get("currentMethod");
            List<Map<String, Object>> historyCodes = facts.get("historyCodes");

            if (currentMethod == null || historyCodes == null) {
                return;
            }

            String alias = (String) currentMethod.get("flag");

            // 定义时时彩相关别名
            List<String> sscAlias = Matchers.sscAlias; // 需从 Facts 中获取时时彩别名列表
            List<String> additionalAliases = List.of("3djnd", "ydl10", "jssm");

            // 遍历历史期号并截取 issue 字段
            for (Map<String, Object> history : historyCodes) {
                if (history != null && history.containsKey("issue")) {
                    String issue = (String) history.get("issue");
                    if (issue != null && (sscAlias.contains(alias) || additionalAliases.contains(alias))) {
                        history.put("issue", issue.substring(4)); // 截取前4位
                    }
                }
            }

            // 期号高亮处理
            List<Map<String, Object>> updatedHistoryCodes = IssueRules.remarkIssue(alias, historyCodes);

            // 更新 Facts 中的历史期号列表
            facts.put("historyCodes", updatedHistoryCodes);

        } catch (Exception e) {
            CfLog.e("Error in IssueNumberRule: " + e.getMessage());
        }
    }
}