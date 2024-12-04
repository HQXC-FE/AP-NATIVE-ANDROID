package com.xtree.lottery.ui.rule;

import com.xtree.lottery.ui.rule.ending.CommonLHCProcessingRule;
import com.xtree.lottery.ui.rule.ending.SSCFinalMergeRule;

import org.jeasy.rules.api.Rules;

public class EndingRules {

    private Rules rules;

    public EndingRules() {
        rules = new Rules();
        rules.register(new SSCFinalMergeRule());
        rules.register(new CommonLHCProcessingRule());
    }

    public Rules getRules() {
        return rules;
    }
}
