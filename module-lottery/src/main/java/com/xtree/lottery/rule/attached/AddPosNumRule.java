package com.xtree.lottery.rule.attached;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Rule(name = "Add Extra Position Number", description = "添加额外位置参")
public class AddPosNumRule {

    @Priority
    public int getPriority() {
        return 59900;
    }

    @Condition
    public boolean when(Facts facts) {
        List<String> ruleSuite = facts.get("ruleSuite");
        return ruleSuite.stream().anyMatch(item -> item.matches("^add-posnum-.*$"));
    }

    @Action
    public void then(Facts facts) {
        List<String> ruleSuite = facts.get("ruleSuite");
        Optional<String> match = ruleSuite.stream().filter(item -> item.matches("^add-posnum-.*$")).findFirst();
        match.ifPresent(item -> {
            int posnum = Integer.parseInt(item.split("add-posnum-")[1]);
            facts.put("attachedPosnum", posnum);  // 假设有一个键 attachedPosnum
        });
    }
}
