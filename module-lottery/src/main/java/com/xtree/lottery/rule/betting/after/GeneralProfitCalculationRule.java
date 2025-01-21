package com.xtree.lottery.rule.betting.after;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Rule(name = "GeneralProfitCalculationRule", description = "通用盈利计算规则，特殊玩法后续规则处理")
public class GeneralProfitCalculationRule {

    @Priority
    public int getPriority() {
        return -8798;
    }

    @Condition
    public boolean when(Facts facts) {
        return facts.get("currentPrizeModes") != null;
    }

    @Action
    public void then(Facts facts) {
        try {
            List<Map<String, Object>> currentPrizeModes = facts.get("currentPrizeModes");
            Map<String, Object> currentMethod = facts.get("currentMethod");
            Map<String, Object> mode = facts.get("mode");
            int times = facts.get("times");
            double money = facts.get("money");
            int omodel = facts.get("prize") != null ? Integer.parseInt(facts.get("prize").toString()) : 1;

            // 计算 arrPrizeModes
            List<Double> arrPrizeModes = currentPrizeModes.stream()
                    .filter(modeMap -> omodel == (int) modeMap.get("option"))
                    .flatMap(modeMap -> ((List<Double>) modeMap.get("value")).stream())
                    .map(value -> round(value * getRate(currentMethod, mode) * (2 / getBaseBetRate()) * times, 4))
                    .collect(Collectors.toList());

            // 设置 currentPrize
            String currentPrize = arrPrizeModes.size() > 1
                    ? formatRange(arrPrizeModes.get(0), arrPrizeModes.get(arrPrizeModes.size() - 1))
                    : String.format("%.4f", arrPrizeModes.get(0));
            facts.put("currentPrize", currentPrize);

            // 计算 minPrize
            if (currentPrizeModes.size() == 2 &&
                    currentPrizeModes.stream().map(pm -> ((List<?>) pm.get("value")).size()).reduce(0, Integer::sum) == 2) {
                double minPrize = round(
                        currentPrizeModes.stream()
                                .flatMap(pm -> ((List<Double>) pm.get("value")).stream())
                                .min(Double::compare)
                                .orElse(0.0) * getRate(currentMethod, mode) * times,
                        4);
                facts.put("minPrize", minPrize);
            }

            // 计算 arrBonus
            List<Double> arrBonus = currentPrizeModes.stream()
                    .filter(modeMap -> omodel == (int) modeMap.get("option"))
                    .flatMap(modeMap -> ((List<Double>) modeMap.get("value")).stream())
                    .map(value -> round(value * getRate(currentMethod, mode) * times * (2 / getBaseBetRate()) - money, 4))
                    .collect(Collectors.toList());

            // 设置 currentBonus
            String currentBonus = arrBonus.size() > 1
                    ? formatRange(arrBonus.get(0), arrBonus.get(arrBonus.size() - 1))
                    : String.format("%.4f", arrBonus.get(0));
            facts.put("currentBonus", currentBonus);
        } catch (Exception e) {
            CfLog.e(e.getMessage());
        }
    }

    private double getRate(Map<String, Object> currentMethod, Map<String, Object> mode) {
        return ((List<Map<String, Object>>) currentMethod.get("money_modes")).stream()
                .filter(m -> m.get("modeid").equals(mode.get("modeid")))
                .map(m -> Double.parseDouble((String) m.get("rate")))
                .findFirst()
                .orElse(1.0);
    }

    private double getBaseBetRate() {
        // 模拟 CONFIG.BASE_BET_RATE()，根据实际情况替换
        return 2.0;
    }

    private double round(double value, int places) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private String formatRange(double min, double max) {
        return String.format("%.4f~%.4f", min, max);
    }
}
