package com.xtree.lottery.rule.betting;

import com.xtree.lottery.rule.betting.attached.AddFlagRule;
import com.xtree.lottery.rule.betting.attached.AddNumRule;
import com.xtree.lottery.rule.betting.attached.AddPosNumRule;

import org.jeasy.rules.api.Rules;

public class AttachedRules {

    public static void addRules(Rules rules) {
        rules.register(new AddNumRule());
        rules.register(new AddPosNumRule());
        rules.register(new AddFlagRule());
    }
}
