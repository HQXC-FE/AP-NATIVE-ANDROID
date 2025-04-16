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

@Rule(name = "SoloIdentifierRule", description = "标识单挑注单规则")
public class SoloIdentifierRule {

    @Priority
    public int getPriority() {
        return -8900;
    }

    @Condition
    public boolean when(Facts facts) {
        String lotteryType = facts.get("lotteryType");
        List<String> ruleSuite = facts.get("ruleSuite");
        return "ssc".equals(lotteryType) && !ruleSuite.contains("any-solo");
    }

    @Action
    public void then(Facts facts) {
        try {
            int num = 0;
            Map<String, String> currentCategory = facts.get("currentCategory");
            Map<String, String> currentMethod = facts.get("currentMethod");
            String methodName = currentCategory.get("name") + currentMethod.get("name");
            String lotteryAlias = currentCategory.get("flag");

            Map<String, List<Boolean>> bet = facts.get("bet");
            List<Boolean> posChoose = bet.get("poschoose");
            Object formatCodes = facts.get("formatCodes");

            boolean solo = false;
            int positionLength = 0;

            if (posChoose != null) {
                for (Boolean item : posChoose) {
                    if (item) {
                        positionLength++;
                    }
                }
            } else if (formatCodes != null) {
                if (formatCodes instanceof List) {
                    num = facts.get("num");
                    List<Object> formatCodesList = (List<Object>) formatCodes;
                    for (Object code : formatCodesList) {
                        if (code instanceof String) {
                            positionLength++;
                        } else {
                            if (((List<String>) code).size() > 0) {
                                positionLength++;
                            }
                        }
                    }
                } else if (formatCodes instanceof String) {

                }
            }

            if (Matchers.sscAlias.contains(lotteryAlias)) {
                switch (methodName) {
                    case "五星复式":
                    case "五星单式":
                        solo = num <= 1000;
                        break;
                    case "五星组合":
                        solo = num <= 5000;
                        break;
                    case "四星复式":
                    case "四星单式":
                        solo = num <= 100;
                        break;
                    case "四星组合":
                        solo = num <= 400;
                        break;
                    case "四星组选24":
                        solo = num <= 5;
                        break;
                    case "前三码单式":
                    case "中三码单式":
                    case "后三码单式":
                    case "前三码直选单式":
                    case "中三码直选单式":
                    case "后三码直选单式":
                    case "前三码直选和值":
                    case "中三码直选和值":
                    case "后三码直选和值":
                    case "前三码复式":
                    case "中三码复式":
                    case "后三码复式":
                    case "前三码直选复式":
                    case "中三码直选复式":
                    case "后三码直选复式":
                        solo = num <= 10;
                        break;
                    case "前三码组三":
                    case "中三码组三":
                    case "后三码组三":
                    case "前三码组六":
                    case "中三码组六":
                    case "后三码组六":
                    case "前三码混合组选":
                    case "中三码混合组选":
                    case "后三码混合组选":
                    case "前三码混合":
                    case "中三码混合":
                    case "后三码混合":
                        solo = num <= 1;
                        break;
                    case "前三码组选和值":
                    case "中三码组选和值":
                    case "后三码组选和值":
                        solo = num <= 2;
                        break;
                    case "二码后二直选(复式)":
                    case "二码后二组选(复式)":
                    case "二码前二直选(复式)":
                    case "二码前二组选(复式)":
                    case "二码中二直选(复式)":
                    case "二码后二直选(单式)":
                    case "二码后二组选(单式)":
                    case "二码中二直选(单式)":
                    case "二码中二组选(单式)":
                    case "二码前二直选(单式)":
                    case "二码前二组选(单式)":
                    case "二码后二直选复式":
                    case "二码后二组选复式":
                    case "二码前二直选复式":
                    case "二码前二组选复式":
                    case "二码中二直选复式":
                    case "二码中二组选复式":
                    case "二码后二直选单式":
                    case "二码后二组选单式":
                    case "二码中二直选单式":
                    case "二码中二组选单式":
                    case "二码前二直选单式":
                    case "二码前二组选单式":
                    case "二码后二直选和值":
                    case "二码前二直选和值":
                    case "二码后二组选和值":
                    case "二码前二组选和值":
                        solo = num <= 1;
                        break;
                    case "趣味三星报喜":
                        solo = num <= 1;
                        break;
                    case "趣味四季发财":
                        solo = num <= 10;
                        break;
                    case "任选二直选复式":
                    case "任选二直选单式":
                    case "任选二直选和值":
                    case "任选二组选复式":
                    case "任选二组选单式":
                    case "任选二组选和值":
                        solo = (positionLength == 2 && num <= 1) ||
                                (positionLength == 3 && num <= 3) ||
                                (positionLength == 4 && num <= 6) ||
                                (positionLength == 5 && num <= 10);
                        break;
                    case "任选三直选复式":
                    case "任选三直选单式":
                    case "任选三直选和值":
                        solo = (positionLength == 3 && num <= 10) ||
                                (positionLength == 4 && num <= 40) ||
                                (positionLength == 5 && num <= 100);
                        break;
                    case "任选三组三":
                    case "任选三组六":
                    case "任选三混合组选":
                    case "任选三组选和值":
                        solo = (positionLength == 3 && num <= 2) ||
                                (positionLength == 4 && num <= 8) ||
                                (positionLength == 5 && num <= 20);
                        break;
                    case "任选四直选复式":
                    case "任选四直选单式":
                        solo = (positionLength == 4 && num <= 100) ||
                                (positionLength == 5 && num <= 500);
                        break;
                    case "任选四组选24":
                        solo = (positionLength == 4 && num <= 5) ||
                                (positionLength == 5 && num <= 25);
                        break;
                    //例外规则
                    case "五星组选120":
                        solo = num <= 8;
                        break;
                    case "五星组选60":
                        solo = num <= 16;
                        break;
                    case "五星组选30":
                        solo = num <= 33;
                        break;
                    case "五星组选20":
                        solo = num <= 50;
                        break;
                    case "五星组选5":
                    case "五星组选10":
                        solo = num <= 90;
                        break;
                    case "四星组选12":
                        solo = num <= 9;
                        break;
                    case "四星组选6":
                        solo = num <= 24;
                        break;
                    case "四星组选4":
                        solo = num <= 25;
                        break;
                    case "任选四组选12":
                        solo = (positionLength == 4 && num <= 9) ||
                                (positionLength == 5 && num <= 45);
                        break;
                    case "任选四组选4":
                    case "任选四组选6":
                        solo = (positionLength == 4 && num <= 25) ||
                                (positionLength == 5 && num <= 125);
                        break;
                    case "龙虎斗龙虎斗":
                    case "龙虎斗玄麟斗":
                    case "龙虎斗万个":
                    case "龙虎斗万千":
                    case "龙虎斗万百":
                    case "龙虎斗万十":
                    case "龙虎斗千百":
                    case "龙虎斗千十":
                    case "龙虎斗千个":
                    case "龙虎斗百十":
                    case "龙虎斗百个":
                    case "龙虎斗十个":
                        List<List<String>> betCodes = (List<List<String>>) facts.get("betCodes");
                        if (num == 1 && betCodes != null && "和".equals(betCodes.get(0).get(0))) {
                            solo = true;
                        }
                        break;
                }
            }

            if (Matchers._11x5Alias.contains(lotteryAlias)) {
                switch (methodName) {
                    case "三码前三直选复式":
                    case "三码前三直选单式":
                        solo = num <= 9;
                        break;
                    case "三码前三组选复式":
                    case "三码前三组选单式":
                    case "二码前二直选复式":
                    case "二码前二直选单式":
                    case "二码前二组选复式":
                    case "二码前二组选单式":
                    case "任选复式三中三":
                    case "任选单式三中三":
                    case "任选复式八中五":
                    case "任选单式八中五":
                        solo = num <= 1;
                        break;
                    case "任选复式四中四":
                    case "任选单式四中四":
                    case "任选复式七中五":
                    case "任选单式七中五":
                        solo = num <= 3;
                        break;
                    case "任选复式五中五":
                    case "任选单式五中五":
                    case "任选复式六中五":
                    case "任选单式六中五":
                        solo = num <= 4;
                        break;
                }
            }

            if (Matchers._3dAlias.contains(lotteryAlias)) {
                switch (methodName) {
                    case "三码复式":
                    case "三码直选和值":
                    case "三码单式":
                        solo = num <= 10;
                        break;
                    case "三码组三":
                    case "三码组六":
                    case "三码混合":
                    case "二码后二直选复式":
                    case "二码后二直选单式":
                    case "二码前二直选复式":
                    case "二码前二直选单式":
                    case "二码后二组选复式":
                    case "二码后二组选单式":
                    case "二码前二组选复式":
                    case "二码前二组选单式":
                        solo = num <= 1;
                        break;
                    case "三码组选和值":
                        solo = num <= 2;
                        break;
                }
            }

            if (Matchers.pk10Alias.contains(lotteryAlias)) {
                switch (methodName) {
                    case "前六复式":
                    case "前六单式":
                        solo = num <= 1500;
                        break;
                    case "前五复式":
                    case "前五单式":
                        solo = num <= 300;
                        break;
                    case "前四复式":
                    case "前四单式":
                        solo = num <= 50;
                        break;
                    case "前三复式":
                    case "前三单式":
                        solo = num <= 7;
                        break;
                    case "前二复式":
                    case "前二单式":
                        solo = num <= 1;
                        break;
                }
            }

            if (Matchers.jssmAlias.contains(lotteryAlias)) {
                switch (methodName) {
                    case "六连环复式":
                    case "六连环单式":
                        solo = num <= 1500;
                        break;
                    case "五连环复式":
                    case "五连环单式":
                        solo = num <= 300;
                        break;
                    case "四连环复式":
                    case "四连环单式":
                        solo = num <= 50;
                        break;
                    case "三连环复式":
                    case "三连环单式":
                        solo = num <= 7;
                        break;
                    case "连赢复式":
                    case "连赢单式":
                        solo = num <= 1;
                        break;
                }
            }

            facts.put("solo", solo);
        } catch (Exception e) {
            CfLog.e(e.getMessage());
        }
    }
}

