package com.xtree.lottery.rule.betting.decision;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.List;

@Rule(name = "MultiRule", description = "复式玩法规则")
public class MultiRule {

    @Priority
    public int getPriority() {
        return -20000;
    }

    @Condition
    public boolean when(Facts facts) {
        List<String> ruleSuite = facts.get("ruleSuite");
        return ruleSuite.contains("multi");
    }

    @Action
    public void then(Facts facts) {
        try {
            // 获取格式化后的代码集合
            List<List<String>> formatCodes = facts.get("formatCodes");

            // 检查 formatCodes 是否为空
            if (formatCodes == null || formatCodes.isEmpty()) {
                facts.put("num", 0); // 如果没有有效数据，则设置组合数为 0
                return;
            }

            int num = 1;

            // 遍历 Map 的每个条目，计算所有 List 的长度乘积
            for (List<String> codes : formatCodes) {
                num *= codes.size();
            }

            // 将计算结果存入 facts
            facts.put("num", num);
        } catch (Exception e) {
            CfLog.e(e.getMessage());
        }
    }
}
