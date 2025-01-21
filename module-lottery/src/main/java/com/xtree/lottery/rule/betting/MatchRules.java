package com.xtree.lottery.rule.betting;

import com.xtree.lottery.rule.betting.match.LHCRuleSetRule;
import com.xtree.lottery.rule.betting.match.MatchMethodIdRule;
import com.xtree.lottery.rule.betting.match.MatchRuleTokenToSetRule;
import com.xtree.lottery.rule.betting.match.NoRuleSetFoundRule;
import com.xtree.lottery.rule.betting.match.NoRuleTokenFoundRule;

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
