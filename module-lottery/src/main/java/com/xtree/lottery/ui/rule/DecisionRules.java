package com.xtree.lottery.ui.rule;

import com.xtree.lottery.ui.rule.decision.AnyChosenRule;
import com.xtree.lottery.ui.rule.decision.CombinationChosenRule;
import com.xtree.lottery.ui.rule.decision.CombinationRule;
import com.xtree.lottery.ui.rule.decision.DantuoDif2Rule;
import com.xtree.lottery.ui.rule.decision.DantuoDif3Rule;
import com.xtree.lottery.ui.rule.decision.GeneralElectionRule;
import com.xtree.lottery.ui.rule.decision.LineMissRule;
import com.xtree.lottery.ui.rule.decision.MultiCombinationRule;
import com.xtree.lottery.ui.rule.decision.MultiRule;
import com.xtree.lottery.ui.rule.decision.MultiplyRule;
import com.xtree.lottery.ui.rule.decision.PositionChosenRule;
import com.xtree.lottery.ui.rule.decision.RankingChosenRule;
import com.xtree.lottery.ui.rule.decision.RankingPkRule;
import com.xtree.lottery.ui.rule.decision.SingleNumberMultipleBetsRule;
import com.xtree.lottery.ui.rule.decision.SingleRule;
import com.xtree.lottery.ui.rule.decision.SpanChosenRule;
import com.xtree.lottery.ui.rule.decision.SumPlusDifRule;
import com.xtree.lottery.ui.rule.decision.SumPlusRule;
import com.xtree.lottery.ui.rule.decision.TimesChosenRule;

import org.jeasy.rules.api.Rules;

public class DecisionRules {

    private Rules rules;

    public DecisionRules() {
        rules = new Rules();
        rules.register(new MultiRule());
        rules.register(new SingleRule());
        rules.register(new CombinationRule());
        rules.register(new MultiCombinationRule());
        rules.register(new SumPlusRule());
        rules.register(new AnyChosenRule());
        rules.register(new TimesChosenRule());
        rules.register(new GeneralElectionRule());
        rules.register(new PositionChosenRule());
        rules.register(new SpanChosenRule());
        rules.register(new CombinationChosenRule());
        rules.register(new RankingChosenRule());
        rules.register(new SumPlusDifRule());
        rules.register(new RankingPkRule());
        rules.register(new DantuoDif3Rule());
        rules.register(new DantuoDif2Rule());
        rules.register(new SingleNumberMultipleBetsRule());
        rules.register(new LineMissRule());
        rules.register(new MultiplyRule());
    }

    public Rules getRules() {
        return rules;
    }
}
