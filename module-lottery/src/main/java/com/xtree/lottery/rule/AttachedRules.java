package com.xtree.lottery.rule;

import com.xtree.lottery.rule.attached.AddFlagRule;
import com.xtree.lottery.rule.attached.AddNumRule;
import com.xtree.lottery.rule.attached.AddPosNumRule;

import org.jeasy.rules.api.Rules;

public class AttachedRules {

    public static void addRules(Rules rules) {
        rules.register(new AddNumRule());
        rules.register(new AddPosNumRule());
        rules.register(new AddFlagRule());
    }
}
