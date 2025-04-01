package com.xtree.bet.ui.adapter;

import static com.xtree.base.utils.BtDomainUtil.KEY_PLATFORM;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_PM;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_PMXC;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.ClickUtil;
import com.xtree.base.utils.TimeUtils;
import com.xtree.bet.R;
import com.xtree.bet.bean.ui.BetConfirmOption;
import com.xtree.bet.bean.ui.BetConfirmOptionUtil;
import com.xtree.bet.bean.ui.League;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.Option;
import com.xtree.bet.bean.ui.OptionList;
import com.xtree.bet.bean.ui.PlayGroup;
import com.xtree.bet.bean.ui.PlayGroupFb;
import com.xtree.bet.bean.ui.PlayGroupPm;
import com.xtree.bet.bean.ui.PlayType;
import com.xtree.bet.constant.Constants;
import com.xtree.bet.contract.BetContract;
import com.xtree.bet.databinding.BtFbLeagueGroupBinding;
import com.xtree.bet.databinding.BtFbMatchListBinding;
import com.xtree.bet.manager.BtCarManager;
import com.xtree.bet.ui.activity.BtDetailActivity;
import com.xtree.bet.ui.fragment.BtCarDialogFragment;
import com.xtree.bet.weight.AnimatedExpandableListViewMax;
import com.xtree.bet.weight.BaseDetailDataView;
import com.xtree.bet.weight.DiscolourTextView;
import com.xtree.bet.weight.PageHorizontalScrollView;
import com.xtree.bet.weight.pm.SnkDataView;

import java.util.ArrayList;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseActivity;
import me.xtree.mvvmhabit.bus.RxBus;
import me.xtree.mvvmhabit.utils.ConvertUtils;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

public class LeagueAdapter extends AnimatedExpandableListViewMax.AnimatedExpandableListAdapter {
    private List<League> mDatas;
    private final Context mContext;
    private final String platform;
    private int liveHeaderPosition;
    private int noLiveHeaderPosition;
    private boolean isUpdateFootBallOrBasketBallState;
    private PageHorizontalScrollView.OnScrollListener mOnScrollListener;

    public LeagueAdapter(Context context, List<League> datas) {
        this.mContext = context;
        this.mDatas = datas;
        this.platform = SPUtils.getInstance().getString(KEY_PLATFORM);
        init();
    }

    public void setOnScrollListener(PageHorizontalScrollView.OnScrollListener onScrollListener) {
        this.mOnScrollListener = onScrollListener;
    }

    public void setHeaderIsExpand(int position, boolean headerIsExpand) {
        if (mDatas != null && position < mDatas.size()) {
            mDatas.get(position).setExpand(headerIsExpand);
        }
    }

    public int getLiveHeaderPosition() {
        return liveHeaderPosition;
    }

    public int getNoLiveHeaderPosition() {
        return noLiveHeaderPosition;
    }

    public void setData(List<League> leagueList) {
        this.mDatas = leagueList;
        init();
        notifyDataSetChanged();
    }

    private void init() {
        liveHeaderPosition = 0;
        noLiveHeaderPosition = 0;
        if (mDatas.isEmpty()) return;

        int index = 0;
        for (int i = 0, size = mDatas.size(); i < size; i++) {
            League league = mDatas.get(i);
            if (league.isHead() && league.getHeadType() == League.HEAD_TYPE_LIVE_OR_NOLIVE) {
                if (index == 0) {
                    liveHeaderPosition = i;
                    index++;
                } else {
                    noLiveHeaderPosition = i;
                    break;
                }
            }
        }
    }

    public boolean isHandleGoingOnExpand(int start) {
        return noLiveHeaderPosition > 0 && start - 2 == liveHeaderPosition;
    }

    public String expandRangeLive() {
        int start = liveHeaderPosition + 2;
        int end = noLiveHeaderPosition > 0 ? noLiveHeaderPosition : getGroupCount();
        return start + "/" + end;
    }

    public String expandRangeNoLive() {
        int size = getGroupCount();
        return noLiveHeaderPosition > 0 ? (noLiveHeaderPosition + 2) + "/" + size : "0/" + size;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return (groupPosition >= mDatas.size()) ? 0 : mDatas.get(groupPosition).getMatchList().size();
    }

    @Override
    public int getGroupCount() {
        return mDatas.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupPosition < mDatas.size() ? mDatas.get(groupPosition) : null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (groupPosition >= mDatas.size()) return null;
        List<Match> matchList = mDatas.get(groupPosition).getMatchList();
        return (matchList != null && childPosition < matchList.size()) ? matchList.get(childPosition) : null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (groupPosition >= mDatas.size()) {
            return convertView != null ? convertView : LayoutInflater.from(mContext).inflate(R.layout.bt_fb_league_group, parent, false);
        }

        GroupHolder holder = null;
        if (convertView == null) {
            BtFbLeagueGroupBinding binding = BtFbLeagueGroupBinding.inflate(LayoutInflater.from(mContext), parent, false);
            holder = new GroupHolder(binding);
            convertView = binding.getRoot();
            convertView.setTag(holder);
        } else {
            if(convertView.getTag() instanceof GroupHolder){
                holder = (GroupHolder) convertView.getTag();
            }
        }

        if(holder != null && holder.binding != null){
            League league = mDatas.get(groupPosition);
            BtFbLeagueGroupBinding binding = holder.binding;
            boolean isHead = league.isHead();
            binding.llHeader.setVisibility(isHead ? View.VISIBLE : View.GONE);
            binding.rlLeague.setVisibility(isHead ? View.GONE : View.VISIBLE);

            if (!isHead) {
                configureLeagueView(binding, league, isExpanded);
            } else {
                configureHeader(binding, league, groupPosition);
            }
        }

        return convertView;
    }

    private void configureLeagueView(BtFbLeagueGroupBinding binding, League league, boolean isExpanded) {
        binding.tvLeagueName.setText(league.getLeagueName());
        Glide.with(mContext)
                .load(league.getIcon())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivIcon);
        binding.vSpace.setVisibility(isExpanded ? View.GONE : View.VISIBLE);
        binding.groupIndicator.setImageResource(
                isExpanded ? R.mipmap.bt_icon_expand : R.mipmap.bt_icon_unexpand);
        binding.rlLeague.setBackgroundResource(
                isExpanded ? R.drawable.bt_bg_league_top : R.drawable.bt_bg_league_top_collapse);
        league.setExpand(isExpanded);
    }

    private void configureHeader(BtFbLeagueGroupBinding binding, League league, int groupPosition) {
        if (league.getHeadType() == League.HEAD_TYPE_LIVE_OR_NOLIVE) {
            binding.rlHeader.setVisibility(View.VISIBLE);
            binding.tvSportName.setVisibility(View.GONE);
            binding.ivExpand.setSelected(league.isExpand());
            binding.tvHeaderName.setText(league.getLeagueName());
            binding.ivHeader.setBackgroundResource(getHeaderIcon(league.getLeagueName()));
            binding.rlHeader.setOnClickListener(v -> {
                boolean newExpandState = !league.isExpand();
                binding.ivExpand.setSelected(newExpandState);
                league.setExpand(newExpandState);
                RxBus.getDefault().post(new BetContract(BetContract.ACTION_EXPAND, calculateExpandRange(groupPosition)));
            });
        } else {
            binding.tvSportName.setText(league.getLeagueName() + "(" + league.getMatchCount() + ")");
            binding.rlHeader.setVisibility(View.GONE);
            binding.tvSportName.setVisibility(View.VISIBLE);
        }
        binding.vSpace.setVisibility(View.VISIBLE);
    }

    private int getHeaderIcon(String leagueName) {
        Resources res = mContext.getResources();
        if (leagueName.equals(res.getString(R.string.bt_game_going_on))) return R.mipmap.bt_icon_going_on;
        if (leagueName.equals(res.getString(R.string.bt_game_waiting))) return R.mipmap.bt_icon_waiting;
        if (leagueName.equals(res.getString(R.string.bt_all_league))) return R.mipmap.bt_icon_all_league;
        return R.mipmap.bt_icon_all_league;
    }

    private String calculateExpandRange(int groupPosition) {
        int size = getGroupCount();
        int start = (liveHeaderPosition == groupPosition) ? groupPosition + 2 :
                (noLiveHeaderPosition > 0 ? noLiveHeaderPosition + 2 : 0);
        int end = (liveHeaderPosition == groupPosition && noLiveHeaderPosition > 0) ? noLiveHeaderPosition : size;
        return start + "/" + end;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Match match = (Match) getChild(groupPosition, childPosition);
        if (match == null) {
            return convertView != null ? convertView :
                    LayoutInflater.from(mContext).inflate(R.layout.bt_fb_match_list, parent, false);
        }

        ChildHolder holder = null;
        if (convertView == null) {
            BtFbMatchListBinding binding = BtFbMatchListBinding.inflate(
                    LayoutInflater.from(mContext), parent, false);
            holder = new ChildHolder(binding);
            convertView = binding.getRoot();
            convertView.setTag(holder);
        } else {
            if(convertView.getTag() instanceof ChildHolder){
                holder = (ChildHolder) convertView.getTag();
            }
        }

        if(holder != null && holder.binding != null){
            configureMatchView(holder.binding, match, parent, isLastChild);
        }

        return convertView;
    }

    private void configureMatchView(BtFbMatchListBinding binding, Match match, ViewGroup parent, boolean isLastChild) {
        binding.tvTeamNameMain.setText(match.getTeamMain());
        binding.tvTeamNameVisitor.setText(match.getTeamVistor());
        configureTeamSideIndicators(binding, match);
        configureScore(binding, match);
        binding.tvPlaytypeCount.setText(match.getPlayTypeCount() + "+>");
        configureMatchTime(binding, match);
        configureIcons(binding, match);
        configurePlayGroups(binding, match, parent);
        setClickListeners(binding, match);
        binding.vSpace.setVisibility(isLastChild ? View.VISIBLE : View.GONE);
        binding.cslRoot.setBackgroundResource(
                isLastChild ? R.drawable.bt_bg_match_item_bottom : R.drawable.bt_bg_match_item);
    }

    private void configureTeamSideIndicators(BtFbMatchListBinding binding, Match match) {
        Drawable serverDrawable = mContext.getResources().getDrawable(R.drawable.bt_bg_server);
        boolean needCheck = match.needCheckHomeSide() && match.isGoingon();
        if (needCheck) {
            boolean isHome = match.isHomeSide();
            binding.tvTeamNameMain.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    isHome ? serverDrawable : null, null);
            binding.tvTeamNameVisitor.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    isHome ? null : serverDrawable, null);
        } else {
            binding.tvTeamNameMain.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            binding.tvTeamNameVisitor.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }

    private void configureScore(BtFbMatchListBinding binding, Match match) {
        if (!match.isGoingon()) {
            binding.tvScoreMain.setText("");
            binding.tvScoreVisitor.setText("");
            return;
        }
        List<Integer> scoreList = match.getScore(Constants.getScoreType());
        if (scoreList != null && scoreList.size() > 1) {
            binding.tvScoreMain.setText(String.valueOf(scoreList.get(0)));
            binding.tvScoreVisitor.setText(String.valueOf(scoreList.get(1)));
        }
    }

    private void configureMatchTime(BtFbMatchListBinding binding, Match match) {
        if (!match.isGoingon()) {
            binding.tvMatchTime.setText(TimeUtils.longFormatString(match.getMatchTime(), TimeUtils.FORMAT_MM_DD_HH_MM));
            return;
        }
        String stage = match.getStage();
        if (stage == null) return;

        boolean isFootballOrBasketball = TextUtils.equals(Constants.getFbSportId(), match.getSportId()) ||
                TextUtils.equals(Constants.getBsbSportId(), match.getSportId());
        if (isFootballOrBasketball) {
            if (stage.contains("休息") || stage.contains("结束")) {
                binding.tvMatchTime.setText(stage);
            } else if (!isUpdateFootBallOrBasketBallState) {
                binding.tvMatchTime.setText(stage + " " + match.getTime());
            }
        } else {
            binding.tvMatchTime.setText(stage);
        }
    }

    private void configureIcons(BtFbMatchListBinding binding, Match match) {
        binding.ivCourt.setSelected(match.hasAs());
        binding.ivLive.setSelected(match.hasVideo());
        boolean hasCornor = match.hasCornor();
        binding.ivCornor.setVisibility(hasCornor ? View.VISIBLE : View.GONE);
        binding.ivCornor.setSelected(hasCornor);
        boolean isNeutrality = match.isNeutrality();
        binding.ivNeutrality.setVisibility(isNeutrality ? View.VISIBLE : View.GONE);
        binding.ivNeutrality.setSelected(isNeutrality);
    }

    private void configurePlayGroups(BtFbMatchListBinding binding, Match match, ViewGroup parent) {
        LinearLayout llTypeGroup = (LinearLayout) binding.hsvPlayTypeGroup.getChildAt(0);
        LinearLayout firstPagePlayType = (LinearLayout) llTypeGroup.getChildAt(0);
        PlayGroup playGroup = createPlayGroup(match);
        List<PlayGroup> playGroupList = playGroup.getPlayGroupList(match.getSportId());

        if (playGroupList.isEmpty()) return;

        configurePlayTypePage(firstPagePlayType, match, parent, playGroupList.get(0).getOriginalPlayTypeList());
        binding.llPointer.removeAllViews();

        LinearLayout secondPagePlayType = (LinearLayout) llTypeGroup.getChildAt(1);
        if (playGroupList.size() > 1) {
            secondPagePlayType.setVisibility(View.VISIBLE);
            binding.hsvPlayTypeGroup.setChildCount(2);
            configurePlayTypePage(secondPagePlayType, match, parent, playGroupList.get(1).getOriginalPlayTypeList());
            binding.llPointer.setVisibility(View.VISIBLE);
            binding.llScoreData.setVisibility(View.GONE);
            initPointer(binding);
        } else {
            secondPagePlayType.setVisibility(View.GONE);
            binding.llPointer.setVisibility(View.GONE);
            binding.llScoreData.removeAllViews();
            if (match.isGoingon()) {
                binding.llScoreData.setVisibility(View.VISIBLE);
                BaseDetailDataView scoreDataView = BaseDetailDataView.getInstance(mContext, match, true);
                if (scoreDataView instanceof SnkDataView || scoreDataView instanceof com.xtree.bet.weight.fb.SnkDataView) {
                    scoreDataView.setSnkMatch(match, true);
                    scoreDataView.addMatchListAdditional(match.getFormat() + " 总分");
                }
                if (scoreDataView != null) binding.llScoreData.addView(scoreDataView);
            }
        }
    }

    private PlayGroup createPlayGroup(Match match) {
        return (TextUtils.equals(platform, PLATFORM_PM) || TextUtils.equals(platform, PLATFORM_PMXC)) ?
                new PlayGroupPm(match.getPlayTypeList()) : new PlayGroupFb(match.getPlayTypeList());
    }

    private void configurePlayTypePage(LinearLayout playTypeLayout, Match match, ViewGroup parent, List<PlayType> playTypes) {
        for (int i = 0, count = playTypeLayout.getChildCount(); i < count; i++) {
            setPlayTypeGroup(match, parent, (LinearLayout) playTypeLayout.getChildAt(i), playTypes.get(i));
        }
    }

    private void setClickListeners(BtFbMatchListBinding binding, Match match) {
        View.OnClickListener detailClick = v -> BtDetailActivity.start(mContext, match);
        binding.llRoot.setOnClickListener(detailClick);
        binding.rlPlayCount.setOnClickListener(detailClick);
    }

    private void initPointer(BtFbMatchListBinding binding) {
        if (binding.llPointer.getChildCount() == 0) {
            for (int i = 0; i < 2; i++) {
                ImageView ivPointer = new ImageView(mContext);
                ivPointer.setBackgroundResource(R.drawable.bt_bg_play_type_group_pointer_selected);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ConvertUtils.dp2px(i == 0 ? 12 : 7), ConvertUtils.dp2px(2));
                params.rightMargin = ConvertUtils.dp2px(2);
                ivPointer.setLayoutParams(params);
                binding.llPointer.addView(ivPointer);
            }
        }
        updatePointer(binding, binding.hsvPlayTypeGroup.getCurrentPage());
    }

    private void updatePointer(BtFbMatchListBinding binding, int currentPage) {
        for (int i = 0, count = binding.llPointer.getChildCount(); i < count; i++) {
            View pointer = binding.llPointer.getChildAt(i);
            pointer.setSelected(i == currentPage);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) pointer.getLayoutParams();
            params.width = ConvertUtils.dp2px(i == currentPage ? 12 : 7);
            pointer.setLayoutParams(params);
        }
        binding.hsvPlayTypeGroup.setOnScrollListener(mOnScrollListener);
    }

    private void setPlayTypeGroup(Match match, ViewGroup parent, LinearLayout rootPlayType, PlayType playType) {
        rootPlayType.getLayoutParams().width = parent.getWidth() / 6; // 优化计算
        for (int j = 0, count = rootPlayType.getChildCount(); j < count; j++) {
            View view = rootPlayType.getChildAt(j);
            if (view instanceof TextView) {
                ((TextView) view).setText(playType.getPlayTypeName());
            } else {
                configureOptionView((LinearLayout) view, match, playType, j - 1);
            }
        }
    }

    private void configureOptionView(LinearLayout optionView, Match match, PlayType playType, int optionIndex) {
        List<Option> options = playType.getOptionList(match.getSportId());
        optionView.setVisibility(optionIndex >= options.size() ? View.GONE : View.VISIBLE);
        if (optionIndex >= options.size()) return;

        Option option = optionIndex < 0 ? null : options.get(optionIndex);
        TextView unavailableTextView = (TextView) optionView.getChildAt(0);
        TextView nameTextView = (TextView) optionView.getChildAt(1);
        DiscolourTextView oddTextView = (DiscolourTextView) optionView.getChildAt(2);

        if (option == null) {
            unavailableTextView.setVisibility(View.VISIBLE);
            oddTextView.setVisibility(View.GONE);
            nameTextView.setVisibility(View.GONE);
            unavailableTextView.setText("-");
            unavailableTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            optionView.setOnClickListener(null);
        } else {
            OptionList optionList = playType.getOptionLists().get(0);
            if (!optionList.isOpen()) {
                unavailableTextView.setVisibility(View.VISIBLE);
                oddTextView.setVisibility(View.GONE);
                nameTextView.setVisibility(View.GONE);
                unavailableTextView.setText("");
                unavailableTextView.setCompoundDrawablesWithIntrinsicBounds(
                        mContext.getResources().getDrawable(R.mipmap.bt_icon_option_locked), null, null, null);
                optionView.setOnClickListener(null);
            } else {
                unavailableTextView.setVisibility(View.GONE);
                oddTextView.setVisibility(View.VISIBLE);
                configureOptionText(nameTextView, oddTextView, option);
                BetConfirmOption betConfirmOption = BetConfirmOptionUtil.getInstance(match, playType, optionList, option);
                optionView.setTag(betConfirmOption);
                updateOptionSelection(optionView, nameTextView, oddTextView, betConfirmOption);
                setOptionClickListener(optionView, optionList, option);
            }
        }
    }

    private void configureOptionText(TextView nameTextView, DiscolourTextView oddTextView, Option option) {
        if (TextUtils.isEmpty(option.getSortName())) {
            nameTextView.setVisibility(View.GONE);
        } else {
            nameTextView.setVisibility(View.VISIBLE);
            nameTextView.setText(option.getSortName());
        }
        oddTextView.setOptionOdd(option);
    }

    private void updateOptionSelection(LinearLayout optionView, TextView nameTextView,
                                       DiscolourTextView oddTextView, BetConfirmOption betConfirmOption) {
        boolean isCg = BtCarManager.isCg();
        boolean has = isCg && BtCarManager.has(betConfirmOption);
        optionView.setSelected(has);
        oddTextView.setSelected(has);
        nameTextView.setSelected(has);
    }

    private void setOptionClickListener(LinearLayout llOption, OptionList optionList, Option option) {
        llOption.setOnClickListener(v -> {
            if (!optionList.isOpen() || option == null) return;
            BetConfirmOption betConfirmOption = (BetConfirmOption) v.getTag();
            if (BtCarManager.isCg()) {
                if (!optionList.isAllowCrossover()) {
                    ToastUtils.showShort(mContext.getResources().getText(R.string.bt_bt_is_not_allow_crossover));
                    return;
                }
                if (!BtCarManager.has(betConfirmOption)) {
                    BtCarManager.addBtCar(betConfirmOption);
                } else {
                    BtCarManager.removeBtCar(betConfirmOption);
                }
                RxBus.getDefault().post(new BetContract(BetContract.ACTION_OPEN_CG));
            } else if (!ClickUtil.isFastClick()) {
                BtCarDialogFragment fragment = new BtCarDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable(BtCarDialogFragment.KEY_BT_OPTION, betConfirmOption);
                fragment.setArguments(bundle);
                if (mContext instanceof BaseActivity) {
                    fragment.show(((BaseActivity) mContext).getSupportFragmentManager(), "btCarDialogFragment");
                }
            }
        });
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    //足球和篮球比赛时间按秒计时代码
    public void updateVisibleItems(ExpandableListView listView) {
            isUpdateFootBallOrBasketBallState = true;

            int firstVisible = listView.getFirstVisiblePosition();
            int lastVisible = listView.getLastVisiblePosition();

            for (int i = firstVisible; i <= lastVisible; i++) {
                long packedPosition = listView.getExpandableListPosition(i);
                int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
                int childPosition = ExpandableListView.getPackedPositionChild(packedPosition);

                // 只处理 ChildItem，跳过 GroupHeader
                if (childPosition == ExpandableListView.INVALID_POSITION) continue;
                if (groupPosition >= getGroupCount()) continue;

                // 获取实际的 ChildView
                View childView = listView.getChildAt(i - firstVisible);
                if (childView == null) continue;

                TextView tvMatchTime = childView.findViewById(R.id.tv_match_time);
                if (tvMatchTime != null) {
                    Match match = (Match) getChild(groupPosition, childPosition);
                    if (match != null) {
                        updateMatchTimeDisplay(tvMatchTime, match);
                    }
                }
            }

    }

    //比赛时间更新只针对足球和篮球
    private void updateMatchTimeDisplay(TextView tvMatchTime, Match match) {
        String stage = match.getStage();
        String sportId = match.getSportId();
        if (!isFootBallOrBasketBall(sportId) || stage == null ||
                stage.contains("休息") || stage.contains("结束") ||
                stage.contains("未开赛") || stage.contains("未开始")) {
            return;
        }

        int normalTime = getTagIntValue(tvMatchTime, R.id.tag_normal_time);
        if (sportId.equals("1")) {
            updateFootballTime(tvMatchTime, match, normalTime, stage);
        } else if ((sportId.equals("2") || sportId.equals("3")) && match.getSportName().equals("篮球")) {//FB跟PM的篮球赛种代号不一致，加上赛种名称判断
            updateBasketballTime(tvMatchTime, match, normalTime, stage);
        }
    }

    //vs开头的足球比赛时间不准，已对比之前没加走秒前代码进行过对比，不是按照5秒刷新的
    private void updateFootballTime(TextView tvMatchTime, Match match, int normalTime, String stage) {
        int timeS = match.getTimeS();
        if (normalTime != timeS) { //已刷新接口,校正时间
            //CfLog.d("============ updateFootballTime 自动校正时间 三方的时间==========="+formatTime(timeS));
            tvMatchTime.setTag(R.id.tag_normal_time, timeS);
            tvMatchTime.setTag(R.id.tag_add_time, 0);
            tvMatchTime.setText(stage + " " + formatTime(timeS));
        } else { //未刷新接口，自增秒数
            int seconds = getTagIntValue(tvMatchTime, R.id.tag_add_time) + 1;
            //CfLog.d("============ updateFootballTime 当前自增时间 ==========="+seconds);
            tvMatchTime.setTag(R.id.tag_add_time, seconds);
            int currentTime = timeS + seconds;
            tvMatchTime.setText(stage + " " + formatTime(currentTime));
            tvMatchTime.setTag(R.id.tag_last_time, currentTime);
        }
    }

    private void updateBasketballTime(TextView tvMatchTime, Match match, int normalTime, String stage) {
        CfLog.d("================== updateBasketballTime getMess ====================="+match.getMess());
        if(match.getMess() == 0){ //比赛暂停
            return;
        }
        int timeS = match.getTimeS();
        if (normalTime != timeS) {
            tvMatchTime.setTag(R.id.tag_normal_time, timeS);
            tvMatchTime.setTag(R.id.tag_add_time, 0);
        } else {
            int seconds = getTagIntValue(tvMatchTime, R.id.tag_add_time) + 1;
            tvMatchTime.setTag(R.id.tag_add_time, seconds);
            int currentTime = Math.max(normalTime - seconds, 0);
            int lastTime = getTagIntValue(tvMatchTime, R.id.tag_last_time);
            tvMatchTime.setText(stage + " " + formatTime(lastTime > 0 && currentTime > lastTime ? lastTime : currentTime));
        }
    }

    private int getTagIntValue(TextView view, int key) {
        Object tag = view.getTag(key);
        return tag instanceof Integer ? (Integer) tag : 0;
    }

    private boolean isFootBallOrBasketBall(String sportId) {
        return TextUtils.equals(Constants.getFbSportId(), sportId) ||
                TextUtils.equals(Constants.getBsbSportId(), sportId);
    }

    public String formatTime(int totalSeconds) {
        return String.format("%02d:%02d", totalSeconds / 60, totalSeconds % 60);
    }

    private static class GroupHolder {
        final BtFbLeagueGroupBinding binding;

        GroupHolder(BtFbLeagueGroupBinding binding) {
            this.binding = binding;
        }
    }

    private static class ChildHolder {
        final BtFbMatchListBinding binding;

        ChildHolder(BtFbMatchListBinding binding) {
            this.binding = binding;
        }
    }

}