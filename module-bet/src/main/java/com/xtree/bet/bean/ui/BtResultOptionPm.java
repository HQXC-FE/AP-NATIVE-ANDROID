package com.xtree.bet.bean.ui;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.xtree.bet.R;
import com.xtree.bet.bean.response.pm.BtRecordRsp;

import java.util.HashMap;
import java.util.Map;

@SuppressLint("ParcelCreator")
public class BtResultOptionPm implements BtResultOption {
    private BtRecordRsp.RecordsBean.DetailBean detailBean;

    private static Map<String, String> btResultMap = new HashMap<>();

    static {
        btResultMap.put("0", "无结果");
        btResultMap.put("2", "走水");
        btResultMap.put("3", "输");
        btResultMap.put("4", "赢");
        btResultMap.put("5", "赢半");
        btResultMap.put("6", "输半");
        btResultMap.put("7", "玩法取消");
        btResultMap.put("8", "赛事延期");
        btResultMap.put("11", "比赛延迟");
        btResultMap.put("12", "比赛中断");
        btResultMap.put("13", "未知");
        btResultMap.put("15", "比赛放弃");
        btResultMap.put("16", "异常盘口");
    }

    public BtResultOptionPm(BtRecordRsp.RecordsBean.DetailBean detailBean) {
        this.detailBean = detailBean;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

    }

    @Override
    public String getLeagueName() {
        return detailBean.matchName;
    }

    @Override
    public long getMatchTime() {
        if (detailBean == null || detailBean.beginTime == null) {
            return 0;
        }
        return Long.valueOf(detailBean.beginTime);
    }

    @Override
    public String getOptionName() {
        return detailBean.marketValue;
    }

    @Override
    public String getPlayType() {
        return detailBean.playName;
    }

    @Override
    public String getScore() {
        if (detailBean == null || TextUtils.isEmpty(detailBean.scoreBenchmark)) {
            return "";
        }
        return detailBean.scoreBenchmark;
    }

    @Override
    public String getOdd() {
        return "@" + detailBean.oddFinally;
    }

    @Override
    public String getTeamName() {
        return detailBean.matchInfo;
    }
    @Override
    public String getBtResult() {
        return btResultMap.get(String.valueOf(detailBean.betResult));
    }

    @Override
    public String getFullScore() {
        if (detailBean == null) {
            return "";
        }
        //5387单子修改  PM注单记录页展示比分
        if (detailBean.matchType == 1) {//早盘赛事
            //1.1.未结算注单为【无记录】文案  已结算注单保持原样
            if (detailBean.betStatus == 0) {
                return "【无记录】";
            }
        } else if (detailBean.matchType == 2) {//滚球盘赛事
            if (detailBean.betStatus == 0) {//2.1.未结算注单为实时比分
                return detailBean.scoreBenchmark;//实时比分
            } else if (detailBean.betStatus == 1) {
                //2.2.提早结算注单为实时比分   提前结算的，拉单接口会返回提前结算金额
                if (detailBean.preBetAmount != null && detailBean.preBetAmount != 0.0) {
                    return detailBean.scoreBenchmark;//实时比分
                }
            } else if (detailBean.betStatus == 3) {
                //2.4.滚球时的取消注单为
                //2.4.1.有比分的状况：实时比分
                //2.4.2.无比分的状况：【无记录】文案
                if (!TextUtils.isEmpty(detailBean.scoreBenchmark)) {
                    return detailBean.scoreBenchmark;
                } else {
                    return "【无记录】";
                }
            }
        }
        //3.取消赛事／取消盘口／取消投注项：【取消赛事】
        if (detailBean.cancelType == 1) {
            return "【取消赛事】";
        }

        if (TextUtils.isEmpty(detailBean.settleScore)) {
            return "";
        }
        return detailBean.settleScore;//结算比分
    }

    @Override
    public boolean isSettled() {
        return detailBean.betStatus == 1;
    }

    @Override
    public int getResultColor() {

        //投注项结算结果 0：无结果，2：走水，3：输，4：赢，5：赢一半， 6：输一半，7：赛事取消，8：赛事延期， 11：比赛延迟，12：比赛中断，13：未知， 15：比赛放弃，16：异常盘口

        if (detailBean.betResult == 4 || detailBean.betResult == 5) {
            return R.color.bt_color_option_win;
        } else if (detailBean.betResult == 3 || detailBean.betResult == 6) {
            return R.color.bt_color_option_lose;
        } else {
            return R.color.bt_color_option_draw;
        }
    }

    @Override
    public String getMarketType() {
        return TextUtils.equals(detailBean.marketType, "EU") ? "欧洲盘" : "香港盘";
    }
}
