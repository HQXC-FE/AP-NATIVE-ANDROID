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

@Rule(name = "VNM Middle and South Fast Rule", description = "Handles VNM Middle and South Fast game rules")
public class VNMMiddleSouthFastRule {

    @Priority
    public int getPriority() {
        return -65000;
    }

    @Condition
    public boolean when(Facts facts) {
        List<String> ruleSuite = facts.get("ruleSuite");
        return ruleSuite != null && ruleSuite.contains("VNM_MIDDLE_SOUTH_FAST");
    }

    @Action
    public void then(Facts facts) {
        try {
            Map<String, Object> currentMethod = facts.get("currentMethod");
            String currentIssue = (String) currentMethod.get("currentIssue");
            List<Map<String, Object>> historyCodes = facts.get("historyCodes");

            if (currentIssue == null) return;

            Map<String, Object> currentIssueShow = historyCodes.stream()
                    .filter(item -> currentIssue.equals(item.get("issue")))
                    .findFirst()
                    .orElse(null);

            if (currentIssueShow == null) return;

            List<String> splitCode = (List<String>) currentIssueShow.get("split_code");

            // Processing numbers into chunks
            List<List<Map<String, String>>> bonu6Nums = processChunks(splitCode, 6, 6);
            List<List<Map<String, String>>> bonu5Nums = processChunks(splitCode.subList(6, 61), 5, 55);
            List<List<Map<String, String>>> bonu4Nums = processChunks(splitCode.subList(45, 61), 4, 16);
            List<List<Map<String, String>>> bonu3Nums = processChunks(splitCode.subList(61, 64), 3, 3);
            List<List<Map<String, String>>> bonu2Nums = processChunks(splitCode.subList(64, 66), 2, 2);

            String matchName = currentMethod.get("cateName").toString().trim() +
                    currentMethod.get("groupName").toString().trim();

            // Apply rules to classify numbers
            applyRules(matchName, bonu6Nums, bonu5Nums, bonu4Nums, bonu3Nums, bonu2Nums);

            // Display code assignment
            List<Map<String, Object>> displayCode = createDisplayCodes(bonu6Nums, bonu5Nums, bonu4Nums, bonu3Nums, bonu2Nums);

            currentIssueShow.put("displayCode", displayCode);
            facts.put("historyCodes", List.of(currentIssueShow));

        } catch (Exception e) {
            CfLog.e("Error in VNMMiddleSouthFastRule: " + e.getMessage());
        }
    }

    private List<List<Map<String, String>>> processChunks(List<String> splitCode, int chunkSize, int limit) {
        return splitCode.stream()
                .limit(limit)
                .collect(Collectors.groupingBy(s -> splitCode.indexOf(s) / chunkSize))
                .values().stream()
                .map(chunk -> chunk.stream()
                        .map(code -> Map.of("codes", code, "className", "disabled"))
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    private void applyRules(String matchName, List<List<Map<String, String>>>... bonuNums) {
        for (List<List<Map<String, String>>> bonuGroup : bonuNums) {
            bonuGroup.forEach(group -> {
                for (int idx = 0; idx < group.size(); idx++) {
                    Map<String, String> num = group.get(idx);

                    // Example of logic
                    if (matchName.equals("example") && idx > group.size() - 3) {
                        num.put("className", "active");
                    }
                }
            });
        }
    }

    private List<Map<String, Object>> createDisplayCodes(List<List<Map<String, String>>> bonu6Nums, List<List<Map<String, String>>> bonu5Nums,
                                                         List<List<Map<String, String>>> bonu4Nums, List<List<Map<String, String>>> bonu3Nums,
                                                         List<List<Map<String, String>>> bonu2Nums) {
        return List.of(
                Map.of("label", "特奖", "codes", bonu6Nums.get(0)),
                Map.of("label", "一奖", "codes", bonu5Nums.get(0)),
                Map.of("label", "二奖", "codes", bonu5Nums.subList(1, 2)),
                Map.of("label", "三奖", "codes", bonu5Nums.subList(2, 4)),
                Map.of("label", "四奖", "codes", bonu5Nums.subList(4, 11)),
                Map.of("label", "五奖", "codes", bonu4Nums.get(0)),
                Map.of("label", "六奖", "codes", bonu4Nums.subList(1, 3)),
                Map.of("label", "七奖", "codes", bonu3Nums),
                Map.of("label", "八奖", "codes", bonu2Nums)
        );
    }
}
