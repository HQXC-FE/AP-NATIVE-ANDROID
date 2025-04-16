package com.xtree.lottery.rule.betting.after;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.List;
import java.util.Map;

@Rule(name = "11选5任选复式规则", description = "按位置任选, 11选5任选复式")
public class Choose5LotteryRule {

    @Priority
    public int getPriority() {
        return -8795;
    }

    @Condition
    public boolean when(Facts facts) {
        List<String> currentPrizeModes = facts.get("currentPrizeModes");
        String currentCategoryName = (String) ((Map<String, Object>) facts.get("currentCategory")).get("name");
        return currentPrizeModes != null && List.of("任选二", "任选三", "任选四").contains(currentCategoryName);
    }

    @Action
    public void then(Facts facts) {
        try {
            String methodName = ((Map<String, String>) facts.get("currentCategory")).get("name") + ((Map<String, String>) facts.get("currentMethod")).get("name");
            int poschooseNum = calculatePositionChosenNumber(facts);
            String currentBonus = facts.get("currentBonus");
            String currentPrize = facts.get("currentPrize");
            double money = facts.get("money");
            List<Object> formatCodes = facts.get("formatCodes");
            switch (methodName) {
                case "任选二直选复式":
                case "任选二直选和值":
                    if (poschooseNum == 3) {
                        facts.put("currentBonus", currentBonus + "~" + round(Double.parseDouble(currentPrize) * 2 + Double.parseDouble(currentBonus)));
                        facts.put("currentPrize", currentPrize + "~" + round(Double.parseDouble(currentPrize) * 3));
                    } else if (poschooseNum == 4) {
                        facts.put("currentBonus", currentBonus + "~" + round(Double.parseDouble(currentPrize) * 5 + Double.parseDouble(currentBonus)));
                        facts.put("currentPrize", currentPrize + "~" + round(Double.parseDouble(currentPrize) * 6));
                    } else if (poschooseNum == 5) {
                        facts.put("currentBonus", currentBonus + "~" + round(Double.parseDouble(currentPrize) * 9 + Double.parseDouble(currentBonus)));
                        facts.put("currentPrize", currentPrize + "~" + round(Double.parseDouble(currentPrize) * 10));
                    }
                    break;
                case "任选二组选复式":
                    if (poschooseNum == 3 && formatCodes.size() == 2) {
                        facts.put("currentBonus", currentBonus + "~" + round(Double.parseDouble(currentPrize) + Double.parseDouble(currentBonus)));
                        facts.put("currentPrize", currentPrize + "~" + round(Double.parseDouble(currentPrize) * 2));
                    } else if (poschooseNum == 3 &&  formatCodes.size() >= 3) {
                        facts.put("currentBonus", currentBonus + "~" + round(Double.parseDouble(currentPrize) * 3 - money));
                        facts.put("currentPrize", currentPrize + "~" + round(Double.parseDouble(currentPrize) * 3));
                    } else if (poschooseNum == 4 && formatCodes.size() == 2) {
                        facts.put("currentBonus", currentBonus + "~" + round(Double.parseDouble(currentPrize) * 4 - money));
                        facts.put("currentPrize", currentPrize + "~" + round(Double.parseDouble(currentPrize) * 4));
                    } else if (poschooseNum == 4 && formatCodes.size() == 3) {
                        facts.put("currentBonus", currentBonus + "~" + round(Double.parseDouble(currentPrize) * 5 - money));
                        facts.put("currentPrize", currentPrize + "~" + round(Double.parseDouble(currentPrize) * 5));
                    } else if (poschooseNum == 4 && formatCodes.size() >= 4) {
                        facts.put("currentBonus", currentBonus + "~" + round(Double.parseDouble(currentPrize) * 6 - money));
                        facts.put("currentPrize", currentPrize + "~" + round(Double.parseDouble(currentPrize) * 6));
                    } else if (poschooseNum == 5 && formatCodes.size() == 2) {
                        facts.put("currentBonus", currentBonus + "~" + round(Double.parseDouble(currentPrize) * 6 - money));
                        facts.put("currentPrize", currentPrize + "~" + round(Double.parseDouble(currentPrize) * 6));
                    } else if (poschooseNum == 5 && formatCodes.size() == 3) {
                        facts.put("currentBonus", currentBonus + "~" + round(Double.parseDouble(currentPrize) * 8 - money));
                        facts.put("currentPrize", currentPrize + "~" + round(Double.parseDouble(currentPrize) * 8));
                    } else if (poschooseNum == 5 && formatCodes.size() == 4) {
                        facts.put("currentBonus", currentBonus + "~" + round(Double.parseDouble(currentPrize) * 9 - money));
                        facts.put("currentPrize", currentPrize + "~" + round(Double.parseDouble(currentPrize) * 9));
                    } else if (poschooseNum == 5 && formatCodes.size() >= 5) {
                        facts.put("currentBonus", currentBonus + "~" + round(Double.parseDouble(currentPrize) * 10 - money));
                        facts.put("currentPrize", currentPrize + "~" + round(Double.parseDouble(currentPrize) * 10));
                    }
                    break;
                case "任选二组选和值":
                    if (poschooseNum == 3 && ((List<String>) formatCodes.get(0)).size() == 1) {
                        facts.put("currentBonus", currentBonus + "~" + round(Double.parseDouble(currentPrize) + Double.parseDouble(currentBonus)));
                        facts.put("currentPrize", currentPrize + "~" + round(Double.parseDouble(currentPrize) * 2));
                    } else if (poschooseNum == 3 && ((List<String>) formatCodes.get(0)).size() >= 2) {
                        facts.put("currentBonus", currentBonus + "~" + round(Double.parseDouble(currentPrize) * 3 - money));
                        facts.put("currentPrize", currentPrize + "~" + round(Double.parseDouble(currentPrize) * 3));
                    } else if (poschooseNum == 4 && ((List<String>) formatCodes.get(0)).size() == 1) {
                        facts.put("currentBonus", currentBonus + "~" + round(Double.parseDouble(currentPrize) * 4 - money));
                        facts.put("currentPrize", currentPrize + "~" + round(Double.parseDouble(currentPrize) * 4));
                    } else if (poschooseNum == 4 && ((List<String>) formatCodes.get(0)).size() == 2) {
                        facts.put("currentBonus", currentBonus + "~" + round(Double.parseDouble(currentPrize) * 5 - money));
                        facts.put("currentPrize", currentPrize + "~" + round(Double.parseDouble(currentPrize) * 5));
                    } else if (poschooseNum == 4 && ((List<String>) formatCodes.get(0)).size() >= 3) {
                        facts.put("currentBonus", currentBonus + "~" + round(Double.parseDouble(currentPrize) * 6 - money));
                        facts.put("currentPrize", currentPrize + "~" + round(Double.parseDouble(currentPrize) * 6));
                    } else if (poschooseNum == 5 && ((List<String>) formatCodes.get(0)).size() == 1) {
                        facts.put("currentBonus", currentBonus + "~" + round(Double.parseDouble(currentPrize) * 5 - money));
                        facts.put("currentPrize", currentPrize + "~" + round(Double.parseDouble(currentPrize) * 5));
                    } else if (poschooseNum == 5 && ((List<String>) formatCodes.get(0)).size() >= 3 && ((List<String>) ((List<String>) formatCodes.get(0))).size() <= 6) {
                        facts.put("currentBonus", currentBonus + "~" + round(Double.parseDouble(currentPrize) * 9 - money));
                        facts.put("currentPrize", currentPrize + "~" + round(Double.parseDouble(currentPrize) * 9));
                    } else if (poschooseNum == 5 && ((List<String>) formatCodes.get(0)).size() >= 7) {
                        facts.put("currentBonus", currentBonus + "~" + round(Double.parseDouble(currentPrize) * 10 - money));
                        facts.put("currentPrize", currentPrize + "~" + round(Double.parseDouble(currentPrize) * 10));
                    }
                    break;
                case "任选三直选复式":
                case "任选三直选和值":
                    if (poschooseNum == 4) {
                        facts.put("currentBonus", currentBonus + "~" + round(Double.parseDouble(currentPrize) * 3 + Double.parseDouble(currentBonus)));
                        facts.put("currentPrize", currentPrize + "~" + round(Double.parseDouble(currentPrize) * 4));
                    } else if (poschooseNum == 5) {
                        facts.put("currentBonus", currentBonus + "~" + round(Double.parseDouble(currentPrize) * 8 + Double.parseDouble(currentBonus)));
                        facts.put("currentPrize", currentPrize + "~" + round(Double.parseDouble(currentPrize) * 9));
                    }
                    break;
                case "任选三组三":
                    if (poschooseNum == 4) {
                        facts.put("currentBonus", currentBonus + "~" + round(Double.parseDouble(currentPrize) * 3 + Double.parseDouble(currentBonus)));
                        facts.put("currentPrize", currentPrize + "~" + round(Double.parseDouble(currentPrize) * 4));
                    } else if (poschooseNum == 5) {
                        facts.put("currentBonus", currentBonus + "~" + round(Double.parseDouble(currentPrize) * 8 + Double.parseDouble(currentBonus)));
                        facts.put("currentPrize", currentPrize + "~" + round(Double.parseDouble(currentPrize) * 9));
                    }
                    break;
                case "任选三组六":
                    if (poschooseNum == 4 && ((List<String>) formatCodes.get(0)).size() == 3) {
                        facts.put("currentBonus", currentBonus + "~" + round(Double.parseDouble(currentPrize) + Double.parseDouble(currentBonus)));
                        facts.put("currentPrize", currentPrize + "~" + round(Double.parseDouble(currentPrize) * 2));
                    } else if (poschooseNum == 4 && ((List<String>) formatCodes.get(0)).size() >= 4) {
                        facts.put("currentBonus", currentBonus + "~" + round(Double.parseDouble(currentPrize) * 4 - money));
                        facts.put("currentPrize", currentPrize + "~" + round(Double.parseDouble(currentPrize) * 4));
                    } else if (poschooseNum == 5 && ((List<String>) formatCodes.get(0)).size() == 3) {
                        facts.put("currentBonus", currentBonus + "~" + round(Double.parseDouble(currentPrize) * 3 - money));
                        facts.put("currentPrize", currentPrize + "~" + round(Double.parseDouble(currentPrize) * 4));
                    } else if (poschooseNum == 5 && ((List<String>) formatCodes.get(0)).size() == 4) {
                        facts.put("currentBonus", currentBonus + "~" + round(Double.parseDouble(currentPrize) * 7 - money));
                        facts.put("currentPrize", currentPrize + "~" + round(Double.parseDouble(currentPrize) * 7));
                    } else if (poschooseNum == 5 && ((List<String>) formatCodes.get(0)).size() >= 5) {
                        facts.put("currentBonus", currentBonus + "~" + round(Double.parseDouble(currentPrize) * 10 - money));
                        facts.put("currentPrize", currentPrize + "~" + round(Double.parseDouble(currentPrize) * 10));
                    }
                    break;
                case "任选三组选和值":
                    if (poschooseNum == 4 && ((List<String>) formatCodes.get(0)).size() == 1) {
                        facts.put("currentBonus", Double.parseDouble(currentBonus.split("~")[0] + "~" + round(Double.parseDouble(currentPrize.split("~")[1]) * 3 - money)));
                        facts.put("currentPrize", currentPrize.split("~")[0] + "~" + round(Double.parseDouble(currentPrize.split("~")[1]) * 3));
                    } else if (poschooseNum == 4 && ((List<String>) formatCodes.get(0)).size() >= 2) {
                        facts.put("currentBonus", Double.parseDouble(currentBonus.split("~")[0] + "~" + round(Double.parseDouble(currentPrize.split("~")[1]) * 4 - money)));
                        facts.put("currentPrize", currentPrize.split("~")[0] + "~" + round(Double.parseDouble(currentPrize.split("~")[1]) * 4));
                    } else if (poschooseNum == 5 && ((List<String>) formatCodes.get(0)).size() == 1) {
                        facts.put("currentBonus", Double.parseDouble(currentBonus.split("~")[0] + "~" + round(Double.parseDouble(currentPrize.split("~")[1]) * 6 - money)));
                        facts.put("currentPrize", currentPrize.split("~")[0] + "~" + round(Double.parseDouble(currentPrize.split("~")[1]) * 6));
                    } else if (poschooseNum == 5 && ((List<String>) formatCodes.get(0)).size() == 2) {
                        facts.put("currentBonus", Double.parseDouble(currentBonus.split("~")[0] + "~" + round(Double.parseDouble(currentPrize.split("~")[1]) * 8 - money)));
                        facts.put("currentPrize", currentPrize.split("~")[0] + "~" + round(Double.parseDouble(currentPrize.split("~")[1]) * 8));
                    } else if (poschooseNum == 5 && ((List<String>) formatCodes.get(0)).size() >= 3) {
                        facts.put("currentBonus", Double.parseDouble(currentBonus.split("~")[0] + "~" + round(Double.parseDouble(currentPrize.split("~")[1]) * 9 - money)));
                        facts.put("currentPrize", currentPrize.split("~")[0] + "~" + round(Double.parseDouble(currentPrize.split("~")[1]) * 9));
                    }
                    break;
                case "任选四直选复式":
                    if (poschooseNum == 5) {
                        facts.put("currentBonus", currentBonus + "~" + round(Double.parseDouble(currentPrize) * 4 + Double.parseDouble(currentBonus)));
                        facts.put("currentPrize", currentPrize + "~" + round(Double.parseDouble(currentPrize) * 5));
                    }
                    break;
                case "任选四组选24":
                case "任选四组选12":
                    if (poschooseNum == 5) {
                        facts.put("currentBonus", currentBonus + "~" + round(Double.parseDouble(currentPrize) + Double.parseDouble(currentBonus)));
                        facts.put("currentPrize", currentPrize + "~" + round(Double.parseDouble(currentPrize) * 2));
                    }
                    break;
                case "任选四组选6":
                    if (poschooseNum == 5) {
                        facts.put("currentBonus", currentBonus + "~" + round(Double.parseDouble(currentPrize) * 2 + Double.parseDouble(currentBonus)));
                        facts.put("currentPrize", currentPrize + "~" + round(Double.parseDouble(currentPrize) * 3));
                    }
                    break;
                case "任选四组选4":
                    if (poschooseNum == 5) {
                        facts.put("currentBonus", currentBonus + "~" + round(Double.parseDouble(currentPrize) * 3 + Double.parseDouble(currentBonus)));
                        facts.put("currentPrize", currentPrize + "~" + round(Double.parseDouble(currentPrize) * 4));
                    }
                    break;
                case "任选二直选单式":
                case "任选二组选单式":
                case "任选三直选单式":
                case "任选三混合组选":
                case "任选四直选单式":
                    facts.put("currentBonus", "undefined");
                    facts.put("currentPrize", "undefined");
                    break;
            }
        } catch (Exception e) {
            CfLog.e(e.getMessage());
        }
    }

    private int calculatePositionChosenNumber(Facts facts) {
        Map<String, List<Boolean>> bet = facts.get("bet");
        List<Boolean> poschoose = bet.get("poschoose");
        List<Object> formatCodes = facts.get("formatCodes");

        if (poschoose != null) {
            return (int) poschoose.stream().filter(item -> item).count();
        } else {
            return (int) formatCodes.stream().count();
        }
    }

    private double round(double value) {
        return Math.round(value * 10000.0) / 10000.0;
    }
}

