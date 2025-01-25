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

@Rule(name = "VNM North Rule", description = "Handles the specific logic for VNM North.")
public class VnmNorthRule {

    @Priority
    public int getPriority() {
        return -65000;
    }

    @Condition
    public boolean when(Facts facts) {
        List<String> ruleSuite = facts.get("ruleSuite");
        return ruleSuite.contains("VNM_NORTH");
    }

    @Action
    public void then(Facts facts) {
        try {
            Map<String, Object> currentMethod = facts.get("currentMethod");
            List<Map<String, Object>> historyCodes = facts.get("historyCodes");

            if (currentMethod.get("currentIssue") == null) {
                return;
            }

            Map<String, Object> currentIssueShow = historyCodes.stream()
                    .filter(item -> item.get("issue").equals(currentMethod.get("currentIssue")))
                    .findFirst()
                    .orElse(null);

            if (currentIssueShow == null) {
                return;
            }

            List<List<Map<String, String>>> bonu5Nums = chunkAndMap((List<String>) currentIssueShow.get("split_code"), 50, 5);
            List<List<Map<String, String>>> bonu4Nums = chunkAndMap((List<String>) currentIssueShow.get("split_code"), 90, 4, 40);
            List<List<Map<String, String>>> bonu3Nums = chunkAndMap((List<String>) currentIssueShow.get("split_code"), 17, 3, 9);
            List<List<Map<String, String>>> bonu2Nums = chunkAndMap((List<String>) currentIssueShow.get("split_code"), 8, 2);

            String matchName = ((String) currentMethod.get("cateName") + currentMethod.get("groupName")).trim();

            if (List.of("包组", "串号", "不中").stream().anyMatch(matchName::contains)) {
                activateBonuses(matchName, bonu5Nums, bonu4Nums, bonu3Nums, bonu2Nums);
            }

            if (List.of("头头", "头与尾头与尾").contains(matchName)) {
                setActive(bonu2Nums);

                if (matchName.equals("头与尾头与尾")) {
                    bonu5Nums.get(0).get(3).put("className", "active");
                    bonu5Nums.get(0).get(4).put("className", "active");
                }
            }

            if (matchName.startsWith("尾") || matchName.startsWith("一等奖")) {
                List<Map<String, String>> targetList = matchName.startsWith("尾") ? bonu5Nums.get(0) : bonu5Nums.get(1);
                updateTail(matchName, targetList);
            }

            if (matchName.equals("鱼虾蟹鱼虾蟹")) {
                bonu5Nums.get(0).get(4).put("className", "active");
            }

            currentIssueShow.put("displayCode", List.of(
                    Map.of("label", "特奖", "codes", List.of(bonu5Nums.get(0))),
                    Map.of("label", "一奖", "codes", List.of(bonu5Nums.get(1))),
                    Map.of("label", "二奖", "codes", List.of(bonu5Nums.get(2), bonu5Nums.get(3))),
                    Map.of("label", "三奖", "codes", bonu5Nums.subList(bonu5Nums.size() - 6, bonu5Nums.size())),
                    Map.of("label", "四奖", "codes", bonu4Nums.subList(0, 4)),
                    Map.of("label", "五奖", "codes", bonu4Nums.subList(bonu4Nums.size() - 6, bonu4Nums.size())),
                    Map.of("label", "六奖", "codes", bonu3Nums),
                    Map.of("label", "七奖", "codes", bonu2Nums)
            ));

            historyCodes.clear();
            historyCodes.add(currentIssueShow);
        } catch (Exception e) {
            CfLog.e("Error in VnmNorthRule: " + e.getMessage());
        }
    }

    private List<List<Map<String, String>>> chunkAndMap(List<String> codes, int take, int chunkSize) {
        return chunkAndMap(codes, take, chunkSize, 0);
    }

    private List<List<Map<String, String>>> chunkAndMap(List<String> codes, int take, int chunkSize, int offset) {
        return codes.stream()
                .skip(offset)
                .limit(take)
                .collect(Collectors.groupingBy(s -> codes.indexOf(s) / chunkSize))
                .values()
                .stream()
                .map(group -> group.stream()
                        .map(code -> Map.of("codes", code, "className", "disabled"))
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    private void activateBonuses(String matchName, List<List<Map<String, String>>>... bonuses) {
        for (List<List<Map<String, String>>> bonusGroup : bonuses) {
            for (List<Map<String, String>> bonus : bonusGroup) {
                for (int idx = 0; idx < bonus.size(); idx++) {
                    Map<String, String> item = bonus.get(idx);
                    if (List.of("包组后二直选", "串号选2", "串号选3", "串号选4", "不中4号不中", "不中8号不中", "不中10号不中").contains(matchName) && idx > bonus.size() - 3) {
                        item.put("className", "active");
                    }
                    if (matchName.equals("包组后三直选") && idx > bonus.size() - 4 && bonus.size() > 2) {
                        item.put("className", "active");
                    }
                    if (matchName.equals("包组后四直选") && idx > bonus.size() - 5 && bonus.size() > 3) {
                        item.put("className", "active");
                    }
                }
            }
        }
    }

    private void setActive(List<List<Map<String, String>>> bonuses) {
        for (List<Map<String, String>> bonus : bonuses) {
            for (Map<String, String> item : bonus) {
                item.put("className", "active");
            }
        }
    }

    private void updateTail(String matchName, List<Map<String, String>> targetList) {
        for (int index = 0; index < targetList.size(); index++) {
            Map<String, String> item = targetList.get(index);
            if (matchName.endsWith("后二") && index > 2) {
                item.put("className", "active");
            }
            if (matchName.endsWith("后三") && index > 1) {
                item.put("className", "active");
            }
            if (matchName.endsWith("后四") && index > 0) {
                item.put("className", "active");
            }
        }
    }
}

