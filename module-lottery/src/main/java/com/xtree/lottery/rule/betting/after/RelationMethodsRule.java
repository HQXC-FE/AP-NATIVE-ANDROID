package com.xtree.lottery.rule.betting.after;

import com.xtree.base.utils.CfLog;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//Todo 还有问题
@Rule(name = "RelationMethodsRule", description = "处理与当前方法相关的规则")
public class RelationMethodsRule {

    @Priority
    public int getPriority() {
        return -8550;
    }

    @Condition
    public boolean when(Facts facts) {
        Map<String, Object> currentMethod = facts.get("currentMethod");
        return currentMethod != null && currentMethod.containsKey("relationMethods") &&
                ((List<?>) currentMethod.get("relationMethods")).size() > 0;
    }

    @Action
    public void then(Facts facts) throws Exception {
        try {
            Map<String, Object> currentMethod = facts.get("currentMethod");
            List<Integer> relationMethodsIds = (List<Integer>) currentMethod.get("relationMethods");
            String currentCategoryFlag = (String) ((Map<String, Object>) facts.get("currentCategory")).get("flag");
            String lotteryType = facts.get("lotteryType");
            Map<String, Object> bet = facts.get("bet");

            // 模拟执行异步任务并收集结果
            List<Map<String, Object>> relationMethods = relationMethodsIds.stream()
                    .map(methodId -> {
                        Map<String, Object> methodData = findMethodData(currentCategoryFlag, methodId);
                        Map<String, Object> betWithMethodId = new java.util.HashMap<>(bet);
                        betWithMethodId.put("methodid", methodId);

                        // 模拟 execute() 回调逻辑
                        Map<String, Object> callbackData = executeTask(currentCategoryFlag, methodData, lotteryType, betWithMethodId);

                        Map<String, Object> result = new java.util.HashMap<>(betWithMethodId);
                        result.putAll(callbackData);
                        return result;
                    })
                    .collect(Collectors.toList());

            // 存储 relationMethods 结果
            facts.put("relationMethods", relationMethods);
        } catch (Exception e) {
            CfLog.e(e.getMessage());
        }
    }

    // 查找方法数据
    private Map<String, Object> findMethodData(String categoryFlag, int methodId) {
        // 模拟从 __METHODS_STATIC__ 中查找方法数据的逻辑
        // 在实际项目中需要替换为真实逻辑
        return Map.of(
                "methodid", methodId,
                "money_modes", List.of("low", "medium", "high"),
                "originalName", "Sample Method " + methodId,
                "name", "Sample Method " + methodId
        );
    }

    // 模拟执行器任务
    private Map<String, Object> executeTask(String categoryFlag, Map<String, Object> methodData, String lotteryType, Map<String, Object> bet) {
        // 模拟 execute 方法逻辑
        // 在实际项目中应替换为真实实现
        return Map.of("data", "Result for methodId: " + methodData.get("methodid"));
    }
}
