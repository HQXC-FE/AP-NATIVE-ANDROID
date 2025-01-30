package com.xtree.lottery.rule.recent.match;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Rule(name = "Extract Useful Values for Algorithm", description = "Extracts workCode and displayCode based on ruleSuite position")
public class ExtractUsefulValuesRule {

    @Priority
    public int getPriority() {
        return -99700;
    }

    @Condition
    public boolean when(Facts facts) {
        // 条件恒为 true
        return true;
    }

    @Action
    public void then(Facts facts) {
        try {
            // 获取历史记录和规则集
            List<Map<String, Object>> historyCodes = facts.get("historyCodes");
            List<String> ruleSuite = facts.get("ruleSuite");

            if (historyCodes != null && ruleSuite != null) {
                String position = ruleSuite.get(0);
                String[] positions = position.split(",");

                for (Map<String, Object> history : historyCodes) {
                    List<Integer> workCode = new ArrayList<>();
                    List<Map<String, Object>> displayCode = new ArrayList<>();
                    List<String> splitCode = (List<String>) history.get("split_code");

                    if (splitCode != null && positions.length == splitCode.size()) {
                        for (int i = 0; i < positions.length; i++) {
                            String item = positions[i];
                            String code = splitCode.get(i);

                            if ("X".equals(item)) {
                                workCode.add(Integer.parseInt(code));
                                displayCode.add(Map.of("codes", code, "className", "active"));
                            } else {
                                displayCode.add(Map.of("codes", code, "className", "disabled"));
                            }
                        }
                    }

                    // 更新历史记录
                    history.put("workCode", workCode);
                    history.put("displayCode", displayCode);
                    history.put("form", new ArrayList<>()); // 初始化空表单
                }
            }

            facts.put("historyCodes", historyCodes); // 更新 facts 中的历史记录

        } catch (Exception e) {
            CfLog.e("Error in ExtractUsefulValuesRule: " + e.getMessage());
        }
    }
}