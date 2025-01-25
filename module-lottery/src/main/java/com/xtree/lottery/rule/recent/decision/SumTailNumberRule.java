package com.xtree.lottery.rule.recent.decision;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.List;
import java.util.Map;

@Rule(name = "Sum Tail Number", description = "Calculates the sum tail number if ruleSuite includes 'SUM_TAIL_NUM'")
public class SumTailNumberRule {

    @Priority
    public int getPriority() {
        return -76000;
    }

    @Condition
    public boolean when(Facts facts) {
        // 检查是否包含规则 "SUM_TAIL_NUM"
        List<String> rules = facts.get("ruleSuite.rules");
        return rules != null && rules.contains("SUM_TAIL_NUM");
    }

    @Action
    public void then(Facts facts) {
        try {
            // 获取历史记录和 className 映射
            List<Map<String, Object>> historyCodes = facts.get("historyCodes");

            if (historyCodes != null) {
                // 设置标题
                facts.put("title", "和值尾数");

                // 遍历历史记录
                for (Map<String, Object> history : historyCodes) {
                    // 获取 workCode 并计算和值
                    List<Integer> workCode = (List<Integer>) history.get("workCode");
                    if (workCode != null) {
                        int sum = workCode.stream().mapToInt(Integer::intValue).sum();
                        int tailNum = sum % 10;

                        // 添加到 form
                        List<Map<String, String>> form = (List<Map<String, String>>) history.get("form");
                        if (form != null) {
                            form.add(Map.of(
                                    "label", (tailNum >= 0 ? String.valueOf(tailNum) : "-"),
                                    "className", "ball b_f8aa46"
                            ));
                        }
                    }
                }
            }
        } catch (Exception e) {
            CfLog.e("Error in SumTailNumberRule: " + e.getMessage());
        }
    }
}

