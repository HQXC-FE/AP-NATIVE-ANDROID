package com.xtree.lottery.ui.rule;

import com.xtree.lottery.ui.rule.prepare.FormatBoxTypeRule;
import com.xtree.lottery.ui.rule.prepare.FormatDataWithLayoutRule;
import com.xtree.lottery.ui.rule.prepare.FormatDataWithoutLayoutRule;
import com.xtree.lottery.ui.rule.prepare.PreserveOriginalFormatRule;

import org.jeasy.rules.api.Rules;

public class PrepareRules {

    private Rules rules;

    public PrepareRules() {
        rules = new Rules();
        rules.register(new FormatDataWithLayoutRule());
        rules.register(new FormatDataWithoutLayoutRule());
        rules.register(new FormatBoxTypeRule());
        rules.register(new PreserveOriginalFormatRule());
    }

    public Rules getRules() {
        return rules;
    }
}
