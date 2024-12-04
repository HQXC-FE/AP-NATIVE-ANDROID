package com.xtree.lottery.rule;

import com.xtree.lottery.rule.match.LHCRuleSetRule;
import com.xtree.lottery.rule.match.MatchMethodIdRule;
import com.xtree.lottery.rule.match.MatchRuleTokenToSetRule;
import com.xtree.lottery.rule.match.NoRuleSetFoundRule;
import com.xtree.lottery.rule.match.NoRuleTokenFoundRule;

import org.jeasy.rules.api.Rules;

public class MatchRules {
    public static void addRules(Rules rules) {
        rules.register(new MatchMethodIdRule());
        rules.register(new NoRuleTokenFoundRule());
        rules.register(new MatchRuleTokenToSetRule());
        rules.register(new NoRuleSetFoundRule());
        rules.register(new LHCRuleSetRule());
    }
}
