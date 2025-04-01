package com.xtree.bet.weight.pm;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;

import com.xtree.bet.R;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.weight.BaseDetailDataView;

/**
 * 网球相关数据view(第一盘，第二盘比分等)
 * 需要展示例如“三局二胜”和“总分”这种附加详情的球种有：网球，排球，沙滩排球，
 * 羽毛球，乒乓球，斯诺克（不区分列表和详情）。
 */
public class SnkDataView extends BaseDetailDataView {

    public SnkDataView(@NonNull Context context, Match match, boolean isMatchList) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.bt_layout_basket_data, this);
        root = findViewById(R.id.ll_root);
        //最多35局,PM接口文档35局是s154
        periods = new String[]{"S120", "S121", "S122", "S123", "S124"
                        ,"S125", "S126", "S127", "S128", "S129", "S130", "S131", "S132"
                ,"S133", "S134", "S135", "S136", "S137", "S138", "S139", "S140"
                ,"S141", "S142", "S143", "S144", "S145", "S146", "S147", "S148"
                ,"S149", "S150", "S151", "S152", "S153", "S154"};
        scoreType = periods;
        setSnkMatch(match, isMatchList);
        if(match != null && match.isGoingon()){
            addMatchListAdditional(match.getFormat() + " 总分");
        }
    }
}
