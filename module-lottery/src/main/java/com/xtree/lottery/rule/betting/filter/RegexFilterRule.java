package com.xtree.lottery.rule.betting.filter;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

//Todo 此方案的 scope 与 number 是有问题的 他属于 attached 应该要修正

@Rule(name = "Single Regex Filter", description = "Filter entries by regex")
public class RegexFilterRule {

    @Priority
    public int getPriority() {
        return -48000;
    }

    @Condition
    public boolean when(Facts facts) {
        List<String> ruleSuite = facts.get("ruleSuite");
        return ruleSuite.contains("filter-input-regex");
    }

    @Action
    public void then(Facts facts) {
        try {
            List<String> formatCodes = facts.get("formatCodes");
            String scope = facts.get("scope");
            scope = (scope != null) ? scope : "0-9";
            int number = facts.get("number");

            Pattern regex = Pattern.compile("^[" + scope + "s]{" + number + "}$");

            List<String> errorCodes = new ArrayList<>();
            List<String> currentCodes = formatCodes.stream()
                    .filter(item -> {
                        boolean matches = regex.matcher(item).matches();
                        if (!matches) errorCodes.add(item);
                        return matches;
                    })
                    .collect(Collectors.toList());

            if (!errorCodes.isEmpty()) {
                List<String> message = facts.get("message");
                message.add("以下号码错误，已进行自动过滤");
                message.add(String.join(",", errorCodes));
                facts.put("message", message);
            }

            facts.put("formatCodes", currentCodes);
        } catch (Exception e) {
            CfLog.e(e.getMessage());
        }
    }
}
