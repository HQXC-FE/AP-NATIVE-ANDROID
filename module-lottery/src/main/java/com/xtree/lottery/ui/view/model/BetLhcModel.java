package com.xtree.lottery.ui.view.model;

import androidx.databinding.ObservableField;

import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.base.utils.CfLog;
import com.xtree.lottery.R;

/**
 * Created by KAKA on 2024/5/6.
 * Describe: 数字选注itemdata
 */
public class BetLhcModel extends BindModel {
    public String number = "?";
    public Ball ball;
    public String odds = "?";
    public ObservableField<String> money = new ObservableField<>("");
    public String methodid = "?";
    public String menuid = "?";

    public ObservableField<String> getMoney() {
        return money;
    }

    public void setMoney(ObservableField<String> money) {
        this.money = money;
    }

    public BetLhcModel(String number, Ball ball, String odds, String methodid, String menuid) {
        this.ball = ball;
        this.number = number;
        this.odds = odds;
        this.methodid = methodid;
        this.menuid = menuid;
    }

    public enum Ball {
        RED("red"),
        BLUE("blue"),
        GREEN("green");

        private final String color;
        private final int resourceId;

        // 构造方法
        Ball(String color) {
            this.color = color;
            this.resourceId = getResourceIdByColor(color); // 根据color获取resourceId
        }

        // 根据 color 字符串获取对应的资源ID
        public static int getResourceIdByColor(String color) {
            switch (color.toLowerCase()) {
                case "red":
                    return R.mipmap.lottery_lhc_ball_red;
                case "blue":
                    return R.mipmap.lottery_lhc_ball_blue;
                case "green":
                    return R.mipmap.lottery_lhc_ball_green;
                default:
                    // 如果未找到匹配的颜色，可以返回一个默认资源ID或抛出异常
                    CfLog.e("Unknown color: " + color);
                    return R.mipmap.lottery_lhc_ball_red;
            }
        }

        // 根据color获取对应的枚举实例
        public static Ball getBallByColor(String color) {
            for (Ball ball : Ball.values()) {
                if (ball.color.equalsIgnoreCase(color)) {
                    return ball;
                }
            }
            CfLog.e("Unknown color: " + color);
            return RED;
        }

        // 获取color属性
        public String getColor() {
            return color;
        }

        // 获取resourceId
        public int getResourceId() {
            return resourceId;
        }
    }


}
