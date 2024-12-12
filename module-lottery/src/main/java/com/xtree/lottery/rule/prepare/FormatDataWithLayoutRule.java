package com.xtree.lottery.rule.prepare;

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
        Map<String, Object> currentMethod = facts.get("currentMethod");
        Map<String, Object> selectArea = (Map<String, Object>) currentMethod.get("selectarea");
        List<Map<String, Object>> layout = (List<Map<String, Object>>) selectArea.get("layout");
        Map<String, List<List<String>>> bet = facts.get("bet");
        List<List<String>> betCodes = bet.get("codes");
        List<List<String>> formatCodes = new ArrayList<>();

        // 遍历布局，根据 place 将代码分组到 formatCodes 中
        for (int index = 0; index < layout.size(); index++) {
            Map<String, Object> item = layout.get(index);
            Integer place = Integer.parseInt((String) item.get("place"));

            // 确保 place 对应的列表已初始化
            while (formatCodes.size() <= place) {
                formatCodes.add(new ArrayList<>());
            }

            // 将 bet.codes[index] 的内容追加到对应 place 的列表中
            formatCodes.get(place).addAll(betCodes.get(index));
        }
        // 将格式化后的数据存入 facts
        facts.put("formatCodes", formatCodes);
    }
}