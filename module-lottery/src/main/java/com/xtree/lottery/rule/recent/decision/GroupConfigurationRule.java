package com.xtree.lottery.rule.recent.decision;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Rule(name = "Group Configuration", description = "Handles group configuration logic based on ruleSuite")
public class GroupConfigurationRule {

    @Priority
    public int getPriority() {
        return -80000;
    }

    @Condition
    public boolean when(Facts facts) {
        List<String> rules = facts.get("ruleSuite");
        return rules != null && rules.contains("GROUP");
    }

    @Action
    public void then(Facts facts) {
        try {
            List<Map<String, Object>> historyCodes = facts.get("historyCodes");
            String title = null;

            if (historyCodes != null) {
                for (Map<String, Object> history : historyCodes) {
                    List<Integer> workCode = (List<Integer>) history.get("workCode");
                    if (workCode == null || workCode.isEmpty()) {
                        continue;
                    }

                    long unionWorkCode = workCode.stream().distinct().count();
                    long groupBySize = workCode.stream()
                            .collect(Collectors.groupingBy(code -> code))
                            .values()
                            .stream()
                            .filter(group -> group.size() > 1)
                            .count();

                    if (history.get("code") == null || history.get("code").toString().isEmpty()) {
                        addForm(history, "--", "#ff0000");
                    }

                    switch (workCode.size()) {
                        case 5:
                            title = "五星组态";
                            if (unionWorkCode == 5) addForm(history, "组选120", "#7c73ff");
                            if (unionWorkCode == 4) addForm(history, "组选60", "#1291bb");
                            if (unionWorkCode == 3 && groupBySize == 2) addForm(history, "组选30", "#229e6d");
                            if (unionWorkCode == 3 && groupBySize == 1) addForm(history, "组选20", "#cc8b1e");
                            if (unionWorkCode == 2 && groupBySize == 2) addForm(history, "组选10", "#006dfe");
                            if (unionWorkCode == 2 && groupBySize == 1) addForm(history, "组选5", "#ff33ff");
                            break;

                        case 4:
                            title = "四星组态";
                            if (unionWorkCode == 4) addForm(history, "组选24", "#7c73ff");
                            if (unionWorkCode == 3) addForm(history, "组选12", "#1291bb");
                            if (unionWorkCode == 2 && groupBySize == 2) addForm(history, "组选6", "#229e6d");
                            if (unionWorkCode == 2 && groupBySize == 1) addForm(history, "组选4", "#cc8b1e");
                            break;

                        case 3:
                            title = "三星组态";
                            if (unionWorkCode == 3) addForm(history, "组六", "#685ba2");
                            if (unionWorkCode == 2) addForm(history, "组三", "#fc5d50");
                            break;

                        default:
                            break;
                    }
                }

                facts.put("title", title);
            }
        } catch (Exception e) {
            CfLog.e("Error in GroupConfigurationRule: " + e.getMessage());
        }
    }

    private void addForm(Map<String, Object> history, String label, String className) {
        List<Map<String, String>> form = (List<Map<String, String>>) history.computeIfAbsent("form", key -> new java.util.ArrayList<>());
        form.add(Map.of("label", label, "className", className));
    }
}
