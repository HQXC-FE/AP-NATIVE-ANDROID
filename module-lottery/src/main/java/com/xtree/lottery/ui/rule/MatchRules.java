package com.xtree.lottery.ui.rule;

import com.xtree.lottery.ui.rule.match.LHCRuleSetRule;
import com.xtree.lottery.ui.rule.match.MatchMethodIdRule;
import com.xtree.lottery.ui.rule.match.MatchRuleTokenToSetRule;
import com.xtree.lottery.ui.rule.match.NoRuleSetFoundRule;
import com.xtree.lottery.ui.rule.match.NoRuleTokenFoundRule;

import org.jeasy.rules.api.Rules;

public class MatchRules {

    private Rules rules;

    public MatchRules() {
        rules = new Rules();
        rules.register(new MatchMethodIdRule());
        rules.register(new NoRuleTokenFoundRule());
        rules.register(new MatchRuleTokenToSetRule());
        rules.register(new NoRuleSetFoundRule());
        rules.register(new LHCRuleSetRule());
    }

    public Rules getRules() {
        return rules;
    }
}
