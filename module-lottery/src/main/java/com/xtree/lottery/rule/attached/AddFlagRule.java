package com.xtree.lottery.rule.attached;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Rule(name = "Add Extra Flag", description = "添加额外标记")
public class AddFlagRule {

    @Priority
    public int getPriority() {
        return -59800;
    }

    @Condition
    public boolean when(Facts facts) {
        List<String> ruleSuite = facts.get("ruleSuite");
        return ruleSuite.stream().anyMatch(item -> item.matches("^add-flag-.*$"));
    }

    @Action
    public void then(Facts facts) {
        List<String> ruleSuite = facts.get("ruleSuite");
        List<String> flags = ruleSuite.stream()
                .filter(item -> item.matches("^add-flag-.*$"))
                .map(item -> item.split("add-flag-")[1])
                .toList();

        Map<String, List<String>> attached;

        if (facts.get("attached") != null) {
            attached = facts.get("attached");
        } else {
            attached = new HashMap<>();
        }
        attached.put("flags", flags);

        facts.put("attached", attached);
    }
}
