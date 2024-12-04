package com.xtree.lottery.rule;

import com.xtree.lottery.rule.after.AnySoloRule;
import com.xtree.lottery.rule.after.CalculateBonusRule;
import com.xtree.lottery.rule.after.CalculateSSCProfitPrizeModesRule;
import com.xtree.lottery.rule.after.CalculateTotalBetAmountRule;
import com.xtree.lottery.rule.after.Choose5LotteryRule;
import com.xtree.lottery.rule.after.ClearPrizeAndBonusRule;
import com.xtree.lottery.rule.after.CombinationProfitCalculationRule;
import com.xtree.lottery.rule.after.DwdPositionRule;
import com.xtree.lottery.rule.after.ElevenOptionalMultipleRule;
import com.xtree.lottery.rule.after.FillZeroCountRule;
import com.xtree.lottery.rule.after.GeneralProfitCalculationRule;
import com.xtree.lottery.rule.after.K3TwoDifferentNumbersRule;
import com.xtree.lottery.rule.after.LotteryPrizeRule;
import com.xtree.lottery.rule.after.PK10JSSMBigSmallOddEvenRule;
import com.xtree.lottery.rule.after.PkTypeFormatRule;
import com.xtree.lottery.rule.after.PositionChosenRule;
import com.xtree.lottery.rule.after.RelationMethodsRule;
import com.xtree.lottery.rule.after.SSCDontCalculateProfitRule;
import com.xtree.lottery.rule.after.SetFundingModeRule;
import com.xtree.lottery.rule.after.SetTimesRule;
import com.xtree.lottery.rule.after.SingleLevelPrizeRule;
import com.xtree.lottery.rule.after.SoloIdentifierRule;
import com.xtree.lottery.rule.after.ThreeDPositionAndUncertainRule;

import org.jeasy.rules.api.Rules;

public class AfterRules {
    public static void addRules(Rules rules) {
        rules.register(new AnySoloRule());
        rules.register(new CalculateSSCProfitPrizeModesRule());
        rules.register(new CalculateTotalBetAmountRule());
        rules.register(new ClearPrizeAndBonusRule());
        rules.register(new CombinationProfitCalculationRule());
        rules.register(new DwdPositionRule());
        rules.register(new ElevenOptionalMultipleRule());
        rules.register(new FillZeroCountRule());
        rules.register(new CalculateBonusRule());
        rules.register(new GeneralProfitCalculationRule());
        rules.register(new K3TwoDifferentNumbersRule());
        rules.register(new LotteryPrizeRule());
        rules.register(new PK10JSSMBigSmallOddEvenRule());
        rules.register(new PkTypeFormatRule());
        rules.register(new PositionChosenRule());
        rules.register(new RelationMethodsRule());
        rules.register(new SetFundingModeRule());
        rules.register(new SetTimesRule());
        rules.register(new SingleLevelPrizeRule());
        rules.register(new SoloIdentifierRule());
        rules.register(new SSCDontCalculateProfitRule());
        rules.register(new ThreeDPositionAndUncertainRule());
        rules.register(new Choose5LotteryRule());
    }
}
