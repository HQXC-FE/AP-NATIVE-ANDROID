package com.xtree.lottery.rule;

import com.xtree.base.vo.UserMethodsResponse;
import com.xtree.lottery.data.source.vo.MenuMethodsData;
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

    public void startEngine(RulesEntryData rulesEntryData) {
        facts = new Facts();
        Map<String, Object> currentMethod = new HashMap<>();
        Map<String, Object> currentMethodSelectArea = new HashMap<>();
        List<Map<String, Object>> currentMethodSelectAreaLayout = new ArrayList<>();
        List<Map<String, String>> currentMethodMoneyModes = new ArrayList<>();
        List<Map<String, String>> currentMethodPrizeGroup = new ArrayList<>();
        Map<String, Object> attached = new HashMap<>();
        List<String> message = new ArrayList<>();

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

        //facts.put("lotteryType", rulesEntryData.getType());
        facts.put("lotteryType", "ssc");
        facts.put("currentMethod", currentMethod);
        facts.put("attached", attached);
        facts.put("message", message);
        if (currentMethod.get("originalName") != null && !((String) currentMethod.get("originalName")).isEmpty())
            facts.put("matcherName", currentMethod.get("cateName") + "-" + currentMethod.get("groupName") + "-" + currentMethod.get("originalName"));
        else {
            facts.put("matcherName", currentMethod.get("cateName") + "-" + currentMethod.get("groupName") + "-" + currentMethod.get("name"));
        }

        // enter the rules
        rulesEngine.fire(rules, facts);

        //        RulesEntryData.SubmitDTO submitDTO = new RulesEntryData.SubmitDTO();
        //        HashMap<String, Object> done = facts.get("done");
        //        if (done != null) {
        //            HashMap<String, Object> submit = (HashMap<String, Object>) done.get("submit");
        //            submitDTO.setMethodid(Integer.parseInt((String) submit.get("methodid")));
        //            submitDTO.setCodes((String) submit.get("codes"));
        //            submitDTO.setOmodel((int) submit.get("omodel"));
        //            submitDTO.setMode(Integer.parseInt((String) submit.get("mode")));
        //            submitDTO.setTimes((int) submit.get("times"));
        //            submitDTO.setPoschoose(submit.get("poschoose"));
        //            submitDTO.setMenuid(Integer.parseInt((String) submit.get("menuid")));
        //            submitDTO.setType((String) submit.get("type"));
        //            submitDTO.setNums((int) submit.get("nums"));
        //            submitDTO.setMoney((double) submit.get("money"));
        //            submitDTO.setSolo((boolean) submit.get("solo"));
        //            submitDTO.setDesc((String) submit.get("desc"));
        //        }
        //
        //        return submitDTO;
    }
}

