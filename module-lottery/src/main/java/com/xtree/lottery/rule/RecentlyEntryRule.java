package com.xtree.lottery.rule;

import com.xtree.lottery.data.source.vo.RecentLotteryBackReportVo;
import com.xtree.lottery.data.source.vo.RecentLotteryVo;
import com.xtree.lottery.data.source.vo.RecentReturnVo;
import com.xtree.lottery.rule.betting.data.RulesEntryData;
import com.xtree.lottery.rule.recent.RecentDecisionRules;
import com.xtree.lottery.rule.recent.RecentEndingRules;
import com.xtree.lottery.rule.recent.RecentMatchRules;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class RecentlyEntryRule {
    Rules rules;
    Facts facts;
    RulesEngine rulesEngine;
    private static RecentlyEntryRule INSTANCE;

    public static RecentlyEntryRule getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RecentlyEntryRule();
        }
        return INSTANCE;
    }

    public RecentlyEntryRule() {
        rules = new Rules();
        rulesEngine = new DefaultRulesEngine();

        RecentMatchRules.addRules(rules);
        RecentDecisionRules.addRules(rules);
        RecentEndingRules.addRules(rules);
    }

    public RecentReturnVo startEngine(RulesEntryData rulesEntryData, List<RecentLotteryVo> historyLottery) {
        facts = new Facts();
        Map<String, Object> currentMethod = new HashMap<>();
        List<Map<String, Object>> inputHistoryLottery = new ArrayList<>();

        // currentMethod.flag 此为 CurrentCategory 的 flag
        currentMethod.put("flag", rulesEntryData.getCurrentCategory().getFlag());
        // currentMethod.methodName
        currentMethod.put("methodName", Optional.ofNullable(rulesEntryData.getCurrentMethod().getOriginalName()).orElse(rulesEntryData.getCurrentMethod().getName()));
        // currentMethod.name
        currentMethod.put("name", rulesEntryData.getCurrentMethod().getName());
        // currentMethod.desc
        currentMethod.put("desc", rulesEntryData.getCurrentMethod().getDesc());
        // currentMethod.show_str
        currentMethod.put("cateName", rulesEntryData.getCurrentMethod().getCateName());
        // currentMethod.groupName
        currentMethod.put("groupName", rulesEntryData.getCurrentMethod().getGroupName());

        // historyLottery
        for (RecentLotteryVo item : historyLottery) {
            HashMap<String, Object> temp = new HashMap<>();
            temp.put("code", item.getCode());
            temp.put("draw_time", item.getDraw_time());
            temp.put("issue", item.getIssue());
            temp.put("original_code", item.getOriginal_code());
            temp.put("split_code", item.getSplit_code());
            inputHistoryLottery.add(temp);
        }

        facts.put("currentMethod", currentMethod);
        facts.put("historyCodes", inputHistoryLottery);

        // enter the rules
        rulesEngine.fire(rules, facts);

        List<RecentLotteryBackReportVo> outputHistory;
        List<Map<String, Object>> filterLottery = ((Map<String, List<Map<String, Object>>>) facts.get("done")).get("history");
        outputHistory = filterLottery.stream()
                .map(item -> {
                    HashMap<String, Object> historyItem = new HashMap<>();

                    if (null != item.get("displayCode")) {
                        historyItem.put("displayCode", item.get("displayCode"));
                    } else if (null != item.get("codes")) {
                        historyItem.put("displayCode", item.get("codes"));
                    } else {
                        historyItem.put("displayCode", new ArrayList<>());
                    }

                    if (null != item.get("form")) {
                        historyItem.put("form", item.get("form"));
                    } else {
                        historyItem.put("form", new ArrayList<>());
                    }

                    if (null != item.get("issue")) {
                        historyItem.put("issue", item.get("issue"));
                    } else {
                        historyItem.put("issue", "");
                    }

                    if (null != item.get("draw_time")) {
                        historyItem.put("draw_time", item.get("draw_time"));
                    } else {
                        historyItem.put("draw_time", "");
                    }

                    if (null != item.get("issueClass")) {
                        historyItem.put("issueClass", item.get("issueClass"));
                    } else {
                        historyItem.put("issueClass", "");
                    }

                    return new RecentLotteryBackReportVo((String) historyItem.get("issue"),
                            (String) historyItem.get("draw_time"),
                            (String) historyItem.get("issueClass"),
                            (ArrayList<Map<String, String>>) historyItem.get("displayCode"),
                            (ArrayList<Map<String, String>>) historyItem.get("form"));
                })
                .collect(Collectors.toList());

        return new RecentReturnVo((((Map<String, String>) facts.get("done")).get("title")), outputHistory);
    }
}

