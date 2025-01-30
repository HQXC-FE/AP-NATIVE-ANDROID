package com.xtree.lottery.rule;

import com.xtree.base.vo.UserMethodsResponse;
import com.xtree.lottery.data.source.vo.MenuMethodsData;
import com.xtree.lottery.data.source.vo.RecentLotteryVo;
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

    public List<RecentLotteryVo> startEngine(RulesEntryData rulesEntryData, List<RecentLotteryVo> historyLottery) {
        facts = new Facts();
        Map<String, Object> currentMethod = new HashMap<>();
        Map<String, Object> currentMethodSelectArea = new HashMap<>();
        List<Map<String, Object>> currentMethodSelectAreaLayout = new ArrayList<>();
        List<Map<String, String>> currentMethodMoneyModes = new ArrayList<>();
        List<Map<String, String>> currentMethodPrizeGroup = new ArrayList<>();
        List<Map<String, Object>> inputHistoryLottery = new ArrayList<>();

        // currentMethod.flag 此为 CurrentCategory 的 flag
        currentMethod.put("flag", rulesEntryData.getCurrentCategory().getFlag());
        // currentMethod.methodName
        currentMethod.put("methodName", Optional.ofNullable(rulesEntryData.getCurrentMethod().getOriginalName()).orElse(rulesEntryData.getCurrentMethod().getName()));
        // currentMethod.methodid
        currentMethod.put("methodid", rulesEntryData.getCurrentMethod().getMethodid());
        // currentMethod.menuid
        currentMethod.put("menuid", rulesEntryData.getCurrentMethod().getMenuid());
        // currentMethod.lotteryId
        currentMethod.put("lotteryId", rulesEntryData.getCurrentMethod().getLotteryId());
        // currentMethod.name
        currentMethod.put("name", rulesEntryData.getCurrentMethod().getName());
        // currentMethod.originalName
        currentMethod.put("originalName", rulesEntryData.getCurrentMethod().getOriginalName());
        // currentMethod.cate_title
        currentMethod.put("cate_title", rulesEntryData.getCurrentMethod().getCateTitle());
        // currentMethod.desc
        currentMethod.put("desc", rulesEntryData.getCurrentMethod().getDesc());
        // currentMethod.show_str
        currentMethod.put("show_str", rulesEntryData.getCurrentMethod().getShowStr());
        // currentMethod.code_sp
        currentMethod.put("code_sp", rulesEntryData.getCurrentMethod().getCodeSp());
        // currentMethod.cateName
        currentMethod.put("cateName", rulesEntryData.getCurrentMethod().getCateName());
        // currentMethod.groupName
        currentMethod.put("groupName", rulesEntryData.getCurrentMethod().getGroupName());
        // currentMethod.money_modes
        for (MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO.MoneyModesDTO item : rulesEntryData.getCurrentMethod().getMoneyModes()) {
            Map<String, String> currentMethodMoneyModesItem = new HashMap<>();
            currentMethodMoneyModesItem.put("modeid", String.valueOf(item.getModeid()));
            currentMethodMoneyModesItem.put("name", item.getName());
            currentMethodMoneyModesItem.put("rate", String.valueOf(item.getRate()));
            currentMethodMoneyModes.add(currentMethodMoneyModesItem);
        }
        currentMethod.put("money_modes", currentMethodMoneyModes);
        // currentMethod.prize_group
        for (UserMethodsResponse.DataDTO.PrizeGroupDTO item : rulesEntryData.getCurrentMethod().getPrizeGroup()) {
            Map<String, String> currentMethodPrizeGroupItem = new HashMap<>();
            currentMethodPrizeGroupItem.put("value", String.valueOf(item.getValue()));
            currentMethodPrizeGroupItem.put("label", item.getLabel());
            currentMethodPrizeGroup.add(currentMethodPrizeGroupItem);
        }
        currentMethod.put("prize_group", currentMethodPrizeGroup);
        // currentMethod.prize_level
        currentMethod.put("prize_level", rulesEntryData.getCurrentMethod().getPrizeLevel());
        // currentMethod.relationMethods
        currentMethod.put("relationMethods", rulesEntryData.getCurrentMethod().getRelationMethods());
        // currentMethod.selectarea.originType
        // TODO 这个需要补全
        // currentMethod.selectarea.type
        currentMethodSelectArea.put("type", rulesEntryData.getCurrentMethod().getSelectarea().getType());
        rulesEntryData.getCurrentMethod().getSelectarea().getLayout();
        // currentMethod.selectarea.layout
        if (rulesEntryData.getCurrentMethod().getSelectarea().getLayout() != null) {
            for (MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO.SelectareaDTO.LayoutDTO item : rulesEntryData.getCurrentMethod().getSelectarea().getLayout()) {
                Map<String, Object> currentMethodSelectAreaLayoutItem = new HashMap<>();
                currentMethodSelectAreaLayoutItem.put("title", item.getTitle());
                currentMethodSelectAreaLayoutItem.put("no", item.getNo());
                currentMethodSelectAreaLayoutItem.put("place", String.valueOf(item.getPlace()));
                currentMethodSelectAreaLayoutItem.put("cols", String.valueOf(item.getCols()));
                currentMethodSelectAreaLayoutItem.put("minchosen", String.valueOf(item.getMinchosen()));
                currentMethodSelectAreaLayout.add(currentMethodSelectAreaLayoutItem);
            }
            currentMethodSelectArea.put("layout", currentMethodSelectAreaLayout);
        }
        // currentMethod.selectarea.selPosition
        currentMethodSelectArea.put("selPosition", rulesEntryData.getCurrentMethod().getSelectarea().isSelPosition());
        // currentMethod.selectarea
        currentMethod.put("selectarea", currentMethodSelectArea);

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

        //Todo 返回的参数请使用 LotteryBackReportVo

        List<RecentLotteryVo> outputHistory = new ArrayList<>();
        List<Map<String, Object>> filterLottery = ((Map<String, List<Map<String, Object>>>) facts.get("done")).get("history");
        for (Map<String, Object> item : filterLottery) {
            RecentLotteryVo vo = new RecentLotteryVo(
                    (String) item.get("displayCode"),
                    (String) item.get("draw_time"),
                    (String) item.get("issueClass"),
                    (String) item.get("issue"),
                    (ArrayList<String>) item.get("form"));
            outputHistory.add(vo);
        }

        return outputHistory;
    }
}

