package com.xtree.lottery.ui.rule;

import com.xtree.lottery.ui.rule.filter.DuplicateRemovalRule;
import com.xtree.lottery.ui.rule.filter.RegexFilterRule;

import org.jeasy.rules.api.Rules;

public class FilterRules {

    private Rules rules;

    public FilterRules() {
        rules = new Rules();
        rules.register(new DuplicateRemovalRule());
        rules.register(new RegexFilterRule());
    }

    public Rules getRules() {
        return rules;
    }
}
