package com.xtree.lottery.ui.rule.after;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;

import java.util.Map;

@Rule(name = "ClearPrizeAndBonusRule", description = "当没有当前奖金模式时清除奖金和奖励")
public class ClearPrizeAndBonusRule {

    @Priority
    public int getPriority() {
        return 8600;
    }

    @Condition
    public boolean when(Map<String, Object> facts) {
        // 检查 currentPrizeModes 是否为空或不存在
        return facts.get("currentPrizeModes") == null;
    }

    @Action
    public void then(Map<String, Object> facts) {
        // 清除当前奖金和奖励
        facts.put("currentPrize", null);
        facts.put("currentBonus", null);
    }
}
