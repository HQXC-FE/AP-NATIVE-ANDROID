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

@Rule(name = "Straight Selection Span", description = "Calculates the span for direct selection if ruleSuite includes 'SPAN'")
public class StraightSelectionSpanRule {

    @Priority
    public int getPriority() {
        return -77000;
    }

    @Condition
    public boolean when(Facts facts) {
        List<String> rules = facts.get("ruleSuite");
        return rules != null && rules.contains("SPAN");
    }

    @Action
    public void then(Facts facts) {
        try {
            // 设置标题
            facts.put("title", "直选跨度");

            // 获取历史记录数据
            List<Map<String, Object>> historyCodes = facts.get("historyCodes");

            // 遍历历史记录并计算跨度
            for (Map<String, Object> history : historyCodes) {
                List<Integer> workCode = (List<Integer>) history.get("workCode");
                List<Map<String, Object>> form = (List<Map<String, Object>>) history.get("form");

                if (workCode != null && !workCode.isEmpty()) {
                    int min = workCode.stream().mapToInt(Integer::intValue).min().orElse(Integer.MAX_VALUE);
                    int max = workCode.stream().mapToInt(Integer::intValue).max().orElse(Integer.MIN_VALUE);
                    int span = max - min;

                    // 如果结果是 NaN 或无效值，用 "-" 替代
                    Map<String, Object> result = new HashMap<>();
                    result.put("label", (Double.isNaN(span) ? "-" : span));
                    result.put("className", "ball b_f8aa46"); // 对应 classNames.b_f8aa46
                    form.add(result);
                }
            }
        } catch (Exception e) {
            CfLog.e("Error in StraightSelectionSpanRule: " + e.getMessage());
        }
    }
}
