package com.xtree.lottery.rule.betting.decision;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.List;

@Rule(name = "LineMissRule", description = "串号，不中")
public class LineMissRule {

    @Priority
    public int getPriority() {
        return -17800;
    }

    @Condition
    public boolean when(Facts facts) {
        List<String> ruleSuite = facts.get("ruleSuite");
        return ruleSuite.stream().anyMatch(item -> item.matches("^line-miss-.*$"));
    }

    @Action
    public void then(Facts facts) {
        try {
            List<String> ruleSuite = facts.get("ruleSuite");
            List<List<String>> formatCodes = facts.get("formatCodes");

            String matchedRule = ruleSuite.stream().filter(item -> item.matches("^line-miss-.*$")).findFirst().orElse("");
            int limitNum = Integer.parseInt(matchedRule.split("line-miss-")[1]);

            int num = formatCodes.get(0).size() < limitNum ? 0 : 1;

            facts.put("num", num);
        } catch (Exception e) {
            CfLog.e(e.getMessage());
        }
    }
}
