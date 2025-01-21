package com.xtree.lottery.rule.betting.attached;

import com.xtree.base.utils.CfLog;

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
        return -59900;
    }

    @Condition
    public boolean when(Facts facts) {
        List<String> ruleSuite = facts.get("ruleSuite");
        return ruleSuite.stream().anyMatch(item -> item.matches("^add-posnum-.*$"));
    }

    @Action
    public void then(Facts facts) {
        try {
            Map<String, String> attached = facts.get("attached");
            List<String> ruleSuite = facts.get("ruleSuite");
            Optional<String> match = ruleSuite.stream().filter(item -> item.matches("^add-posnum-.*$")).findFirst();
            match.ifPresent(item -> {
                int posnum = Integer.parseInt(item.split("add-posnum-")[1]);
                attached.put("posnum", posnum + "");
                facts.put("attached", attached);
            });
        } catch (Exception e) {
            CfLog.e(e.getMessage());
        }
    }
}
