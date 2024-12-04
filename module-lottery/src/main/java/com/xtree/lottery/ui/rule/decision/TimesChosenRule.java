package com.xtree.lottery.ui.rule.decision;

import com.xtree.lottery.ui.rule.Matchers;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Rule(name = "TimesChosenRule", description = "倍数算法，一号N注")
public class TimesChosenRule {

    @Priority
    public int getPriority() {
        return 19400;
    }

    @Condition
    public boolean when(Map<String, Object> facts) {
        List<String> ruleSuite = (List<String>) facts.get("ruleSuite");
        return ruleSuite.contains("times-chosen");
    }

    @Action
    public void then(Map<String, Object> facts) {
        Integer num = (Integer) facts.get("num");
        Map<String, Object> attached = (Map<String, Object>) facts.get("attached");
        List<List<String>> formatCodes = (List<List<String>>) facts.get("formatCodes");
        String categoryFlag = (String) ((Map<String, Object>) facts.get("currentCategory")).get("flag");
        String matchName = ((Map<String, Object>) facts.get("currentCategory")).get("name") + "-" + ((Map<String, Object>) facts.get("currentMethod")).get("groupName");

        int times = (int) attached.getOrDefault("number", 1);

        // 如果已存在 num 值，直接进行倍数运算
        if (num != null) {
            if (isSpecialCategory(categoryFlag, matchName)) {
                times = getSpecialTimes(matchName);
            }
            facts.put("num", num * times);
            return;
        }

        // 计算倍数逻辑
        int finalTimes = times;
        int totalSum = formatCodes.stream()
                .filter(code -> !code.isEmpty())
                .mapToInt(code -> code.size() * finalTimes)
                .sum();

        facts.put("num", totalSum);
    }

    private boolean isSpecialCategory(String categoryFlag, String matchName) {
        List<String> specialFlags = Arrays.asList("vnmMidSouAlias", "vnmFastAlias");  // 替换为实际的标识
        return specialFlags.contains(categoryFlag) && Matchers.mapMatchNameToNumList.containsKey(matchName);
    }

    private int getSpecialTimes(String matchName) {
        // 替换为实际的映射关系
        Map<String, Integer> mapMatchNameToNumList = new HashMap<>();
        mapMatchNameToNumList.put("Example-Match", 54);
        return mapMatchNameToNumList.getOrDefault(matchName, 1);
    }
}
