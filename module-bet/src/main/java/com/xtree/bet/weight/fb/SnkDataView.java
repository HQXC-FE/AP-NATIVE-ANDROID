package com.xtree.bet.weight.fb;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.xtree.bet.R;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.constant.FBConstants;
import com.xtree.bet.weight.BaseDetailDataView;

/**
 * 网球相关数据view(第一盘，第二盘比分等)
 * 需要展示例如“三局二胜”和“总分”这种附加详情的球种有：网球，排球，沙滩排球，
 * 羽毛球，乒乓球，斯诺克（不区分列表和详情）。
 */
public class SnkDataView extends BaseDetailDataView {

    public SnkDataView(@NonNull Context context, Match match ,boolean isMatchList) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.bt_layout_basket_data, this);
        root = findViewById(R.id.ll_root);
        periods = new String[]{"13002", "13003", "13004", "13005", "13006", "13007", "13008", "13009"};
        scoreType = new String[]{String.valueOf(FBConstants.SCORE_TYPE_SNK_JF)};
        setMatch(match, isMatchList);
        addMatchListAdditional(match.getFormat() + " 总分");
    }

    
}
