package com.xtree.lottery.rule.betting.filter;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Rule(name = "Single Duplicate Removal", description = "Remove duplicate entries")
public class DuplicateRemovalRule {

    @Priority
    public int getPriority() {
        return -50000;
    }

    @Condition
    public boolean when(Facts facts) {
        List<String> ruleSuite = facts.get("ruleSuite");
        return ruleSuite.contains("filter-input-unqiue");
    }

    @Action
    public void then(Facts facts) {
        try {
            List<String> formatCodes = facts.get("formatCodes");
            List<String> realCode = Arrays.stream(String.join(",", formatCodes).split("[,; ]"))
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
            List<String> uniqueCode = realCode.stream().distinct().collect(Collectors.toList());

            if (realCode.size() != uniqueCode.size()) {
                List<String> message = facts.get("message");
                message.add("已经自动去重");
                facts.put("message", message);
            }

            facts.put("formatCodes", uniqueCode);
        } catch (Exception e) {
            CfLog.e(e.getMessage());
        }
    }
}
