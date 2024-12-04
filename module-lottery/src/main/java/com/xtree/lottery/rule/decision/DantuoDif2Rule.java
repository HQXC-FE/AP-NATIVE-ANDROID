package com.xtree.lottery.rule.decision;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;

import java.util.List;
import java.util.Map;

@Rule(name = "DantuoDif2Rule", description = "二不同胆拖规则")
public class DantuoDif2Rule {

    @Priority
    public int getPriority() {
        return 18050;
    }

    @Condition
    public boolean when(Map<String, Object> facts) {
        // 检查是否包含 "dantuo-dif-2"
        List<String> ruleSuite = (List<String>) facts.get("ruleSuite");
        return ruleSuite != null && ruleSuite.contains("dantuo-dif-2");
    }

    @Action
    public void then(Map<String, Object> facts) {
        // 获取 formatCodes 数据
        List<List<String>> formatCodes = (List<List<String>>) facts.get("formatCodes");

        // 初始化 num 为 0
        int num = 0;

        if (formatCodes == null || formatCodes.size() < 2) {
            facts.put("num", num);
            return;
        }

        List<String> danMa = formatCodes.get(0); // 胆码
        List<String> tuoMa = formatCodes.get(1); // 拖码

        // 根据胆码和拖码的数量计算注数
        if (danMa.isEmpty()) {
            num = 0;
        } else if (danMa.size() == 1) {
            num = tuoMa.size();
        }

        // 将结果存入 facts
        facts.put("num", num);
    }
}
