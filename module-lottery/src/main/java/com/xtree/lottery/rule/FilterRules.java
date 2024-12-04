package com.xtree.lottery.rule;

import com.xtree.lottery.rule.filter.DuplicateRemovalRule;
import com.xtree.lottery.rule.filter.RegexFilterRule;

import org.jeasy.rules.api.Rules;

public class FilterRules {
    public static void addRules(Rules rules) {
        rules.register(new DuplicateRemovalRule());
        rules.register(new RegexFilterRule());
    }
}
