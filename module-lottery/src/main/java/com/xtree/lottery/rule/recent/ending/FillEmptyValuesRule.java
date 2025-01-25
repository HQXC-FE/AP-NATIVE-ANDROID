package com.xtree.lottery.rule.recent.ending;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.List;
import java.util.Map;

@Rule(name = "Fill Empty Values", description = "Fills empty form values in history codes")
public class FillEmptyValuesRule {

    @Priority
    public int getPriority() {
        return -60000;
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

            if (historyCodes != null) {
                for (Map<String, Object> history : historyCodes) {
                    List<Map<String, Object>> form = (List<Map<String, Object>>) history.get("form");

                    // 检查 form 是否为空
                    if (form != null && form.isEmpty()) {
                        form.add(Map.of("label", "-", "className", "empty"));
                    }
                }
            }

            // 更新 facts 中的历史记录
            facts.put("historyCodes", historyCodes);

        } catch (Exception e) {
            CfLog.e("Error in FillEmptyValuesRule: " + e.getMessage());
        }
    }
}
