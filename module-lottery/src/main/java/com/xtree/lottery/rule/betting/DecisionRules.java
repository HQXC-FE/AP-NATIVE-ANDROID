package com.xtree.lottery.rule.betting;

import com.xtree.lottery.rule.betting.decision.AnyChosenRule;
import com.xtree.lottery.rule.betting.decision.CombinationChosenRule;
import com.xtree.lottery.rule.betting.decision.CombinationRule;
import com.xtree.lottery.rule.betting.decision.DantuoAnyRule;
import com.xtree.lottery.rule.betting.decision.DantuoDif2Rule;
import com.xtree.lottery.rule.betting.decision.DantuoDif3Rule;
import com.xtree.lottery.rule.betting.decision.GeneralElectionRule;
import com.xtree.lottery.rule.betting.decision.LineMissRule;
import com.xtree.lottery.rule.betting.decision.MultiCombinationRule;
import com.xtree.lottery.rule.betting.decision.MultiRule;
import com.xtree.lottery.rule.betting.decision.MultiplyRule;
import com.xtree.lottery.rule.betting.decision.PositionChosenDecisionRule;
import com.xtree.lottery.rule.betting.decision.RankingChosenRule;
import com.xtree.lottery.rule.betting.decision.RankingPkRule;
import com.xtree.lottery.rule.betting.decision.SingleNumberMultipleBetsRule;
import com.xtree.lottery.rule.betting.decision.SpanChosenRule;
import com.xtree.lottery.rule.betting.decision.SpecialSingleRule;
import com.xtree.lottery.rule.betting.decision.SumPlusDifRule;
import com.xtree.lottery.rule.betting.decision.SumPlusRule;
import com.xtree.lottery.rule.betting.decision.TimesChosenRule;

import org.jeasy.rules.api.Rules;

public class DecisionRules {
    public static void addRules(Rules rules) {
        rules.register(new MultiRule());
        rules.register(new SpecialSingleRule());
        rules.register(new CombinationRule());
        rules.register(new MultiCombinationRule());
        rules.register(new SumPlusRule());
        rules.register(new AnyChosenRule());
        rules.register(new TimesChosenRule());
        rules.register(new GeneralElectionRule());
        rules.register(new PositionChosenDecisionRule());
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
        rules.register(new DantuoAnyRule());
    }
}
