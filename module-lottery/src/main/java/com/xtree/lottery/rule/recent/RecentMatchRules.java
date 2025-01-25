package com.xtree.lottery.rule.recent;

import com.xtree.lottery.rule.recent.match.ExtractUsefulValuesRule;
import com.xtree.lottery.rule.recent.match.GenerateRuleSuiteRule;
import com.xtree.lottery.rule.recent.match.IssueNumberRule;
import com.xtree.lottery.rule.recent.match.MmcIssueFillRule;
import com.xtree.lottery.rule.recent.match.NoMatchingRule;

import org.jeasy.rules.api.Rules;

public class RecentMatchRules {
    public static void addRules(Rules rules) {
        rules.register(new GenerateRuleSuiteRule());
        rules.register(new IssueNumberRule());
        rules.register(new MmcIssueFillRule());
        rules.register(new NoMatchingRule());
        rules.register(new ExtractUsefulValuesRule());
    }
}
