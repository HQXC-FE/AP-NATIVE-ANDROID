package com.xtree.lottery.rule.betting.after;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.Map;

@Rule(name = "Calculate Bonus for 趣味 and 不定胆", description = "Calculate bonus for 趣味 and 不定胆 types")
public class CalculateBonusRule {

    @Priority
    public int getPriority() {
        return -8796;
    }

    @Condition
    public boolean when(Facts facts) {
        String currentCategoryName = (String) ((Map<String, Object>) facts.get("currentCategory")).get("name");
        Object currentPrizeModes = facts.get("currentPrizeModes");
        return currentPrizeModes != null &&
                ("趣味".equals(currentCategoryName) ||
                        "不定胆".equals(currentCategoryName) ||
                        "龙虎斗".equals(currentCategoryName));
    }

    @Action
    public void then(Facts facts) {
        try {
            String methodName = (String) ((Map<String, Object>) facts.get("currentCategory")).get("name");
            Integer num = facts.get("num");
            double currentBonus = facts.get("currentBonus");
            double currentPrize = facts.get("currentPrize");

            if (num == null || methodName == null) {
                return;
            }

            switch (methodName) {
                case "四星一码不定胆":
                case "五星一码不定胆":
                case "前三一码不定胆":
                case "中三一码不定胆":
                case "后三一码不定胆":
                case "前三位": {
                    if (num == 2) {
                        currentBonus = addRange(currentBonus, currentPrize, 1);
                        currentPrize = addRange(currentPrize, currentPrize, 2);
                    } else if (num == 3) {
                        currentBonus = addRange(currentBonus, currentPrize * 2, 1);
                        currentPrize = addRange(currentPrize, currentPrize, 3);
                    } else if (num > 3 && !"四星一码不定胆".equals(methodName) && !"五星一码不定胆".equals(methodName)) {
                        currentBonus = addRange(currentBonus, currentPrize * 2, 1);
                        currentPrize = addRange(currentPrize, currentPrize, 3);
                    } else if (num == 4 && ("四星一码不定胆".equals(methodName) || "五星一码不定胆".equals(methodName))) {
                        currentBonus = addRange(currentBonus, currentPrize * 3, 1);
                        currentPrize = addRange(currentPrize, currentPrize, 4);
                    } else if (num > 4 && "四星一码不定胆".equals(methodName)) {
                        currentBonus = addRange(currentBonus, currentPrize * 3, 1);
                        currentPrize = addRange(currentPrize, currentPrize, 4);
                    } else if (num >= 5 && "五星一码不定胆".equals(methodName)) {
                        currentBonus = addRange(currentBonus, currentPrize * 4, 1);
                        currentPrize = addRange(currentPrize, currentPrize, 5);
                    }
                    break;
                }
                case "四星二码不定胆":
                case "五星二码不定胆":
                case "前三二码不定胆":
                case "中三二码不定胆":
                case "后三二码不定胆": {
                    if (num == 3) {
                        currentBonus = addRange(currentBonus, currentPrize * 2, 1);
                        currentPrize = addRange(currentPrize, currentPrize, 3);
                    } else if (num > 3 && !"四星二码不定胆".equals(methodName) && !"五星二码不定胆".equals(methodName)) {
                        currentBonus = addRange(currentBonus, currentPrize * 2, 1);
                        currentPrize = addRange(currentPrize, currentPrize, 3);
                    } else if (num > 4 && "四星二码不定胆".equals(methodName)) {
                        currentBonus = addRange(currentBonus, currentPrize * 5, 1);
                        currentPrize = addRange(currentPrize, currentPrize, 6);
                    } else if (num == 6 && "五星二码不定胆".equals(methodName)) {
                        currentBonus = addRange(currentBonus, currentPrize * 5, 1);
                        currentPrize = addRange(currentPrize, currentPrize, 6);
                    } else if (num >= 10 && "五星二码不定胆".equals(methodName)) {
                        currentBonus = addRange(currentBonus, currentPrize * 9, 1);
                        currentPrize = addRange(currentPrize, currentPrize, 10);
                    }
                    break;
                }
                case "四星三码不定胆":
                case "五星三码不定胆": {
                    if (num >= 4 && "四星三码不定胆".equals(methodName)) {
                        currentBonus = addRange(currentBonus, currentPrize * 3, 1);
                        currentPrize = addRange(currentPrize, currentPrize, 4);
                    } else if (num == 4 && "五星三码不定胆".equals(methodName)) {
                        currentBonus = addRange(currentBonus, currentPrize * 3, 1);
                        currentPrize = addRange(currentPrize, currentPrize, 4);
                    } else if (num >= 10 && "五星三码不定胆".equals(methodName)) {
                        currentBonus = addRange(currentBonus, currentPrize * 9, 1);
                        currentPrize = addRange(currentPrize, currentPrize, 10);
                    }
                    break;
                }
                case "一帆风顺": {
                    if (num > 1 && num <= 5) {
                        currentBonus = addRange(currentBonus, currentPrize * (num - 1), 1);
                        currentPrize = addRange(currentPrize, currentPrize, num);
                    } else if (num > 5) {
                        currentBonus = addRange(currentBonus, currentPrize * 4, 1);
                        currentPrize = addRange(currentPrize, currentPrize, 5);
                    }
                    break;
                }
                case "好事成双": {
                    if (num > 1) {
                        currentBonus = addRange(currentBonus, currentPrize, 1);
                        currentPrize = addRange(currentPrize, currentPrize, 2);
                    }
                    break;
                }
                case "龙虎斗":
                case "玄麟斗": {
                    currentBonus = extractBaseValue(currentBonus);
                    currentPrize = extractBaseValue(currentPrize);
                    break;
                }
            }

            facts.put("currentBonus", currentBonus);
            facts.put("currentPrize", currentPrize);
        } catch (Exception e) {
            CfLog.e(e.getMessage());
        }
    }

    private double addRange(double baseValue, double increment, int multiplier) {
        return Double.parseDouble(String.format("%.4f", baseValue)) + increment * multiplier;
    }

    private double extractBaseValue(double value) {
        String[] parts = String.valueOf(value).split("~");
        return Double.parseDouble(parts[0]);
    }
}