package com.xtree.lottery.rule.decision;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;

import java.util.List;
import java.util.Map;

@Rule(name = "DantuoDif3Rule", description = "三不同胆拖规则")
public class DantuoDif3Rule {

    @Priority
    public int getPriority() {
        return 18050;
    }

    @Condition
    public boolean when(Map<String, Object> facts) {
        // 检查是否包含 "dantuo-dif-3"
        List<String> ruleSuite = (List<String>) facts.get("ruleSuite");
        return ruleSuite != null && ruleSuite.contains("dantuo-dif-3");
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

        int danMaSize = danMa.size();
        int tuoMaSize = tuoMa.size();

        if (tuoMaSize < 4 && danMaSize == 0) {
            num = 0;
        } else if (tuoMaSize == 3 && danMaSize >= 1) {
            num = 3;
        } else if (tuoMaSize == 4 && danMaSize == 0) {
            num = 4;
        } else if (tuoMaSize == 4 && danMaSize == 1) {
            num = 6;
        } else if (tuoMaSize == 4 && danMaSize == 2) {
            num = 4;
        } else if (tuoMaSize == 5) {
            num = 10;
        } else if (tuoMaSize == 6 && danMaSize == 0) {
            num = 20;
        }

        // 将结果存入 facts
        facts.put("num", num);
    }
}
