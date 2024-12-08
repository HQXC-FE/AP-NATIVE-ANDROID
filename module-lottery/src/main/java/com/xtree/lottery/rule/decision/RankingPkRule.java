package com.xtree.lottery.rule.decision;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.List;
import java.util.Map;

@Rule(name = "RankingPkRule", description = "对决竞速规则")
public class RankingPkRule {

    @Priority
    public int getPriority() {
        return -19200;
    }

    @Condition
    public boolean when(Facts facts) {
        // 检查是否包含 "ranking-pk"
        List<String> ruleSuite = facts.get("ruleSuite");
        return ruleSuite != null && ruleSuite.contains("ranking-pk");
    }

    @Action
    public void then(Facts facts) {
        // 获取 formatCodes 数据
        List<List<String>> formatCodes = facts.get("formatCodes");

        // 初始化 num 为 0
        int num = 0;

        if (formatCodes != null) {
            // 检查每个位置是否包含 2 个元素
            for (List<String> item : formatCodes) {
                if (item.size() == 2) {
                    num++;
                }
            }
        }

        // 更新结果到 facts
        facts.put("num", num);
    }
}
