package com.xtree.lottery.rule.attached;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;

import java.util.List;
import java.util.Map;

@Rule(name = "Add Extra Position Number", description = "添加额外位置参")
public class AddPosNumRule {

    @Priority
    public int getPriority() {
        return 59900;
    }

    @Condition
    public boolean when(Map<String, Object> facts) {
        List<String> ruleSuite = (List<String>) facts.get("ruleSuite");
        return ruleSuite.stream().anyMatch(item -> item.matches("^add-posnum-.*$"));
    }

    @Action
    public void then(Map<String, Object> facts) {
        List<String> ruleSuite = (List<String>) facts.get("ruleSuite");
        int posnum = Integer.parseInt(
                ruleSuite.stream()
                        .filter(item -> item.matches("^add-posnum-.*$"))
                        .findFirst()
                        .get()
                        .split("add-posnum-")[1]
        );
        ((Map<String, Object>) facts.get("attached")).put("posnum", posnum);
        facts.put("attached", facts.get("attached"));
    }
}
