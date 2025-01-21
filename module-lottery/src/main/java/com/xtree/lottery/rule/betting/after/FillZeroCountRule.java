package com.xtree.lottery.rule.betting.after;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

@Rule(name = "Fill Zero Count", description = "Fill count with zero if undefined")
public class FillZeroCountRule {

    @Priority
    public int getPriority() {
        return -8850;
    }

    @Condition
    public boolean when(Facts facts) {
        Integer num = facts.get("num");
        return num == null;
    }

    @Action
    public void then(Facts facts) {
        try {
            facts.put("num", 0);
        } catch (Exception e) {
            CfLog.e(e.getMessage());
        }
    }
}
