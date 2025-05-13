package com.xtree.bet.weight.im;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.xtree.bet.R;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.constant.FBConstants;
import com.xtree.bet.weight.BaseDetailDataView;

/**
 * 网球相关数据view(第一盘，第二盘比分等)
 */
public class NetBallDataView extends BaseDetailDataView {

    public NetBallDataView(@NonNull Context context, Match match, boolean isMatchList) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.bt_layout_basket_data, this);
        root = findViewById(R.id.ll_root);
        periods = new String[]{"5002", "5003", "5004", "5005", "5006"};
        scoreType = new String[]{String.valueOf(FBConstants.SCORE_TYPE_PF)};
        setMatch(match, isMatchList);
        if(isMatchList) {
            addMatchListAdditional(match.getFormat() + " 总局数");
        }
    }

}
