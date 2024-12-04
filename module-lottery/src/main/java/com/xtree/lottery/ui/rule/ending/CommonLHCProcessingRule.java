package com.xtree.lottery.ui.rule.ending;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Rule(name = "Common LHC Processing", description = "通用六合彩处理规则")
public class CommonLHCProcessingRule {

    @Priority
    public int getPriority() {
        return 100;
    }

    @Condition
    public boolean when(Map<String, Object> facts) {
        return ((List<String>) facts.get("ruleSuite")).contains("lhc-common");
    }

    @Action
    public void then(Map<String, Object> facts) {
        List<Map<String, Object>> forDisplay = new ArrayList<>();
        List<Map<String, Object>> forSubmit = new ArrayList<>();

        List<Map<String, Object>> betCodes = (List<Map<String, Object>>) facts.get("bet.codes");
        Map<String, Object> currentMethod = (Map<String, Object>) facts.get("currentMethod");

        betCodes.forEach(code -> {
            if (code.get("value") != null && Integer.parseInt(code.get("value").toString()) > 0) {
                Map<String, Object> displayItem = new HashMap<>();
                Map<String, Object> submitItem = new HashMap<>();

                displayItem.put("methodName", "[" + currentMethod.get("cateName") + "_" + code.get("name") + "]");
                displayItem.put("codes", code.get("num"));
                displayItem.put("times", code.get("value") + "倍");
                displayItem.put("num", 1);
                displayItem.put("money", Integer.parseInt(code.get("value").toString()));
                displayItem.put("mode", "元");
                displayItem.put("prize", facts.get("bet.prize") != null ?
                        "模式:" + ((Map<String, Object>) ((List<?>) currentMethod.get("prize_group")).get(0))
                                .get("label").toString().split("-")[0].substring(2) : "");

                submitItem.put("omodel", facts.get("bet.prize"));
                submitItem.put("mode", "CONFIG.LHC_SUBMIT_MODE");
                submitItem.put("times", code.get("value"));
                submitItem.put("methodid", code.get("methodid"));
                submitItem.put("codes", code.get("num"));
                submitItem.put("menuid", code.get("menuid"));
                submitItem.put("type", code.get("type"));
                submitItem.put("money", Integer.parseInt(code.get("value").toString()));
                submitItem.put("nums", 1);
                submitItem.put("desc", (String)displayItem.get("methodName") + displayItem.get("codes"));

                forDisplay.add(displayItem);
                forSubmit.add(submitItem);
            }
        });

        Map<String, Object> done = new HashMap<>();
        done.put("currentPrize", facts.get("currentPrize"));
        done.put("currentBonus", facts.get("currentBonus"));
        done.put("display", forDisplay);
        done.put("submit", forSubmit);
        done.put("message", facts.get("message"));

        facts.put("done", done);
    }
}