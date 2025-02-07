package com.xtree.lottery.rule;

import com.xtree.base.vo.UserMethodsResponse;
import com.xtree.lottery.data.source.vo.MenuMethodsData;
import com.xtree.lottery.rule.betting.AfterRules;
import com.xtree.lottery.rule.betting.AttachedRules;
import com.xtree.lottery.rule.betting.DecisionRules;
import com.xtree.lottery.rule.betting.EndingRules;
import com.xtree.lottery.rule.betting.FilterRules;
import com.xtree.lottery.rule.betting.MatchRules;
import com.xtree.lottery.rule.betting.PrepareRules;
import com.xtree.lottery.rule.betting.data.RulesEntryData;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BettingEntryRule {
    private static BettingEntryRule INSTANCE;
    Rules rules;
    Facts facts;
    RulesEngine rulesEngine;

    public BettingEntryRule() {
        rules = new Rules();
        rulesEngine = new DefaultRulesEngine();

        MatchRules.addRules(rules);
        FilterRules.addRules(rules);
        PrepareRules.addRules(rules);
        AttachedRules.addRules(rules);
        DecisionRules.addRules(rules);
        AfterRules.addRules(rules);
        EndingRules.addRules(rules);
    }

    public static BettingEntryRule getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BettingEntryRule();
        }
        return INSTANCE;
    }

    public List<RulesEntryData.SubmitDTO> startEngine(RulesEntryData rulesEntryData) {
        facts = new Facts();
        Map<String, String> currentCategory = new HashMap<>();
        Map<String, Object> currentMethod = new HashMap<>();
        Map<String, Object> currentMethodSelectArea = new HashMap<>();
        Map<String, Object> bet = new HashMap<>();
        Map<String, String> betMode = new HashMap<>();
        List<Map<String, Object>> currentMethodSelectAreaLayout = new ArrayList<>();
        List<Map<String, String>> currentMethodMoneyModes = new ArrayList<>();
        List<Map<String, String>> currentMethodPrizeGroup = new ArrayList<>();
        Map<String, Object> attached = new HashMap<>();
        List<String> message = new ArrayList<>();

        // currentCategory.name
        currentCategory.put("name", rulesEntryData.getCurrentCategory().getName());
        // currentCategory.flag
        currentCategory.put("flag", rulesEntryData.getCurrentCategory().getFlag());

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
        if (null != rulesEntryData.getCurrentMethod().getPrizeGroup()) {
            for (UserMethodsResponse.DataDTO.PrizeGroupDTO item : rulesEntryData.getCurrentMethod().getPrizeGroup()) {
                Map<String, String> currentMethodPrizeGroupItem = new HashMap<>();
                currentMethodPrizeGroupItem.put("value", String.valueOf(item.getValue()));
                currentMethodPrizeGroupItem.put("label", item.getLabel());
                currentMethodPrizeGroup.add(currentMethodPrizeGroupItem);
            }
        }
        currentMethod.put("prize_group", currentMethodPrizeGroup);
        // currentMethod.prize_level
        currentMethod.put("prize_level", rulesEntryData.getCurrentMethod().getPrizeLevel());
        // currentMethod.relationMethods
        currentMethod.put("relationMethods", rulesEntryData.getCurrentMethod().getRelationMethods());
        // currentMethod.selectarea.originType
        // TODO 这个需要补全
        if (rulesEntryData.getCurrentMethod().getSelectarea() != null) {
            // currentMethod.selectarea.type
            currentMethodSelectArea.put("type", rulesEntryData.getCurrentMethod().getSelectarea().getType());
            // currentMethod.selectarea. originType 骰子
            currentMethodSelectArea.put("originType", rulesEntryData.getCurrentMethod().getSelectarea().getOriginType());
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
        }
        //bet.codes
        if (rulesEntryData.getBet().getCodes() instanceof List) {
            if (((List<?>) rulesEntryData.getBet().getCodes()).get(0) instanceof String) {
                bet.put("codes", rulesEntryData.getBet().getCodes());
            } else if (((List<?>) rulesEntryData.getBet().getCodes()).get(0) instanceof List<?>) {
                List<List<String>> betCodes = new ArrayList<>();
                for (List<String> item : (List<List<String>>) rulesEntryData.getBet().getCodes()) {
                    betCodes.add(item);
                }
                bet.put("codes", betCodes);
            } else {
                bet.put("codes", rulesEntryData.getBet().getCodes());
            }
        } else if (rulesEntryData.getBet().getCodes() instanceof String) {
            bet.put("codes", rulesEntryData.getBet().getCodes());
        } else {
            bet.put("codes", rulesEntryData.getBet().getCodes());
        }
        //bet.mode
        betMode.put("modeid", String.valueOf(rulesEntryData.getBet().getMode().getModeid()));
        betMode.put("name", rulesEntryData.getBet().getMode().getName());
        betMode.put("rate", String.valueOf(rulesEntryData.getBet().getMode().getRate()));
        bet.put("mode", betMode);
        //bet.times
        bet.put("times", String.valueOf(rulesEntryData.getBet().getTimes()));
        //bet.prize
        bet.put("prize", String.valueOf(rulesEntryData.getBet().getPrize()));
        //Todo bet.poschoose这个参数到最后还是无法知道
        //bet.poschoose
        bet.put("poschoose", null);

        facts.put("lotteryType", rulesEntryData.getType());
        facts.put("currentCategory", currentCategory);
        facts.put("currentMethod", currentMethod);
        facts.put("bet", bet);
        facts.put("attached", attached);
        facts.put("message", message);
        if (currentMethod.get("originalName") != null && !((String) currentMethod.get("originalName")).isEmpty())
            facts.put("matcherName", currentMethod.get("cateName") + "-" + currentMethod.get("groupName") + "-" + currentMethod.get("originalName"));
        else {
            facts.put("matcherName", currentMethod.get("cateName") + "-" + currentMethod.get("groupName") + "-" + currentMethod.get("name"));
        }

        // enter the rules
        rulesEngine.fire(rules, facts);

        List<RulesEntryData.SubmitDTO> submitDTOList = new ArrayList<>();
        HashMap<String, Object> done = facts.get("done");
        if (done != null) {
            if (done.get("submit") instanceof List) {
                List<HashMap<String, Object>> submitList = (List<HashMap<String, Object>>) done.get("submit");
                for (HashMap<String, Object> submit : submitList) {
                    submitDTOList.add(calcSubmit(submit));
                }
            } else {
                HashMap<String, Object> submit = (HashMap<String, Object>) done.get("submit");
                submitDTOList.add(calcSubmit(submit));
            }
        }

        return submitDTOList;
    }

    private RulesEntryData.SubmitDTO calcSubmit(HashMap<String, Object> submit) {
        RulesEntryData.SubmitDTO submitDTO = new RulesEntryData.SubmitDTO();
        submitDTO.setMethodid(Integer.valueOf(submit.get("methodid").toString()));
        submitDTO.setCodes(submit.get("codes").toString());
        submitDTO.setOmodel(Integer.valueOf(submit.get("omodel").toString()));
        submitDTO.setMode(Integer.valueOf(submit.get("mode").toString()));
        submitDTO.setTimes(Integer.valueOf(submit.get("times").toString()));
        submitDTO.setPoschoose(submit.get("poschoose"));
        submitDTO.setMenuid(Integer.valueOf(submit.get("menuid").toString()));
        submitDTO.setType(submit.get("type").toString());
        submitDTO.setNums(Integer.valueOf(submit.get("nums").toString()));
        submitDTO.setMoney(Double.valueOf(submit.get("money").toString()));
        if (submit.get("solo") != null) {
            submitDTO.setSolo(Boolean.valueOf(submit.get("solo").toString()));
        }
        submitDTO.setDesc((String) submit.get("desc"));
        return submitDTO;
    }

}
