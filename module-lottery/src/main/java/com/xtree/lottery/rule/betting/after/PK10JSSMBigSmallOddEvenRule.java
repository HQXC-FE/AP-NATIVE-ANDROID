package com.xtree.lottery.rule.betting.after;

import com.xtree.base.utils.CfLog;
import com.xtree.lottery.rule.betting.Matchers;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Rule(name = "PK10JSSMBigSmallOddEven", description = "PK10, JSSM 大小单双规则")
public class PK10JSSMBigSmallOddEvenRule {

    @Priority
    public int getPriority() {
        return -8793;
    }

    @Condition
    public boolean when(Facts facts) {
        String currentCategoryName = (String) ((Map<String, Object>) facts.get("currentCategory")).get("name");
        String currentCategoryFlag = (String) ((Map<String, Object>) facts.get("currentCategory")).get("flag");

        return facts.get("currentPrizeModes") != null &&
                currentCategoryName.equals("大小单双") &&
                (Matchers.pk10Alias.contains(currentCategoryFlag) ||
                        Matchers.jssmAlias.contains(currentCategoryFlag) ||
                        Matchers.sscAlias.contains(currentCategoryFlag));
    }

    @Action
    public void then(Facts facts) {
        try {
            String methodName = ((Map<String, String>) facts.get("currentCategory")).get("name") + ((Map<String, String>) facts.get("currentMethod")).get("name");
            List<Object> formatCodes = facts.get("formatCodes");
            double currentPrize = Double.parseDouble(facts.get("currentPrize"));
            double currentBonus = Double.parseDouble(facts.get("currentBonus"));

            switch (methodName) {
                case "大小单双总和":
                case "大小单双冠军":
                case "大小单双亚军":
                case "大小单双季军":
                case "大小单双前三和值":
                case "大小单双中三和值":
                case "大小单双后三和值": {
                    String codeJoin = (String) formatCodes.get(0);
                    if (((String) formatCodes.get(0)).length() > 1 && !List.of("大小", "小大", "单双", "双单").contains(codeJoin)) {
                        facts.put("currentBonus", currentBonus + "~" + round(currentPrize + currentBonus, 4));
                        facts.put("currentPrize", currentPrize + "~" + round(currentPrize * 2, 4));
                    }
                    break;
                }
                case "大小单双后二":
                case "大小单双前二": {
                    calculateBonusAndPrize(formatCodes, currentPrize, currentBonus, facts);
                    break;
                }
                case "大小单双后三":
                case "大小单双前三": {
                    calculateBonusAndPrize(formatCodes, currentPrize, currentBonus, facts);
                    break;
                }
                case "大小单双前三大小个数":
                case "大小单双中三大小个数":
                case "大小单双后三大小个数":
                    List<String> codeJoinCol1 = formatCodes.stream()
                            .map(obj -> (String) obj)
                            .collect(Collectors.toList());
                    String codeJoin1 = String.join("", codeJoinCol1);
                    int num1 = facts.get("num");
                    if (num1 < 3 && List.of("全大", "全小", "全大全小").contains(codeJoin1)) {
                        facts.put("currentBonus", splitBonusOrPrize(currentBonus, 1));
                        facts.put("currentPrize", splitBonusOrPrize(currentPrize, 1));
                    }
                    if (num1 < 3 && List.of("2大1小", "1大2小", "1大2小2大1小").contains(codeJoin1)) {
                        facts.put("currentBonus", splitBonusOrPrize(currentBonus, 0));
                        facts.put("currentPrize", splitBonusOrPrize(currentPrize, 0));
                    }
                    break;
                case "大小单双前三单双个数":
                case "大小单双中三单双个数":
                case "大小单双后三单双个数": {
                    List<String> codeJoinCol2 = formatCodes.stream()
                            .map(obj -> (String) obj)
                            .collect(Collectors.toList());
                    String codeJoin2 = String.join("", codeJoinCol2);
                    int num2 = facts.get("num");
                    if (num2 < 3 && List.of("全单", "全双", "全单全双").contains(codeJoin2)) {
                        facts.put("currentBonus", splitBonusOrPrize(currentBonus, 1));
                        facts.put("currentPrize", splitBonusOrPrize(currentPrize, 1));
                    }
                    if (num2 < 3 && List.of("2单1双", "1单2双", "1单2双2单1双").contains(codeJoin2)) {
                        facts.put("currentBonus", splitBonusOrPrize(currentBonus, 0));
                        facts.put("currentPrize", splitBonusOrPrize(currentPrize, 0));
                    }
                    break;
                }
            }
        } catch (Exception e) {
            CfLog.e(e.getMessage());
        }
    }

    private void calculateBonusAndPrize(List<Object> formatCodes, double currentPrize, double currentBonus, Facts facts) {
        int prizeNum = calculatePrizeMultiplier((List<String>) formatCodes.get(0));
        int prizeNum2 = calculatePrizeMultiplier((List<String>) formatCodes.get(1));
        int prizeNum3 = formatCodes.size() > 2 ? calculatePrizeMultiplier((List<String>) formatCodes.get(2)) : 1;

        int totalMultiplier = prizeNum * prizeNum2 * prizeNum3;
        if (totalMultiplier > 1) {
            facts.put("currentBonus", currentBonus + "~" + round(currentPrize * (totalMultiplier - 1) + currentBonus, 4));
            facts.put("currentPrize", currentPrize + "~" + round(currentPrize * totalMultiplier, 4));
        }
    }

    private int calculatePrizeMultiplier(List<String> codes) {
        String codeJoin = String.join("", codes);
        if (codes.size() > 1 && !List.of("大小", "小大", "单双", "双单").contains(codeJoin)) {
            return 2;
        } else {
            return 1;
        }
    }

    private String splitBonusOrPrize(double value, int index) {
        String[] parts = String.valueOf(value).split("~");
        return parts.length > index ? parts[index] : String.valueOf(value);
    }

    private double round(double value, int precision) {
        double scale = Math.pow(10, precision);
        return Math.round(value * scale) / scale;
    }
}
