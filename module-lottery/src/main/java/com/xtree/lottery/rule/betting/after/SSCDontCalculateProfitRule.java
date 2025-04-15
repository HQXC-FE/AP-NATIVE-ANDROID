package com.xtree.lottery.rule.betting.after;

import com.xtree.base.utils.CfLog;
import com.xtree.lottery.rule.betting.Matchers;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.List;
import java.util.Map;

@Rule(name = "SSCDontCalculateProfitRule", description = "时时彩不计算盈利的玩法规则")
public class SSCDontCalculateProfitRule {

    @Priority
    public int getPriority() {
        return -8789;
    }

    @Condition
    public boolean when(Facts facts) {
        String lotteryType = facts.get("lotteryType");
        return "ssc".equals(lotteryType) && facts.get("currentPrizeModes") != null;
    }

    @Action
    public void then(Facts facts) {
        try {
            Map<String, String> currentCategory = facts.get("currentCategory");
            Map<String, String> currentMethod = facts.get("currentMethod");
            String methodName = currentCategory.get("name") + currentMethod.get("name");
            boolean disabled = false;

            // 单式屏蔽
            if (methodName.contains("单式")) {
                disabled = true;
            }

            String currentCategoryFlag = currentCategory.get("flag");

            if (Matchers.pk10Alias.contains(currentCategoryFlag) || Matchers.jssmAlias.contains(currentCategoryFlag)) {
                List<String> disabledMethods = List.of("竞速竞速", "对决对决");
                if (disabledMethods.contains(methodName)) {
                    disabled = true;
                }
            }

            // 不计算盈利的，清除奖金和奖励
            if (disabled) {
                facts.put("currentBonus", "");
                facts.put("currentPrize", "");
            }
        } catch (Exception e) {
            CfLog.e(e.getMessage());
        }
    }
}
