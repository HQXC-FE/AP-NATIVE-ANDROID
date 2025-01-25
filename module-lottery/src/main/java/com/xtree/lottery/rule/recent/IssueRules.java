package com.xtree.lottery.rule.recent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class IssueRules {

    /**
     * 奖期高亮规则
     *
     * @param alias   当前彩种的别名
     * @param history 历史记录列表，每条记录包含期号等信息
     * @return 更新后的历史记录列表
     */
    public static List<Map<String, Object>> remarkIssue(String alias, List<Map<String, Object>> history) {
        // 只有以下彩种会被接受高亮奖期
        List<String> receiveLotteries = Arrays.asList("cq30s");
        if (!receiveLotteries.contains(alias)) {
            return history;
        }

        // 根据彩种的别名执行对应的逻辑
        switch (alias) {
            case "cq30s": {
                List<String> issues = Arrays.asList(
                        "0060", "0100", "0140", "0180", "0220", "0260", "0300", "0340", "0380", "0900", "0940", "0980", "1020",
                        "1060", "1100", "1140", "1180", "1220", "1260", "1300", "1340", "1380", "1420", "1460", "1500", "1540",
                        "1580", "1620", "1660", "1700", "1740", "1780", "1820", "1860", "1900", "1940", "1980", "2020", "2060",
                        "2100", "2140", "2180", "2220", "2260", "2300", "2340", "2380", "2420", "2460", "2500", "2540", "2580",
                        "2620", "2660", "2700", "2740", "2780", "2820", "2860"
                );
                return rebuildIssue(issues, history);
            }
            default:
                return history;
        }
    }

    /**
     * 为高亮的奖期加 class
     *
     * @param issues  高亮期号列表
     * @param history 历史记录列表
     * @return 更新后的历史记录列表
     */
    public static List<Map<String, Object>> rebuildIssue(List<String> issues, List<Map<String, Object>> history) {
        for (Map<String, Object> item : history) {
            if (item.containsKey("issue")) {
                String issue = (String) item.get("issue");
                // 根据 "-" 分隔并判断期号是否需要高亮
                String issueSuffix = issue.split("-")[1]; // 假定期号格式为 "xxxx-xxxx"
                if (issues.contains(issueSuffix)) {
                    item.put("issueClass", "high-light");
                }
            }
        }
        return history;
    }
}
