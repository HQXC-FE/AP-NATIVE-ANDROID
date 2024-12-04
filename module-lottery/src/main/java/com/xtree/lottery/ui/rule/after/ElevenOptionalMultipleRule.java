package com.xtree.lottery.ui.rule.after;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.List;

@Rule(name = "11x5OptionalMultipleRule", description = "11选5任选复式规则")
public class ElevenOptionalMultipleRule {

    @Priority
    public int getPriority() {
        return 8792;
    }

    @Condition
    public boolean when(Facts facts) {
        String currentCategoryName = facts.get("currentCategoryName");
        String currentCategoryFlag = facts.get("currentCategoryFlag");

        return facts.get("currentPrizeModes") != null &&
                currentCategoryName.equals("任选复式") &&
                currentCategoryFlag.contains("11x5");
    }

    @Action
    public void then(Facts facts) {
        String methodName = (String) facts.get("currentCategoryName") + facts.get("currentMethodName");
        List<String> formatCodes = facts.get("formatCodes");
        double currentPrize = facts.get("currentPrize");
        double currentBonus = facts.get("currentBonus");

        int chooseNum = Math.min(formatCodes.size(), 5);

        switch (methodName) {
            case "任选复式一中一":
                if (chooseNum > 1) {
                    facts.put("currentBonus", currentBonus + "~" + round(currentPrize * (chooseNum - 1) + currentBonus, 4));
                    facts.put("currentPrize", currentPrize + "~" + round(currentPrize * chooseNum, 4));
                }
                break;

            case "任选复式二中二":
                if (chooseNum == 3) {
                    facts.put("currentBonus", currentBonus + "~" + round(currentPrize * 2 + currentBonus, 4));
                    facts.put("currentPrize", currentPrize + "~" + round(currentPrize * 3, 4));
                }
                if (chooseNum == 4) {
                    facts.put("currentBonus", currentBonus + "~" + round(currentPrize * 5 + currentBonus, 4));
                    facts.put("currentPrize", currentPrize + "~" + round(currentPrize * 6, 4));
                }
                if (chooseNum == 5) {
                    facts.put("currentBonus", currentBonus + "~" + round(currentPrize * 9 + currentBonus, 4));
                    facts.put("currentPrize", currentPrize + "~" + round(currentPrize * 10, 4));
                }
                break;

            case "任选复式三中三":
                if (chooseNum == 4) {
                    facts.put("currentBonus", currentBonus + "~" + round(currentPrize * 3 + currentBonus, 4));
                    facts.put("currentPrize", currentPrize + "~" + round(currentPrize * 4, 4));
                }
                if (chooseNum == 5) {
                    facts.put("currentBonus", currentBonus + "~" + round(currentPrize * 9 + currentBonus, 4));
                    facts.put("currentPrize", currentPrize + "~" + round(currentPrize * 10, 4));
                }
                break;

            case "任选复式四中四":
                if (chooseNum == 5) {
                    facts.put("currentBonus", currentBonus + "~" + round(currentPrize * 4 + currentBonus, 4));
                    facts.put("currentPrize", currentPrize + "~" + round(currentPrize * 5, 4));
                }
                break;

            case "任选复式六中五":
                if (formatCodes.size() > 6) {
                    facts.put("currentBonus", round(currentPrize * (formatCodes.size() - 6) + currentBonus, 4));
                    facts.put("currentPrize", round(currentPrize * (formatCodes.size() - 5), 4));
                }
                break;

            case "任选复式七中五":
                processBonusAndPrize(formatCodes, 8, currentBonus, currentPrize, facts);
                processBonusAndPrize(formatCodes, 9, currentBonus, currentPrize, facts);
                processBonusAndPrize(formatCodes, 10, currentBonus, currentPrize, facts);
                processBonusAndPrize(formatCodes, 11, currentBonus, currentPrize, facts);
                break;

            case "任选复式八中五":
                processBonusAndPrize(formatCodes, 9, currentBonus, currentPrize, facts);
                processBonusAndPrize(formatCodes, 10, currentBonus, currentPrize, facts);
                processBonusAndPrize(formatCodes, 11, currentBonus, currentPrize, facts);
                break;
        }
    }

    private void processBonusAndPrize(List<String> formatCodes, int size, double currentBonus, double currentPrize, Facts facts) {
        if (formatCodes.size() == size) {
            facts.put("currentBonus", round(currentPrize * size + currentBonus, 4));
            facts.put("currentPrize", round(currentPrize * (size + 1), 4));
        }
    }

    private double round(double value, int precision) {
        double scale = Math.pow(10, precision);
        return Math.round(value * scale) / scale;
    }
}
