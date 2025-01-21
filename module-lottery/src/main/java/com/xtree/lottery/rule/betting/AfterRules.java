package com.xtree.lottery.rule.betting;

import com.xtree.lottery.rule.betting.after.AnySoloRule;
import com.xtree.lottery.rule.betting.after.CalculateBonusRule;
import com.xtree.lottery.rule.betting.after.CalculateSSCProfitPrizeModesRule;
import com.xtree.lottery.rule.betting.after.CalculateTotalBetAmountRule;
import com.xtree.lottery.rule.betting.after.Choose5LotteryRule;
import com.xtree.lottery.rule.betting.after.ClearPrizeAndBonusRule;
import com.xtree.lottery.rule.betting.after.CombinationProfitCalculationRule;
import com.xtree.lottery.rule.betting.after.DwdPositionRule;
import com.xtree.lottery.rule.betting.after.ElevenOptionalMultipleRule;
import com.xtree.lottery.rule.betting.after.FillZeroCountRule;
import com.xtree.lottery.rule.betting.after.GeneralProfitCalculationRule;
import com.xtree.lottery.rule.betting.after.K3TwoDifferentNumbersRule;
import com.xtree.lottery.rule.betting.after.LotteryPrizeRule;
import com.xtree.lottery.rule.betting.after.PK10JSSMBigSmallOddEvenRule;
import com.xtree.lottery.rule.betting.after.PkTypeFormatRule;
import com.xtree.lottery.rule.betting.after.PositionChosenRule;
import com.xtree.lottery.rule.betting.after.RelationMethodsRule;
import com.xtree.lottery.rule.betting.after.SSCDontCalculateProfitRule;
import com.xtree.lottery.rule.betting.after.SetFundingModeRule;
import com.xtree.lottery.rule.betting.after.SetTimesRule;
import com.xtree.lottery.rule.betting.after.SingleLevelPrizeRule;
import com.xtree.lottery.rule.betting.after.SoloIdentifierRule;
import com.xtree.lottery.rule.betting.after.ThreeDPositionAndUncertainRule;

import org.jeasy.rules.api.Rules;

public class AfterRules {
    public static void addRules(Rules rules) {
        rules.register(new PkTypeFormatRule());
        rules.register(new AnySoloRule());
        rules.register(new SoloIdentifierRule());
        rules.register(new FillZeroCountRule());
        rules.register(new SetFundingModeRule());
        rules.register(new SetTimesRule());
        rules.register(new CalculateTotalBetAmountRule());
        rules.register(new LotteryPrizeRule());
        rules.register(new PositionChosenRule());
        rules.register(new CalculateSSCProfitPrizeModesRule());
        rules.register(new GeneralProfitCalculationRule());
        rules.register(new CombinationProfitCalculationRule());
        rules.register(new CalculateBonusRule());
        rules.register(new Choose5LotteryRule());
        rules.register(new DwdPositionRule());
        rules.register(new PK10JSSMBigSmallOddEvenRule());
        rules.register(new ElevenOptionalMultipleRule());
        rules.register(new ThreeDPositionAndUncertainRule());
        rules.register(new K3TwoDifferentNumbersRule());
        rules.register(new SingleLevelPrizeRule());
        rules.register(new SSCDontCalculateProfitRule());
        rules.register(new ClearPrizeAndBonusRule());
        rules.register(new RelationMethodsRule());
    }
}
