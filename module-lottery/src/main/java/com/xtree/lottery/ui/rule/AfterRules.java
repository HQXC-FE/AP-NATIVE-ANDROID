package com.xtree.lottery.ui.rule;

import com.xtree.lottery.ui.rule.after.AnySoloRule;
import com.xtree.lottery.ui.rule.after.CalculateBonusRule;
import com.xtree.lottery.ui.rule.after.CalculateSSCProfitPrizeModesRule;
import com.xtree.lottery.ui.rule.after.CalculateTotalBetAmountRule;
import com.xtree.lottery.ui.rule.after.Choose5LotteryRule;
import com.xtree.lottery.ui.rule.after.ClearPrizeAndBonusRule;
import com.xtree.lottery.ui.rule.after.CombinationProfitCalculationRule;
import com.xtree.lottery.ui.rule.after.DwdPositionRule;
import com.xtree.lottery.ui.rule.after.ElevenOptionalMultipleRule;
import com.xtree.lottery.ui.rule.after.FillZeroCountRule;
import com.xtree.lottery.ui.rule.after.GeneralProfitCalculationRule;
import com.xtree.lottery.ui.rule.after.K3TwoDifferentNumbersRule;
import com.xtree.lottery.ui.rule.after.LotteryPrizeRule;
import com.xtree.lottery.ui.rule.after.PK10JSSMBigSmallOddEvenRule;
import com.xtree.lottery.ui.rule.after.PkTypeFormatRule;
import com.xtree.lottery.ui.rule.after.PositionChosenRule;
import com.xtree.lottery.ui.rule.after.RelationMethodsRule;
import com.xtree.lottery.ui.rule.after.SSCDontCalculateProfitRule;
import com.xtree.lottery.ui.rule.after.SetFundingModeRule;
import com.xtree.lottery.ui.rule.after.SetTimesRule;
import com.xtree.lottery.ui.rule.after.SingleLevelPrizeRule;
import com.xtree.lottery.ui.rule.after.SoloIdentifierRule;
import com.xtree.lottery.ui.rule.after.ThreeDPositionAndUncertainRule;

import org.jeasy.rules.api.Rules;

public class AfterRules {

    private Rules rules;

    public AfterRules() {
        rules = new Rules();
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

    public Rules getRules() {
        return rules;
    }
}
