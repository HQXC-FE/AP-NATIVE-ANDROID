package com.xtree.lottery.rule.betting;

import com.xtree.lottery.rule.betting.ending.CommonLHCProcessingRule;
import com.xtree.lottery.rule.betting.ending.SSCFinalMergeRule;

import org.jeasy.rules.api.Rules;

public class EndingRules {
    public static void addRules(Rules rules) {
        rules.register(new SSCFinalMergeRule());
        rules.register(new CommonLHCProcessingRule());
    }
}
