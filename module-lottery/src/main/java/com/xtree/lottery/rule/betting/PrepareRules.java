package com.xtree.lottery.rule.betting;

import com.xtree.lottery.rule.betting.prepare.FormatBoxTypeRule;
import com.xtree.lottery.rule.betting.prepare.FormatDataWithLayoutRule;
import com.xtree.lottery.rule.betting.prepare.FormatDataWithoutLayoutRule;
import com.xtree.lottery.rule.betting.prepare.PreserveOriginalFormatRule;

import org.jeasy.rules.api.Rules;

public class PrepareRules {
    public static void addRules(Rules rules) {
        rules.register(new FormatDataWithLayoutRule());
        rules.register(new FormatDataWithoutLayoutRule());
        rules.register(new FormatBoxTypeRule());
        rules.register(new PreserveOriginalFormatRule());
    }
}
