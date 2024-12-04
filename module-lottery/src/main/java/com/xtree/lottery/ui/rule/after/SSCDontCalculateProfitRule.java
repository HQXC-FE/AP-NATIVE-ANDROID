package com.xtree.lottery.ui.rule.after;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;

import java.util.List;
import java.util.Map;

@Rule(name = "SSCDontCalculateProfitRule", description = "时时彩不计算盈利的玩法规则")
public class SSCDontCalculateProfitRule {

    @Priority
    public int getPriority() {
        return 8789;
    }

    @Condition
    public boolean when(Map<String, Object> facts) {
        String lotteryType = (String) facts.get("lotteryType");
        return "ssc".equals(lotteryType) && facts.get("currentPrizeModes") != null;
    }

    @Action
    public void then(Map<String, Object> facts) {
        String methodName = (String) facts.get("currentCategoryName") + facts.get("currentMethodName");
        boolean disabled = false;

        // 单式屏蔽
        if (methodName.contains("单式")) {
            disabled = true;
        }

        // PK10 和 JSSM 的屏蔽规则
        List<String> pk10Alias = (List<String>) facts.get("pk10Alias");
        List<String> jssmAlias = (List<String>) facts.get("jssmAlias");
        String currentCategoryFlag = (String) facts.get("currentCategoryFlag");

        if (pk10Alias.contains(currentCategoryFlag) || jssmAlias.contains(currentCategoryFlag)) {
            List<String> disabledMethods = List.of("竞速竞速", "对决对决");
            if (disabledMethods.contains(methodName)) {
                disabled = true;
            }
        }

        // 不计算盈利的，清除奖金和奖励
        if (disabled) {
            facts.put("currentBonus", null);
            facts.put("currentPrize", null);
        }
    }
}
