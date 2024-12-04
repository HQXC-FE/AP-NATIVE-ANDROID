package com.xtree.lottery.rule;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;

public class EntryRule {
    Rules rules;
    Facts facts;
    RulesEngine rulesEngine;

    public EntryRule() {
        rules = new Rules();
        facts = new Facts();
        rulesEngine = new DefaultRulesEngine();

        MatchRules.addRules(rules);
        FilterRules.addRules(rules);
        PrepareRules.addRules(rules);
        AttachedRules.addRules(rules);
        DecisionRules.addRules(rules);
        AfterRules.addRules(rules);
        EndingRules.addRules(rules);
    }

    public void startEngine(Facts facts) {
        rulesEngine.fire(rules, facts);
        //Todo
    }

}
