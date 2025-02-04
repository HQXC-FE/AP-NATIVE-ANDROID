package com.xtree.lottery.rule.recent.match;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Rule(name = "No Matching Rule", description = "Stops processing if ruleSuite is null or empty")
public class NoMatchingRule {

    @Priority
    public int getPriority() {
        return -99800;
    }

    @Condition
    public boolean when(Facts facts) {
        // 检查 ruleSuite 是否为空
        List<String> ruleSuite = facts.get("ruleSuite");
        return ruleSuite == null || ruleSuite.isEmpty();
    }

    @Action
    public void then(Facts facts) {
        try {
            // 获取历史记录
            List<Map<String, Object>> historyCodes = facts.get("historyCodes");
            List<Map<String, Object>> formattedHistory = new ArrayList<>();

            if (historyCodes != null) {
                for (Map<String, Object> item : historyCodes) {
                    Map<String, Object> formattedItem = new HashMap<>();
                    formattedItem.put("issue", item.get("issue"));
                    List<String> splitCode = (List<String>) item.get("split_code");
                    List<Map<String, Object>> formattedCodes = new ArrayList<>();

                    if (splitCode != null) {
                        for (String code : splitCode) {
                            Map<String, Object> codeMap = new HashMap<>();
                            codeMap.put("codes", code);
                            codeMap.put("className", "active");
                            formattedCodes.add(codeMap);
                        }
                    }

                    formattedItem.put("codes", formattedCodes);
                    formattedItem.put("draw_time", item.get("draw_time"));
                    formattedItem.put("issueClass", item.getOrDefault("issueClass", ""));
                    formattedHistory.add(formattedItem);
                }
            }

            // 设置结果
            Map<String, Object> done = new HashMap<>();
            done.put("history", formattedHistory);
            done.put("currentMethod", facts.get("currentMethod"));
            done.put("title", "");
            facts.put("done", done);
        } catch (Exception e) {
            CfLog.e("Error in NoMatchingRule: " + e.getMessage());
        }
    }
}