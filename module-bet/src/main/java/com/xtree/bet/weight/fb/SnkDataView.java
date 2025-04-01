package com.xtree.bet.weight.fb;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.xtree.bet.R;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.constant.FBConstants;
import com.xtree.bet.weight.BaseDetailDataView;

/**
 * 斯诺克相关数据view(第一盘，第二盘比分等)
 * 需要展示例如“三局二胜”和“总分”这种附加详情的球种有：网球，排球，沙滩排球，
 * 羽毛球，乒乓球，斯诺克（不区分列表和详情）。
 */
public class SnkDataView extends BaseDetailDataView {

    public SnkDataView(@NonNull Context context, Match match ,boolean isMatchList) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.bt_layout_basket_data, this);
        root = findViewById(R.id.ll_root);
        //以下是FB斯诺克每场比赛的ID,API定义35局
//        periods = new String[]{"16101", "16102", "16103", "16104", "16105", "16106", "16107", "16108"
//                ,"16109", "16110", "16111", "16112", "16113", "16114", "16115", "16116"
//                ,"16117", "16118", "16119", "16120", "16121", "16122", "16123", "16124"
//                ,"16125", "16126", "16127", "16128", "16129", "16130", "16131", "16132"
//                ,"16133", "16134", "16135"};
        periods = new String[]{"16001", "16002", "16003", "16004", "16005", "16006", "16007", "16008"
                ,"16009", "16100", "16101", "16102", "16103", "16104", "16105", "16106"
                ,"16107", "16108", "16109", "16110", "16111", "16112", "16113", "16114"
                ,"16115", "16116", "16117", "16118", "16119", "16120", "16121", "16122"
                ,"16123", "16124", "16125"};
        scoreType = new String[]{String.valueOf(FBConstants.SCORE_TYPE_SNK_JF)};
        setSnkMatch(match, isMatchList);
        if(match != null && match.isGoingon()){
            addMatchListAdditional(match.getFormat() + " 总分");
        }
    }

    
}
