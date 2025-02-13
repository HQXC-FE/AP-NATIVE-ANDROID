package com.xtree.lottery.rule.recent.decision;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Rule(name = "K3 General Rule", description = "Processes K3 general rule for history codes")
public class K3GeneralRule {

    @Priority
    public int getPriority() {
        return -69000;
    }

    @Condition
    public boolean when(Facts facts) {
        List<String> ruleSuite = facts.get("ruleSuite");
        return ruleSuite != null && ruleSuite.contains("K3_GENERAL");
    }

    @Action
    public void then(Facts facts) {
        try {
            // 设置标题
            facts.put("title", "形态");

            List<Map<String, Object>> historyCodes = facts.get("historyCodes");
            for (Map<String, Object> history : historyCodes) {
                List<Integer> workCode = (List<Integer>) history.get("workCode");
                List<Map<String, String>> form = (List<Map<String, String>>) history.get("form");

                int unionWorkCode = new HashSet<>(workCode).size();
                switch (unionWorkCode) {
                    case 1:
                        form.add(createFormItem("三同号", "classNames.c_cc8b1e"));
                        break;
                    case 2:
                        form.add(createFormItem("二同号", "classNames.c_006dfe"));
                        break;
                    case 3:
                        form.add(createFormItem("三不同", "classNames.c_229e6d"));
                        break;
                }
            }
        } catch (Exception e) {
            CfLog.e("Error in K3GeneralRule: " + e.getMessage());
        }
    }

    private Map<String, String> createFormItem(String label, String className) {
        Map<String, String> formItem = new HashMap<>();
        formItem.put("label", label);
        formItem.put("className", className);
        return formItem;
    }
}
