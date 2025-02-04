package com.xtree.lottery.rule.recent.ending;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Rule(name = "Callback Wrapper", description = "Encapsulates callback logic")
public class CallbackWrapperRule {

    @Priority
    public int getPriority() {
        return -50000;
    }

    @Condition
    public boolean when(Facts facts) {
        return null == facts.get("done");
    }

    @Action
    public void then(Facts facts) {
        try {
            // 获取历史记录列表
            List<Map<String, Object>> historyCodes = facts.get("historyCodes");
            Map<String, Object> currentMethod = facts.get("currentMethod");
            String title = facts.get("title");

            if (historyCodes != null) {
                // 转换历史记录
                List<Map<String, Object>> history = historyCodes.stream()
                        .map(item -> {
                            HashMap<String, Object> historyItem = new HashMap<>();

                            historyItem.put("form", item.get("form"));
                            historyItem.put("issue", item.get("issue"));
                            historyItem.put("displayCode", item.get("displayCode"));

                            if (null != item.get("draw_time")) {
                                historyItem.put("draw_time", item.get("draw_time"));
                            } else {
                                historyItem.put("draw_time", "");
                            }

                            if (null != item.get("issueClass")) {
                                historyItem.put("issueClass", item.get("issueClass"));
                            } else {
                                historyItem.put("issueClass", "");
                            }

                            return historyItem;
                        })
                        .collect(Collectors.toList());

                // 封装结果
                Map<String, Object> done = Map.of(
                        "history", history,
                        "currentMethod", currentMethod,
                        "title", title
                );

                // 更新结果到 Facts
                facts.put("done", done);
            }
        } catch (Exception e) {
            CfLog.e("Error in CallbackWrapperRule: " + e.getMessage());
        }
    }
}
