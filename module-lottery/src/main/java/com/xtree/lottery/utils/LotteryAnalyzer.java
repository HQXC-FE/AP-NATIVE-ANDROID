package com.xtree.lottery.utils;

import com.xtree.base.mvvm.ExKt;
import com.xtree.lottery.data.config.MissingCodesConfig;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by KAKA on 2024/5/7.
 * Describe: 彩票号码算法工具
 */
public class LotteryAnalyzer {

    public static final String SPLIT = ",";
    public static final String SPLIT_1 = "；";

    //每种单式玩法对应的有效号码位数
    public static final Map<String, String> INPUT_PLAYS_MAP = new HashMap() {
        {
            put("五星直选-单式", "5,9,1");
            put("四星直选-单式", "4,9,1");
            put("后三直选-单式", "3,9,1");
            put("后三组选-混合组选", "3,9,1");
            put("前三直选-单式", "3,9,1");
            put("前三组选-混合组选", "3,9,1");
            put("中三直选-单式", "3,9,1");
            put("中三组选-混合组选", "3,9,1");
            put("前中后三星组选-混合组选", "3,9,1");
            put("二星直选-后二直选(单式)", "2,9,1");
            put("二星直选-前二直选(单式)", "2,9,1");
            put("二星组选-后二组选(单式)", "2,9,1");
            put("二星组选-前二直选(单式)", "2,9,1");
            put("二星组选-后二直选(单式)", "2,9,1");
            put("任二直选-直选单式", "2,9,1");
            put("任二组选-组选单式", "2,9,1");
            put("任三直选-直选单式", "3,9,1");
            put("任三组选-混合组选", "3,9,1");
            put("任四直选-直选单式", "4,9,1");

            //11选5
            put("三码-前三直选单式", "3,11,2");
            put("三码-前三组选单式", "3,11,2");
            put("二码-前二直选单式", "2,11,2");
            put("二码-前二组选单式", "2,11,2");
            put("任选单式-一中一", "1,11,2");
            put("任选单式-二中二", "2,11,2");
            put("任选单式-三中三", "3,11,2");
            put("任选单式-四中四", "4,11,2");
            put("任选单式-五中五", "5,11,2");
            put("任选单式-六中五", "6,11,2");
            put("任选单式-七中五", "7,11,2");
            put("任选单式-八中五", "8,11,2");

            //对决-对决
            put("对决-对决", "2,10,2");
            put("竞速-竞速", "2,10,2");
        }
    };
    //输入筛选正则
    public static final String INPUT_REGEX = "[,;；，\n]+";

    // 计算每个数字的热值
    public synchronized static Map<String, Integer> calculateHotValues(List<String> lotteryNumbers, List<String> betNumbers) {
        Map<String, Integer> hotValues = new HashMap<>();

        // 初始化热值为0
        for (String number : betNumbers) {
            hotValues.put(number, 0);
        }

        // 遍历开奖号码，更新热值
        for (String lotteryNumber : lotteryNumbers) {
            String[] numbers = lotteryNumber.split(SPLIT);
            for (String num : numbers) {
                hotValues.put(num, hotValues.get(num) + 1);
            }
        }

        return hotValues;
    }

    // 计算距离最近一次出现的遗漏次数
    public synchronized static Map<String, Integer> calculateMissedCounts(List<String> lotteryNumbers, List<String> betNumbers) {
        Map<String, Integer> missedCounts = new HashMap<>();

        for (String number : betNumbers) {
            int missedCount = 0;
            boolean found = false;

            // 遍历每个开奖号码，拆分为单个数字后查找
            for (String lotteryNumber : lotteryNumbers) {
                String[] numbers = lotteryNumber.split(SPLIT);
                for (String num : numbers) {
                    if (num.equals(number)) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    break;
                }
                missedCount++;
            }

            // 如果找到了数字，更新其遗漏次数
            if (found) {
                missedCounts.put(number, missedCount);
            }
        }

        return missedCounts;
    }

    // 计算遗漏次数最多一次的值
    public static Map<String, Integer> calculateMaxMissingValue(List<String> lotteryNumbers, List<String> validNumbers) {
        Map<String, Integer> maxMissedCounts = new HashMap<>();

        for (String number : validNumbers) {
            int maxMissedCount = 0;
            int currentMissedCount = 0;

            // 遍历开奖号码，查找每个数字的最长连续遗漏次数
            for (String lotteryNumber : lotteryNumbers) {
                String[] numbers = lotteryNumber.split(SPLIT);
                boolean found = false;
                for (String num : numbers) {
                    if (num.equals(number)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    currentMissedCount++;
                    maxMissedCount = Math.max(maxMissedCount, currentMissedCount);
                } else {
                    currentMissedCount = 0;
                }
            }
            // 更新最长连续遗漏次数
            maxMissedCounts.put(number, maxMissedCount);
        }

        return maxMissedCounts;
    }

    // 返回最大值
    public static int getMaxValue(Map<String, Integer> map) {
        int maxValue = Integer.MIN_VALUE;
        for (int value : map.values()) {
            if (value > maxValue) {
                maxValue = value;
            }
        }
        return maxValue;
    }

    // 返回最小值
    public static int getMinValue(Map<String, Integer> map) {
        int minValue = Integer.MAX_VALUE;
        for (int value : map.values()) {
            if (value < minValue) {
                minValue = value;
            }
        }
        return minValue;
    }

    // 返回符合规则的号码集合
    public static Set<String> getValidNumbers(String input, int numCount, int numMax, int digitCount) {
        Set<String> validNumbers = new HashSet<>();

        String[] numbers = input.split(INPUT_REGEX);
        for (String number : numbers) {
            if (isValid(number, numCount, numMax, digitCount)) {
                validNumbers.add(number);
            }
        }
        return validNumbers;
    }

    // 返回重复号码集合
    public static Set<String> getDuplicateNumbers(String input, int numCount, int numMax, int digitCount) {
        Set<String> allNumbers = new HashSet<>();
        Set<String> duplicates = new HashSet<>();
        String[] numbers = input.split(INPUT_REGEX);
        for (String number : numbers) {
            if (!allNumbers.add(number)) {
                duplicates.add(number);
            }
        }
        return duplicates;
    }

    // 返回不符合规则的字符数组
    public static String[] getInvalidCharacters(String input, int numCount, int numMax, int digitCount) {
        Set<String> invalidChars = new HashSet<>();
        String[] numbers = input.split(INPUT_REGEX);
        for (String number : numbers) {
            if (!isValid(number, numCount, numMax, digitCount)) {
                invalidChars.add(number);
            }
        }
        return invalidChars.toArray(new String[0]);
    }

//    // 检查号码是否符合规则
//    private static boolean isValid(String number, int digitCount) {
//        String regex = "\\d{" + digitCount + "}";
//        return number.matches(regex);
//    }

    public static boolean isValid(String input, int numCount, int numMax, int digitCount) {
        // 1. 按空格拆分字符串 A
        String[] numbers = input.split(" ");

        // 2. 统计合法的数字数量
        int validCount = 0;

        for (String number : numbers) {
            // 3. 检查数字的位数是否等于 B
            if (number.length() != digitCount) {
                continue; // 位数不符合，跳过
            }

            // 4. 检查数字是否在合法范围内 (不超过 C)
            try {
                int num = Integer.parseInt(number);  // 转换为整数
                if (num <= numMax) {
                    validCount++;  // 统计合法数字
                }
            } catch (NumberFormatException e) {
                // 如果转换失败，说明这不是一个有效的数字字符串
                continue;
            }
        }

        // 5. 检查合法数字的数量是否为 D
        return validCount == numCount;
    }

    // 返回符合规则的字符并删除重复号码和不规则字符
    public static String getCleanValidNumbers(String input, int numCount, int numMax, int digitCount) {
        StringBuilder cleanNumbers = new StringBuilder();
        Set<String> validNumbers = getValidNumbers(input, numCount, numMax, digitCount);
        for (String number : validNumbers) {
            cleanNumbers.append(number).append(SPLIT_1);
        }
        return cleanNumbers.toString().trim();
    }

    public static MissingCodesConfig.MissingCode findMissingCodes(String alias) {
        MissingCodesConfig.MissingCode missConf = null;
        for (MissingCodesConfig.MissingCode item : MissingCodesConfig.MISSING_CODES) {
            if (ExKt.includes(item.alias, alias)) {
                missConf = item;
                break; // 找到后立即跳出循环，提高性能
            }
        }
        if (missConf != null) {
            System.out.println("找到匹配项: " + missConf.codes);
        } else {
            System.out.println("未找到匹配项");
        }

        return missConf;
    }
}
