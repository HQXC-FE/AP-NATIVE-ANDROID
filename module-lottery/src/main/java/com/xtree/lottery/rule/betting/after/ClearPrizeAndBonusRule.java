package com.xtree.lottery.rule.betting.after;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

@Rule(name = "ClearPrizeAndBonusRule", description = "当没有当前奖金模式时清除奖金和奖励")
public class ClearPrizeAndBonusRule {

    @Priority
    public int getPriority() {
        return -8600;
    }

    @Condition
    public boolean when(Facts facts) {
        // 检查 currentPrizeModes 是否为空或不存在
        return facts.get("currentPrizeModes") == null;
    }

    @Action
    public void then(Facts facts) {
        try {
            // 清除当前奖金和奖励
            facts.put("currentPrize", "");
            facts.put("currentBonus", "");
        } catch (Exception e) {
            CfLog.e(e.getMessage());
        }
    }
}
