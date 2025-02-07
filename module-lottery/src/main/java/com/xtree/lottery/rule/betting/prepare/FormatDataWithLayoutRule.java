package com.xtree.lottery.rule.betting.prepare;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Rule(name = "Format Data with Layout", description = "Formats the codes based on the layout if lottery type is 'ssc'")
public class FormatDataWithLayoutRule {

    @Priority
    public int getPriority() {
        return -90000;
    }

    @Condition
    public boolean when(Facts facts) {
        String lotteryType = facts.get("lotteryType");
        Map<String, Object> currentMethod = facts.get("currentMethod");
        return "ssc".equals(lotteryType) && currentMethod.containsKey("selectarea")
                && currentMethod.get("selectarea") instanceof Map
                && ((Map<String, Object>) currentMethod.get("selectarea")).containsKey("layout");
    }

    @Action
    public void then(Facts facts) {
        try {
            Map<String, Object> currentMethod = facts.get("currentMethod");
            Map<String, Object> selectArea = (Map<String, Object>) currentMethod.get("selectarea");
            List<Map<String, Object>> layout = (List<Map<String, Object>>) selectArea.get("layout");
            Map<String, List<Object>> bet = facts.get("bet");
            List<Object> betCodes = bet.get("codes");
            List<Object> formatCodes = new ArrayList<>();

            // 遍历布局，根据 place 将代码分组到 formatCodes 中
            for (int index = 0; index < layout.size(); index++) {
//                Map<String, Object> item = layout.get(index);
//                Integer place = Integer.parseInt((String) item.get("place"));

                // 将 bet.codes[index] 的内容追加到对应 place 的列表中
                if (betCodes.get(0) instanceof String) {
                    formatCodes = betCodes;
                } else {
                    if (betCodes.size() > index) {
                        formatCodes.add(index, betCodes.get(index));
                    } else {
                        formatCodes.add(new ArrayList<>());
                    }

                }
            }
            // 将格式化后的数据存入 facts
            facts.put("formatCodes", formatCodes);
        } catch (Exception e) {
            CfLog.e(e.getMessage());
        }
    }
}