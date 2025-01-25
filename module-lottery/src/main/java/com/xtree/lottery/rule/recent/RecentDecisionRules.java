package com.xtree.lottery.rule.recent;

import com.xtree.lottery.rule.recent.decision.BaccaratKingRule;
import com.xtree.lottery.rule.recent.decision.BaccaratLeopardRule;
import com.xtree.lottery.rule.recent.decision.BaccaratPairRule;
import com.xtree.lottery.rule.recent.decision.BaccaratRule;
import com.xtree.lottery.rule.recent.decision.DragonTigerBattleRule;
import com.xtree.lottery.rule.recent.decision.FiveInOneRule;
import com.xtree.lottery.rule.recent.decision.GroupConfigurationRule;
import com.xtree.lottery.rule.recent.decision.K3GeneralRule;
import com.xtree.lottery.rule.recent.decision.K3SerialNoRule;
import com.xtree.lottery.rule.recent.decision.NiuniuRule;
import com.xtree.lottery.rule.recent.decision.PK10SingleDoubleRule;
import com.xtree.lottery.rule.recent.decision.SingleDoubleCalculationRule;
import com.xtree.lottery.rule.recent.decision.StraightSelectionSpanRule;
import com.xtree.lottery.rule.recent.decision.SumCalculationRule;
import com.xtree.lottery.rule.recent.decision.SumTailNumberRule;
import com.xtree.lottery.rule.recent.decision.VNMMiddleSouthFastRule;
import com.xtree.lottery.rule.recent.decision.VnmNorthRule;

import org.jeasy.rules.api.Rules;

public class RecentDecisionRules {
    public static void addRules(Rules rules) {
        rules.register(new GroupConfigurationRule());
        rules.register(new SumCalculationRule());
        rules.register(new SingleDoubleCalculationRule());
        rules.register(new StraightSelectionSpanRule());
        rules.register(new SumTailNumberRule());
        rules.register(new DragonTigerBattleRule());
        rules.register(new BaccaratRule());
        rules.register(new BaccaratPairRule());
        rules.register(new BaccaratLeopardRule());
        rules.register(new BaccaratKingRule());
        rules.register(new FiveInOneRule());
        rules.register(new K3GeneralRule());
        rules.register(new K3SerialNoRule());
        rules.register(new PK10SingleDoubleRule());
        rules.register(new NiuniuRule());
        rules.register(new VnmNorthRule());
        rules.register(new VNMMiddleSouthFastRule());
    }
}
