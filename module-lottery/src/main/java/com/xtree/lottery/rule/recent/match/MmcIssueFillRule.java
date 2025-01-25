package com.xtree.lottery.rule.recent.match;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Rule(name = "MMC Issue Fill Rule", description = "Fills issue number and class for MMC lottery")
public class MmcIssueFillRule {

    @Priority
    public int getPriority() {
        return -99850;
    }

    @Condition
    public boolean when(Facts facts) {
        // 检查当前方法的 flag 是否为 'mmc'
        Map<String, Object> currentMethod = facts.get("currentMethod");
        return currentMethod != null && "mmc".equals(currentMethod.get("flag"));
    }

    @Action
    public void then(Facts facts) {
        try {
            // 获取历史记录
            List<Map<String, Object>> historyCodes = facts.get("historyCodes");
            List<Map<String, Object>> filterHistory = new ArrayList<>();

            if (historyCodes != null) {
                // 为秒秒彩填充期号
                for (Map<String, Object> history : historyCodes) {
                    if (history != null) {
                        history.put("issue", "------");
                        history.put("issueClass", "mmc");
                        filterHistory.add(history);
                    }
                }
            }

            // 通知规则引擎继续执行下一个规则
            facts.put("history", filterHistory);

        } catch (Exception e) {
            CfLog.e("Error in MmcIssueFillRule: " + e.getMessage());
        }
    }
}