package com.xtree.lottery.rule.recent.ending;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

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
        // 条件恒为 true
        return true;
    }

    @Action
    public void then(Facts facts) {
        try {
            // 获取历史记录列表
            List<Map<String, Object>> historyCodes = facts.get("historyCodes");
            String currentMethod = facts.get("currentMethod");
            String title = facts.get("title");

            if (historyCodes != null) {
                // 转换历史记录
                List<Map<String, Object>> history = historyCodes.stream()
                        .map(item -> Map.of(
                                "form", item.get("form"),
                                "issue", item.get("issue"),
                                "codes", item.get("displayCode"),
                                "draw_time", item.get("draw_time"),
                                "issueClass", item.getOrDefault("issueClass", "")
                        ))
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
