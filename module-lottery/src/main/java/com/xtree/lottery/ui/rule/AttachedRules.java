package com.xtree.lottery.ui.rule;

import com.xtree.lottery.ui.rule.attached.AddFlagRule;
import com.xtree.lottery.ui.rule.attached.AddNumRule;
import com.xtree.lottery.ui.rule.attached.AddPosNumRule;

import org.jeasy.rules.api.Rules;

public class AttachedRules {

    private Rules rules;

    public AttachedRules() {
        rules = new Rules();
        rules.register(new AddNumRule());
        rules.register(new AddPosNumRule());
        rules.register(new AddFlagRule());
    }

    public Rules getRules() {
        return rules;
    }
}
