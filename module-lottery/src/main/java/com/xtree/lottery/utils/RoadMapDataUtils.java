package com.xtree.lottery.utils;


import com.xtree.lottery.R;
import com.xtree.lottery.data.source.vo.RecentLotteryVo;
import com.xtree.lottery.ui.view.model.RoadMapDLItemModel;
import com.xtree.lottery.ui.view.model.RoadMapZLItemModel;

import java.util.ArrayList;
import java.util.List;

public class RoadMapDataUtils {

    public static List<RoadMapZLItemModel> makeBjlZLData(List<RecentLotteryVo> recentLotteryVos) {
        List<RoadMapZLItemModel> roadMapZLItemModelList = new ArrayList<>();

        for (RecentLotteryVo recentLotteryVo : recentLotteryVos) {
            int[] numbers = convertListToIntArray(recentLotteryVo.getSplit_code());
            String result = calculateResult(numbers);
            RoadMapZLItemModel bean = new RoadMapZLItemModel();
            bean.setBJL(true);
            if (result.startsWith("庄")) {
                if (result.length() > 1) {
                    bean.setText(result.replace("庄", ""));
                    bean.setTextBgDrawableId(R.drawable.bg_roadmap_item_text_red);
                    roadMapZLItemModelList.add(bean);
                } else {
                    bean.setText("庄");
                    bean.setTextBgDrawableId(R.drawable.bg_roadmap_item_text_red);
                    roadMapZLItemModelList.add(bean);
                }

            } else if (result.startsWith("闲")) {
                if (result.length() > 1) {
                    bean.setText(result.replace("闲", ""));
                    bean.setTextBgDrawableId(R.drawable.bg_roadmap_item_text_blue);
                    roadMapZLItemModelList.add(bean);
                } else {
                    bean.setText("闲");
                    bean.setTextBgDrawableId(R.drawable.bg_roadmap_item_text_blue);
                    roadMapZLItemModelList.add(bean);
                }
            } else {
                if (result.length() > 1) {
                    bean.setText(result.replace("和", ""));
                    bean.setTextBgDrawableId(R.drawable.bg_roadmap_item_text_green);
                    roadMapZLItemModelList.add(bean);
                } else {
                    bean.setText("和");
                    bean.setTextBgDrawableId(R.drawable.bg_roadmap_item_text_green);
                    roadMapZLItemModelList.add(bean);
                }
            }

        }

        return roadMapZLItemModelList;
    }


    /**
     * 判断庄、闲、和及对子结果
     *
     * @param numbers 河内五分彩开奖号码（五位数字数组）
     * @return 结果字符串（包含庄、闲、和及对子）
     */
    public static String calculateResult(int[] numbers) {
        if (numbers == null || numbers.length < 5) {
            throw new IllegalArgumentException("开奖号码必须是五位数字");
        }

        // 获取庄的点数：前两位数字之和的个位
        int bankerPoints = (numbers[0] + numbers[1]) % 10;

        // 获取闲的点数：第4、5位数字之和的个位
        int playerPoints = (numbers[3] + numbers[4]) % 10;

        StringBuilder sb = new StringBuilder();
        if (bankerPoints > 7) {
            if(playerPoints > 7){
                sb.append(bankerPoints + ":" + playerPoints);
            }else {
                sb.append(bankerPoints);
            }
        } else {
            if(playerPoints>7){
                sb.append(playerPoints);
            }
        }


        // 比较庄和闲的点数
        String result;
        if (bankerPoints > playerPoints) {
            result = "庄" + sb;
        } else if (bankerPoints < playerPoints) {
            result = "闲" + sb;
        } else {
            result = "和" + sb;
        }

        return result;
    }


    public static List<RoadMapZLItemModel> makeNiuNiuZLData(List<RecentLotteryVo> recentLotteryVos, String playType) {

        List<RoadMapZLItemModel> roadMapZLItemModelList = new ArrayList<>();
        for (RecentLotteryVo recentLotteryVo : recentLotteryVos) {
            int[] numbers = convertListToIntArray(recentLotteryVo.getSplit_code());
            String result = calculateBull(numbers);

            if (playType.equals("dx")) {

                if (result.contains("大") || result.contains("牛牛")) {
                    roadMapZLItemModelList.add(new RoadMapZLItemModel("大", R.drawable.bg_roadmap_item_text_blue));
                } else if (result.contains("小")) {
                    roadMapZLItemModelList.add(new RoadMapZLItemModel("小", R.drawable.bg_roadmap_item_text_red));
                } else if (result.contains("无")) {
                    roadMapZLItemModelList.add(new RoadMapZLItemModel("无", R.drawable.bg_roadmap_item_text_none));
                }

            } else if (playType.equals("ds")) {

                if (result.contains("双") || result.contains("牛牛")) {
                    roadMapZLItemModelList.add(new RoadMapZLItemModel("双", R.drawable.bg_roadmap_item_text_blue));
                } else if (result.contains("单")) {
                    roadMapZLItemModelList.add(new RoadMapZLItemModel("单", R.drawable.bg_roadmap_item_text_red));
                } else if (result.contains("无")) {
                    roadMapZLItemModelList.add(new RoadMapZLItemModel("无", R.drawable.bg_roadmap_item_text_none));
                }

            }

        }

        return roadMapZLItemModelList;
    }

    public static int[] convertListToIntArray(List<String> stringList) {
        if (stringList == null || stringList.isEmpty()) {
            return new int[0];
        }

        int[] result = new int[stringList.size()];
        for (int i = 0; i < stringList.size(); i++) {
            try {
                result[i] = Integer.parseInt(stringList.get(i));
            } catch (NumberFormatException e) {
                // 出现异常时默认处理为 0 或者其他逻辑
                result[i] = 0;
            }
        }
        return result;
    }


    /**
     * 计算牛牛玩法结果
     *
     * @param numbers 输入5个开奖号码
     * @return 牛牛玩法结果（如：牛大、牛单、无牛）
     */
    private static String calculateBull(int[] numbers) {
        if (numbers == null || numbers.length != 5) {
            throw new IllegalArgumentException("必须输入五个号码！");
        }

        // 检查是否有牛
        int totalSum = 0;
        for (int num : numbers) {
            totalSum += num;
        }

        // 任意三数的和模10 == 0
        for (int i = 0; i < numbers.length - 2; i++) {
            for (int j = i + 1; j < numbers.length - 1; j++) {
                for (int k = j + 1; k < numbers.length; k++) {
                    int threeSum = numbers[i] + numbers[j] + numbers[k];
                    if (threeSum % 10 == 0) {
                        // 剩余两数的和
                        int remainingSum = totalSum - threeSum;
                        int bullPoints = remainingSum % 10;

                        if (bullPoints == 0) {
                            return "牛牛";
                        } else {
                            String size = (bullPoints >= 6) ? "大" : "小";
                            String parity = (bullPoints % 2 == 0) ? "双" : "单";
                            return size + "," + parity;
                        }
                    }
                }
            }
        }

        return "无";
    }


    public static List<RoadMapZLItemModel> makeLongHuZLData(List<RecentLotteryVo> recentLotteryVos, String playType) {

        List<RoadMapZLItemModel> roadMapZLItemModelList = new ArrayList<>();

        String[] s = playType.split(",");
        int fA = Integer.parseInt(s[0]);
        int fB = Integer.parseInt(s[1]);

        for (RecentLotteryVo recentLotteryVo : recentLotteryVos) {
            int dragon = Integer.parseInt(recentLotteryVo.getSplit_code().get(fA));
            int tiger = Integer.parseInt(recentLotteryVo.getSplit_code().get(fB));
            if (dragon > tiger) {
                roadMapZLItemModelList.add(new RoadMapZLItemModel("龙", R.drawable.bg_roadmap_item_text_red));
            } else if (dragon == tiger) {
                roadMapZLItemModelList.add(new RoadMapZLItemModel("虎", R.drawable.bg_roadmap_item_text_blue));
            } else {
                roadMapZLItemModelList.add(new RoadMapZLItemModel("和", R.drawable.bg_roadmap_item_text_green));
            }
        }
        return roadMapZLItemModelList;
    }


    public static List<RoadMapZLItemModel> makeZongHengZLData(String weishu, String playType
            , List<RecentLotteryVo> recentLotteryVos) {

        List<RoadMapZLItemModel> roadMapZLItemModelList = new ArrayList<>();

        for (RecentLotteryVo recentLotteryVo : recentLotteryVos) {
            ArrayList<String> nums = recentLotteryVo.getSplit_code();
            if (!nums.isEmpty() || nums.size() == 5) {
                RoadMapZLItemModel bean = null;
                String num = "";
                if (weishu.equals("个位")) {
                    num = nums.get(4);
                } else if (weishu.equals("十位")) {
                    num = nums.get(3);
                } else if (weishu.equals("百位")) {
                    num = nums.get(2);
                } else if (weishu.equals("千位")) {
                    num = nums.get(1);
                } else if (weishu.equals("万位")) {
                    num = nums.get(0);
                }

                if (playType.equals("dx")) {
                    if (Integer.parseInt(num) > 4) {
                        bean = new RoadMapZLItemModel("大", R.drawable.bg_roadmap_item_text_blue);
                    } else {
                        bean = new RoadMapZLItemModel("小", R.drawable.bg_roadmap_item_text_red);
                    }
                } else if (playType.equals("ds")) {
                    if (isOdd(Integer.parseInt(num))) {
                        bean = new RoadMapZLItemModel("单", R.drawable.bg_roadmap_item_text_blue);
                    } else {
                        bean = new RoadMapZLItemModel("双", R.drawable.bg_roadmap_item_text_red);
                    }
                }

                if (bean != null) {
                    roadMapZLItemModelList.add(bean);
                }
            }
        }

        return roadMapZLItemModelList;

    }


    public static boolean isOdd(int number) {
        // 判断数字是否是单数（奇数）
        return (number & 1) == 1;
    }


    public static List<RoadMapDLItemModel> makeResultDLData(List<RoadMapZLItemModel> roadMapZLItemModelList) {
        List<RoadMapDLItemModel> roadMapDLItemModelList = new ArrayList<>();

        int recordColor = -1;
        int countKeepColor = 0;

        for (int i = 0; i < roadMapZLItemModelList.size(); i++) {
            int s = roadMapZLItemModelList.get(i).getTextBgDrawableId();
            boolean isBjl = roadMapZLItemModelList.get(i).isBJL();
            if (recordColor == -1) {
                recordColor = s;
                setColorType(s, roadMapDLItemModelList);
            } else if (recordColor == s) {
                if (countKeepColor < 5) { //已经有连续6个，后面同色的不算入大路
                    setColorType(s, roadMapDLItemModelList);
                    countKeepColor++;
                }

            } else { //出现变化
                int cSize = roadMapDLItemModelList.size();
                int remainder = cSize % 6;
                if (remainder == 0) {//刚好填充一列 自动变路
                    setColorType(s, roadMapDLItemModelList);
                } else if (s == R.drawable.bg_roadmap_item_text_none) { //碰到灰色   不变路
                    setColorType(s, roadMapDLItemModelList);
                } else if (isBjl && s == R.drawable.bg_roadmap_item_text_green) { //百家乐碰到绿色  不变路
                    setColorType(s, roadMapDLItemModelList);
                } else { //变路
                    for (int y = 0; y < 6 - remainder; y++) {
                        roadMapDLItemModelList.add(new RoadMapDLItemModel(5));
                    }
                    setColorType(s, roadMapDLItemModelList);
                }

                recordColor = s;
                countKeepColor = 0;

            }

        }

        return roadMapDLItemModelList;

    }


    private static void setColorType(int s, List<RoadMapDLItemModel> roadMapDLItemModelList) {
        if (s == R.drawable.bg_roadmap_item_text_blue) {
            roadMapDLItemModelList.add(new RoadMapDLItemModel(1));
        } else if (s == R.drawable.bg_roadmap_item_text_red) {
            roadMapDLItemModelList.add(new RoadMapDLItemModel(2));
        } else if (s == R.drawable.bg_roadmap_item_text_green) {
            roadMapDLItemModelList.add(new RoadMapDLItemModel(3));
        } else if (s == R.drawable.bg_roadmap_item_text_none) {
            roadMapDLItemModelList.add(new RoadMapDLItemModel(4));
        }
    }


}
