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

        List<RecentLotteryBackReportVo> outputHistory = new ArrayList<>();
        List<Map<String, Object>> filterLottery = ((Map<String, List<Map<String, Object>>>) facts.get("done")).get("history");
        for (Map<String, Object> item : filterLottery) {
            RecentLotteryBackReportVo vo = new RecentLotteryBackReportVo(
                    (String) item.get("issue"),
                    (String) item.get("draw_time"),
                    (String) item.get("issueClass"),
                    (List<String>) item.get("displayCode"),
                    (List<String>) item.get("form"));
            outputHistory.add(vo);
        }

        return new RecentReturnVo((((Map<String, String>) facts.get("done")).get("title")), outputHistory);
    }
}

