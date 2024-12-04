package com.xtree.lottery.rule.attached;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;

import java.util.List;
import java.util.Map;

@Rule(name = "Add Extra Number", description = "添加额外数字参")
public class AddNumRule {

    @Priority
    public int getPriority() {
        return 60000;
    }

    @Condition
    public boolean when(Map<String, Object> facts) {
        List<String> ruleSuite = (List<String>) facts.get("ruleSuite");
        return ruleSuite.stream().anyMatch(item -> item.matches("^add-num-.*$"));
    }

    @Action
    public void then(Map<String, Object> facts) {
        List<String> ruleSuite = (List<String>) facts.get("ruleSuite");
        int number = Integer.parseInt(
                ruleSuite.stream()
                        .filter(item -> item.matches("^add-num-.*$"))
                        .findFirst()
                        .get()
                        .split("add-num-")[1]
        );
        ((Map<String, Object>) facts.get("attached")).put("number", number);
        facts.put("attached", facts.get("attached"));
    }
}
