package com.xtree.bet.weight;

import static com.xtree.base.utils.BtDomainUtil.KEY_PLATFORM;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_PM;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_PMXC;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.xtree.base.utils.CfLog;
import com.xtree.bet.R;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.Score;
import com.xtree.bet.constant.FBConstants;
import com.xtree.bet.constant.PMConstants;
import com.xtree.bet.weight.fb.BadmintonDataView;
import com.xtree.bet.weight.fb.BasketDataView;
import com.xtree.bet.weight.fb.FbDataView;
import com.xtree.bet.weight.fb.IceBallDataView;
import com.xtree.bet.weight.fb.NetBallDataView;
import com.xtree.bet.weight.fb.SnkDataView;
import com.xtree.bet.weight.fb.StVolleyballDataView;
import com.xtree.bet.weight.fb.TableTennisDataView;
import com.xtree.bet.weight.fb.VolleyballDataView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.xtree.mvvmhabit.utils.ConvertUtils;
import me.xtree.mvvmhabit.utils.SPUtils;

public abstract class BaseDetailDataView extends ConstraintLayout{
    protected LinearLayout root;
    /**
     * 要获取比分的阶段
     */
    protected String[] periods;
    /**
     * 要获取的比分类型
     */
    protected String[] scoreType;
    protected List<Score> scores;
    /**
     * 区分比赛列表还是比赛详情
     */
    private boolean isDisplayMatchList;
    public BaseDetailDataView(@NonNull Context context) {
        super(context);
    }

    public BaseDetailDataView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseDetailDataView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 斯诺克比分最多有35局，全部比分不容易展示，单独处理
     */
    public void setSnkMatch(Match match, boolean isMatchList) {
        isDisplayMatchList = isMatchList;
        List<Score> scoreList = match.getScoreList(scoreType);
        scores = new ArrayList<>();
        if (periods == null) {
            return;
        }
        List<String> periodList = Arrays.asList(periods);
        // 过滤符合条件的比分
        for (Score score : scoreList) {
            if (periodList.contains(score.getPeriod())) {
                scores.add(score);
            }
        }
        root.removeAllViews(); // 避免重复创建视图，先清空已有视图
        CfLog.i("setMatch getMct ====  "+match.getMct());
        int size = scores.size();
        for (int i = 0; i < size; i++) {
            TextView textView = new TextView(getContext());
            // 设置 LayoutParams
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.leftMargin = ConvertUtils.dp2px(5);
            textView.setLayoutParams(params);

            // 计算颜色
            int color = (i == size - 1) ? R.color.bt_color_car_dialog_hight_line2
                    : (isMatchList ? R.color.bt_text_color_primary : R.color.bt_color_under_bg_primary_text);
            textView.setTextColor(getResources().getColor(color));
            textView.setTextSize(10);

            // 处理比分显示逻辑,斯诺克只展示最近5局比分比和第1局比分
            if (i == 1 && i < size - 5) {
                textView.setText("......"); // 第二个位置显示省略号
                root.addView(textView);
                continue;
            } else if (i > 1 && i < size - 5) {
                continue; // 直接跳过中间部分
            }
            Score score = scores.get(i);
            String scoreText = getResources().getString(R.string.bt_detail_score, score.getScores().get(0), score.getScores().get(1));
            textView.setText(scoreText);

            root.addView(textView);
        }

    }

    public void setMatch(Match match, boolean isMatchList){
        isDisplayMatchList = isMatchList;
        List<Score> scoreList = match.getScoreList(scoreType);
        scores = new ArrayList<>();
        if(periods == null){
            return;
        }
        List<String> periodList = Arrays.asList(periods);
        for(Score score : scoreList){
            if(periodList.contains(score.getPeriod())){
                scores.add(score);
            }
        }

        for(int i = 0; i < scores.size(); i ++){
            Score score = scores.get(i);
            TextView textView;
            if(root.getChildAt(i) == null){
                textView = new TextView(getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin = ConvertUtils.dp2px(5);
                textView.setLayoutParams(params);
                int color = i == scores.size() - 1 ? R.color.bt_color_car_dialog_hight_line2 : isMatchList ? R.color.bt_text_color_primary : R.color.bt_color_under_bg_primary_text;
                textView.setTextColor(getResources().getColor(color));
                textView.setTextSize(10);
            }else{
                textView = (TextView) root.getChildAt(i);
                int color = i == scores.size() - 1 ? R.color.bt_color_car_dialog_hight_line2 : isMatchList ? R.color.bt_text_color_primary : R.color.bt_color_under_bg_primary_text;
                textView.setTextColor(getResources().getColor(color));
            }

            textView.setText(getResources().getString(R.string.bt_detail_score, score.getScores().get(0), score.getScores().get(1)));
            if(root.getChildAt(i) == null) {
                root.addView(textView);
            }

        }
    }

    /**
     * 获取总分
     * @return
     */
    public String getTotalScore(){
        int mainScore = 0;
        int visitorScore = 0;
        for (Score score : scores) {
            mainScore += score.getScores().get(0);
            visitorScore += score.getScores().get(1);
        }
        return mainScore + "-" + visitorScore + "(" + (mainScore + visitorScore) + ")";
    }

    /**
     * 增加赛事列表附加数据 ，如三盘两胜 总局数等
     */
    public void addMatchListAdditional(String info){
        CfLog.d("=========== addMatchListAdditional:info ============== "+info);
        TextView textView = new TextView(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = ConvertUtils.dp2px(5);
        textView.setLayoutParams(params);
        int color = isDisplayMatchList ? R.color.bt_text_color_primary : R.color.bt_color_under_bg_primary_text;
        textView.setTextColor(getResources().getColor(color));
        textView.setTextSize(10);
        textView.setText(info);
        root.addView(textView);

        TextView scoreTotalTextView = new TextView(getContext()); // 总分文本
        LinearLayout.LayoutParams scoreTotalParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        scoreTotalParams.leftMargin = ConvertUtils.dp2px(5);
        scoreTotalTextView.setLayoutParams(scoreTotalParams);
        color = R.color.bt_color_car_dialog_hight_line2;
        scoreTotalTextView.setTextColor(getResources().getColor(color));
        scoreTotalTextView.setTextSize(10);
        scoreTotalTextView.setText(getTotalScore());
        root.addView(scoreTotalTextView);
    }

    /**
     *
     * @param context
     * @param match
     * @param isMatchList 是否赛事列表
     * @return
     */
    public static BaseDetailDataView getInstance(Context context, Match match, boolean isMatchList){
        String platform = SPUtils.getInstance().getString(KEY_PLATFORM);
        if(platform == null || match == null || match.getSportId() == null){
            return null;
        }
        String sport = match.getSportId();
        if (!TextUtils.equals(platform, PLATFORM_PM) && !TextUtils.equals(platform, PLATFORM_PMXC)) {
            if (sport.equals(FBConstants.SPORT_ID_FB)) {
                return new FbDataView(context, match);
            } else if (sport.equals(FBConstants.SPORT_ID_BSB)) {
                return new BasketDataView(context, match, isMatchList);
            } else if (sport.equals(FBConstants.SPORT_ID_WQ)) {
                return new NetBallDataView(context, match, isMatchList);
            } else if (sport.equals(FBConstants.SPORT_ID_PQ)) {
                return new VolleyballDataView(context, match, isMatchList);
            } else if (sport.equals(FBConstants.SPORT_ID_STPQ)) {
                return new StVolleyballDataView(context, match, isMatchList);
            } else if (sport.equals(FBConstants.SPORT_ID_YMQ)) {
                return new BadmintonDataView(context, match, isMatchList);
            } else if (sport.equals(FBConstants.SPORT_ID_BBQ)) {
                return new TableTennisDataView(context, match, isMatchList);
            } else if (sport.equals(FBConstants.SPORT_ID_BQ)) {
                return new IceBallDataView(context, match, isMatchList);
            } else if (sport.equals(FBConstants.SPORT_ID_SNK)) {
                return new SnkDataView(context, match, isMatchList);
            }
        }else{
            if (sport.equals(PMConstants.SPORT_ID_FB)) {
                return new com.xtree.bet.weight.pm.FbDataView(context, match, isMatchList);
            } else if (sport.equals(PMConstants.SPORT_ID_BSB)) {
                return new com.xtree.bet.weight.pm.BasketDataView(context, match, isMatchList);
            } else if (sport.equals(PMConstants.SPORT_ID_WQ)) {
                return new com.xtree.bet.weight.pm.NetBallDataView(context, match, isMatchList);
            } else if (sport.equals(PMConstants.SPORT_ID_PQ)) {
                return new com.xtree.bet.weight.pm.VolleyballDataView(context, match, isMatchList);
            } else if (sport.equals(PMConstants.SPORT_ID_STPQ)) {
                return new com.xtree.bet.weight.pm.StVolleyballDataView(context, match, isMatchList);
            } else if (sport.equals(PMConstants.SPORT_ID_YMQ)) {
                return new com.xtree.bet.weight.pm.BadmintonDataView(context, match, isMatchList);
            } else if (sport.equals(PMConstants.SPORT_ID_BBQ)) {
                return new com.xtree.bet.weight.pm.TableTennisDataView(context, match, isMatchList);
            } else if (sport.equals(PMConstants.SPORT_ID_BQ)) {
                return new com.xtree.bet.weight.pm.IceBallDataView(context, match, isMatchList);
            } else if (sport.equals(PMConstants.SPORT_ID_SNK)) {
                return new com.xtree.bet.weight.pm.SnkDataView(context, match, isMatchList);
            }
        }
        return null;
    }

    public boolean hasScore(List<Integer> score){
        return score.get(0) > 0 || score.get(1) > 0;
    }
}
