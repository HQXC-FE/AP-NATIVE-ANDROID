package com.xtree.lottery.rule.betting.decision;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.List;

@Rule(name = "CombinationRule", description = "组合玩法规则")
public class CombinationRule {

    @Priority
    public int getPriority() {
        return -19800;
    }


    @Condition
    public boolean when(Facts facts) {
        List<String> ruleSuite = facts.get("ruleSuite");
        return ruleSuite.contains("combination");
    }

    @Action
    public void then(Facts facts) {
        try {
            List<List<String>> formatCodes = facts.get("formatCodes");
            int num = formatCodes.size();

            for (List<String> codeList : formatCodes) {
                num *= codeList.size();
            }

            facts.put("num", num);
        } catch (Exception e) {
            CfLog.e(e.getMessage());
        }
    }
}

