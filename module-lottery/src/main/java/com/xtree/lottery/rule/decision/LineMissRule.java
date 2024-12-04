package com.xtree.lottery.rule.decision;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;

import java.util.List;
import java.util.Map;

@Rule(name = "LineMissRule", description = "串号，不中")
public class LineMissRule {

    @Priority
    public int getPriority() {
        return 17800;
    }

    @Condition
    public boolean when(Map<String, Object> facts) {
        List<String> ruleSuite = (List<String>) facts.get("ruleSuite");
        return ruleSuite.stream().anyMatch(item -> item.matches("^line-miss-.*$"));
    }

    @Action
    public void then(Map<String, Object> facts) {
        List<String> ruleSuite = (List<String>) facts.get("ruleSuite");
        List<List<String>> formatCodes = (List<List<String>>) facts.get("formatCodes");

        String matchedRule = ruleSuite.stream().filter(item -> item.matches("^line-miss-.*$")).findFirst().orElse("");
        int limitNum = Integer.parseInt(matchedRule.split("line-miss-")[1]);

        int num = formatCodes.get(0).size() < limitNum ? 0 : 1;

        facts.put("num", num);
    }
}
