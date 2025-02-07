package com.xtree.lottery.rule.betting.after;

import com.xtree.lottery.rule.betting.Matchers;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.List;
import java.util.Map;

@Rule(name = "3DPositionAndUncertainRule", description = "3D排列三定位胆、不定胆")
public class ThreeDPositionAndUncertainRule {

    @Priority
    public int getPriority() {
        return -8792;
    }

    @Condition
    public boolean when(Facts facts) {
        Map<String, String> currentCategory = facts.get("currentCategory");
        String currentCategoryName = currentCategory.get("name");
        String currentCategoryFlag = currentCategory.get("flag");

        return facts.get("currentPrizeModes") != null &&
                (currentCategoryName.equals("定位胆") || currentCategoryName.equals("不定胆")) &&
                (Matchers.pk10Alias.contains(currentCategoryFlag) ||
                        Matchers.jssmAlias.contains(currentCategoryFlag) ||
                        Matchers.sscAlias.contains(currentCategoryFlag));
    }

    @Action
    public void then(Facts facts) {
        String methodName = (String) facts.get("currentCategoryName") + facts.get("currentMethodName");
        Map<String, List<Boolean>> bet = facts.get("bet");
        double currentPrize = Double.parseDouble(facts.get("currentPrize"));
        double currentBonus = Double.parseDouble(facts.get("currentBonus"));
        List<Boolean> posChoose = bet.get("posChoose");
        List<List<String>> formatCodes = facts.get("formatCodes");

        int posChooseNum = 0;

        if (posChoose != null) {
            posChooseNum = (int) posChoose.stream().filter(Boolean::booleanValue).count();
        } else {
            posChooseNum = (int) formatCodes.stream().filter(code -> !code.isEmpty()).count();
        }

        switch (methodName) {
            case "定位胆定位胆":
                if (posChooseNum > 1) {
                    facts.put("currentBonus", currentBonus + "~" + Math.round(currentPrize * (posChooseNum - 1) + currentBonus * 10000.0) / 10000.0);
                    facts.put("currentPrize", currentPrize + "~" + Math.round(currentPrize * posChooseNum * 10000.0) / 10000.0);
                }
                break;

            case "不定胆一码不定胆":
                if (formatCodes.get(0).size() > 1) {
                    int times = Math.min(formatCodes.get(0).size(), 3);
                    facts.put("currentBonus", currentBonus + "~" + Math.round(currentPrize * (times - 1) + currentBonus * 10000.0) / 10000.0);
                    facts.put("currentPrize", currentPrize + "~" + Math.round(currentPrize * times * 10000.0) / 10000.0);
                }
                break;

            default:
                break;
        }
    }
}
