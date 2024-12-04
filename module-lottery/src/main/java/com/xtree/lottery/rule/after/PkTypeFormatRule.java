package com.xtree.lottery.rule.after;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Rule(name = "Pk Type Format", description = "竞速PK类型格式化最后显示号码")
public class PkTypeFormatRule {

    @Priority
    public int getPriority() {
        return 9000; // 设置规则优先级
    }

    @Condition
    public boolean when(Facts facts) {
        String lotteryType = (String) facts.get("lotteryType");
        String selectAreaType = (String) facts.get("currentMethod.selectarea.type");
        return "ssc".equals(lotteryType) && "pk".equals(selectAreaType);
    }

    @Action
    public void then(Facts facts) {
        List<List<String>> formatCodes = (List<List<String>>) facts.get("formatCodes");
        List<String> displayCodes = new ArrayList<>();

        for (List<String> item : formatCodes) {
            if (item.size() == 2) {
                displayCodes.add(String.join("vs", item));
            } else {
                displayCodes.add("X");
            }
        }

        String displayCodesStr = String.join(",", displayCodes);
        facts.put("displayCodes", displayCodesStr);

        // 去掉空数组
        List<List<String>> filteredFormatCodes = formatCodes.stream()
                .filter(item -> item.size() == 2)
                .collect(Collectors.toList());
        facts.put("formatCodes", filteredFormatCodes);

        // 完成后触发下一个规则
        facts.get("rules");
    }
}