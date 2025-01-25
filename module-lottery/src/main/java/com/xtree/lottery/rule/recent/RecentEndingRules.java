package com.xtree.lottery.rule.recent;

import com.xtree.lottery.rule.recent.ending.CallbackWrapperRule;
import com.xtree.lottery.rule.recent.ending.FillEmptyValuesRule;

import org.jeasy.rules.api.Rules;

public class RecentEndingRules {
    public static void addRules(Rules rules) {
        rules.register(new FillEmptyValuesRule());
        rules.register(new CallbackWrapperRule());
    }
}
