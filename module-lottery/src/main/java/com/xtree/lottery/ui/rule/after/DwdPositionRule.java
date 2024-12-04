package com.xtree.lottery.ui.rule.after;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.List;

@Rule(name = "DwdPositionRule", description = "定位胆和位置玩法规则")
public class DwdPositionRule {

    @Priority
    public int getPriority() {
        return 8794;
    }

    @Condition
    public boolean when(Facts facts) {
        boolean currentPrizeModes = (boolean) facts.get("currentPrizeModes");
        String currentCategoryName = (String) facts.get("currentCategoryName");
        List<String> currentCategoryFlag = (List<String>) facts.get("currentCategoryFlag");
        List<String> pk10Alias = (List<String>) facts.get("pk10Alias");
        List<String> jssmAlias = (List<String>) facts.get("jssmAlias");

        return currentPrizeModes &&
                ("定位胆".equals(currentCategoryName) || "位置".equals(currentCategoryName)) &&
                currentCategoryFlag.stream().anyMatch(flag -> pk10Alias.contains(flag) || jssmAlias.contains(flag));
    }

    @Action
    public void then(Facts facts) {
        List<String> betPosChoose = (List<String>) facts.get("betPosChoose");
        List<List<String>> formatCodes = (List<List<String>>) facts.get("formatCodes");
        String currentBonus = (String) facts.get("currentBonus");
        String currentPrize = (String) facts.get("currentPrize");

        int posChooseNum = (betPosChoose != null)
                ? (int) betPosChoose.stream().filter(item -> item != null && !item.isEmpty()).count()
                : (int) formatCodes.stream().filter(item -> !item.isEmpty()).count();

        int differenceNum = (int) formatCodes.stream().filter(item -> !item.isEmpty()).count();
        differenceNum = Math.min(differenceNum, posChooseNum);

        if (differenceNum > 1) {
            double prize = Double.parseDouble(currentPrize);
            double bonus = Double.parseDouble(currentBonus);

            currentBonus = currentBonus + "~" + round(bonus + prize * (differenceNum - 1), 4);
            currentPrize = currentPrize + "~" + round(prize * differenceNum, 4);

            facts.put("currentBonus", currentBonus);
            facts.put("currentPrize", currentPrize);
        }
    }

    private double round(double value, int precision) {
        double scale = Math.pow(10, precision);
        return Math.round(value * scale) / scale;
    }
}
