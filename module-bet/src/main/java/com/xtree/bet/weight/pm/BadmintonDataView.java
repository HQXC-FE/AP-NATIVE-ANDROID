package com.xtree.bet.weight.pm;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.xtree.bet.R;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.constant.FBConstants;
import com.xtree.bet.weight.BaseDetailDataView;

/**
 * 羽毛球相关数据view(第一局，第二局比分等)
 * 需要展示例如“三局二胜”和“总分”这种附加详情的球种有：网球，排球，沙滩排球，
 * 羽毛球，乒乓球，斯诺克（不区分列表和详情）。
 */
public class BadmintonDataView extends BaseDetailDataView {

    public BadmintonDataView(@NonNull Context context, Match match, boolean isMatchList) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.bt_layout_basket_data, this);
        root = findViewById(R.id.ll_root);
        periods = new String[]{"S120", "S121", "S122"};
        scoreType = periods;
        setMatch(match, isMatchList);
        if(isMatchList) {
            addMatchListAdditional(match.getFormat() + " 总分");
        }
    }

    
}
