package com.xtree.lottery.ui.rule.attached;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Rule(name = "Add Extra Flag", description = "添加额外标记")
public class AddFlagRule {

    @Priority
    public int getPriority() {
        return 59800;
    }

    @Condition
    public boolean when(Map<String, Object> facts) {
        List<String> ruleSuite = (List<String>) facts.get("ruleSuite");
        return ruleSuite.stream().anyMatch(item -> item.matches("^add-flag-.*$"));
    }

    @Action
    public void then(Map<String, Object> facts) {
        List<String> ruleSuite = (List<String>) facts.get("ruleSuite");
        List<String> flags = ruleSuite.stream()
                .filter(item -> item.matches("^add-flag-.*$"))
                .map(item -> item.split("add-flag-")[1])
                .collect(Collectors.toList());

        ((Map<String, Object>) facts.get("attached")).put("flag", flags);
        facts.put("attached", facts.get("attached"));
    }
}
