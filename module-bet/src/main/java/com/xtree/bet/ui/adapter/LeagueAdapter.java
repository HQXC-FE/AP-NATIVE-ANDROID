package com.xtree.bet.ui.adapter;

import static com.xtree.base.utils.BtDomainUtil.KEY_PLATFORM;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_PM;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_PMXC;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import java.util.List;

import me.xtree.mvvmhabit.base.BaseActivity;
import me.xtree.mvvmhabit.bus.RxBus;
import me.xtree.mvvmhabit.utils.ConvertUtils;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

public class LeagueAdapter extends AnimatedExpandableListViewMax.AnimatedExpandableListAdapter {
    private List<League> mDatas;
    private Context mContext;
    private String platform = SPUtils.getInstance().getString(KEY_PLATFORM);
    private int liveHeaderPosition;
    private int noLiveHeaderPosition;
    private boolean isUpdateTime;
    private boolean isUpdateFootBallOrBasketBallState;

    private PageHorizontalScrollView.OnScrollListener mOnScrollListener;

    public void setOnScrollListener(PageHorizontalScrollView.OnScrollListener onScrollListener) {
        this.mOnScrollListener = onScrollListener;
    }

    public void setHeaderIsExpand(int position, boolean headerIsExpand) {
        if (!mDatas.isEmpty() && mDatas.size() > position) {
            mDatas.get(position).setExpand(headerIsExpand);
        }
    }

    public int getLiveHeaderPosition() {
        return liveHeaderPosition;
    }

    public int getNoLiveHeaderPosition() {
        return noLiveHeaderPosition;
    }

    public void setData(List<League> mLeagueList) {
        this.mDatas = mLeagueList;
        if (mLeagueList != null) {
            init();  // 仅在数据非空时调用 init
            notifyDataSetChanged();
        }
    }

    public LeagueAdapter(Context context, List<League> datas) {
        this.mDatas = datas;
        this.mContext = context;
        init();
    }

    public void init() {
        liveHeaderPosition = 0;
        noLiveHeaderPosition = 0;
        int index = 0;
        for (int i = 0; i < mDatas.size(); i++) {
            if (mDatas.get(i).isHead() && mDatas.get(i).getHeadType() == League.HEAD_TYPE_LIVE_OR_NOLIVE) {
                if (index == 0) {
                    liveHeaderPosition = i;
                    index++;
                } else if (index > 0) {
                    noLiveHeaderPosition = i;
                    break;
                }
            }
        }

    }

    /**
     * 是否点击进行中表头
     *
     * @param start
     * @return
     */
    public boolean isHandleGoingOnExpand(int start) {
        return noLiveHeaderPosition > 0 && start - 2 == liveHeaderPosition;
    }

    /**
     * 获取进行中联赛的在列表中的起止位置
     *
     * @return
     */
    public String expandRangeLive() {
        int start = liveHeaderPosition + 2;
        int end = noLiveHeaderPosition > 0 ? noLiveHeaderPosition : mDatas.size();
        return start + "/" + end;
    }

    /**
     * 获取未开赛联赛的在列表中的起止位置
     *
     * @return
     */
    public String expandRangeNoLive() {
        if (noLiveHeaderPosition > 0) {
            int start = noLiveHeaderPosition + 2;
            int end = mDatas.size();
            return start + "/" + end;
        } else {
            return 0 + "/" + mDatas.size();
        }
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        if (mDatas.isEmpty() || groupPosition >= mDatas.size() || mDatas.get(groupPosition).getMatchList().isEmpty()) {
            return 0;
        }
        return mDatas.get(groupPosition).getMatchList().size();
    }

    @Override
    public int getGroupCount() {
        if (mDatas == null) {
            return 0;
        }
        return mDatas.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        if (!mDatas.isEmpty() && mDatas.size() > groupPosition) {
            return mDatas.get(groupPosition);
        } else {
            return null;
        }
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        if (mDatas == null || mDatas.isEmpty() || mDatas.size() <= groupPosition) {
            return null;
        }
        if (mDatas.get(groupPosition).getMatchList() == null || mDatas.get(groupPosition).getMatchList().size() <= childPosition) {
            return null;
        }
        return mDatas.get(groupPosition).getMatchList().get(childPosition);
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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup viewGroup) {
        if (mDatas == null || groupPosition >= mDatas.size()) {
            return convertView != null ? convertView :
                    LayoutInflater.from(mContext).inflate(R.layout.bt_fb_league_group, viewGroup, false);
        }

        GroupHolder holder;
        if (convertView == null) {
            BtFbLeagueGroupBinding binding = BtFbLeagueGroupBinding.inflate(
                    LayoutInflater.from(mContext), viewGroup, false);
            holder = new GroupHolder(binding.getRoot());
            convertView = binding.getRoot();
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }

        League league = mDatas.get(groupPosition);
        BtFbLeagueGroupBinding binding = holder.binding;

        // 设置可见性
        boolean isHead = league.isHead();
        binding.llHeader.setVisibility(isHead ? View.VISIBLE : View.GONE);
        binding.rlLeague.setVisibility(isHead ? View.GONE : View.VISIBLE);

        if (!isHead) {
            // 非头部联赛视图
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
        } else {
            // 头部视图
            configureHeader(binding, league, groupPosition);
        }

        return convertView;
    }

    /**
     * 配置头部视图
     */
    private void configureHeader(BtFbLeagueGroupBinding binding, League league, int groupPosition) {
        if (league.getHeadType() == League.HEAD_TYPE_LIVE_OR_NOLIVE) {
            binding.rlHeader.setVisibility(View.VISIBLE);
            binding.tvSportName.setVisibility(View.GONE);
            binding.ivExpand.setSelected(league.isExpand());
            binding.tvHeaderName.setText(league.getLeagueName());

            // 设置头部图标
            int iconRes = getHeaderIcon(league.getLeagueName());
            binding.ivHeader.setBackgroundResource(iconRes);

            // 点击事件
            binding.rlHeader.setOnClickListener(v -> {
                boolean newExpandState = !league.isExpand();
                binding.ivExpand.setSelected(newExpandState);
                String range = calculateExpandRange(groupPosition);
                RxBus.getDefault().post(new BetContract(BetContract.ACTION_EXPAND, range));
            });
        } else {
            binding.tvSportName.setText(league.getLeagueName() + "(" + league.getMatchCount() + ")");
            binding.rlHeader.setVisibility(View.GONE);
            binding.tvSportName.setVisibility(View.VISIBLE);
        }
        binding.vSpace.setVisibility(View.VISIBLE);
    }

    /**
     * 获取头部图标资源
     */
    private int getHeaderIcon(String leagueName) {
        if (leagueName.equals(mContext.getResources().getString(R.string.bt_game_going_on))) {
            return R.mipmap.bt_icon_going_on;
        } else if (leagueName.equals(mContext.getResources().getString(R.string.bt_game_waiting))) {
            return R.mipmap.bt_icon_waiting;
        } else if (leagueName.equals(mContext.getResources().getString(R.string.bt_all_league))) {
            return R.mipmap.bt_icon_all_league;
        }
        return R.mipmap.bt_icon_all_league; // 默认图标
    }

    /**
     * 计算展开范围
     */
    private String calculateExpandRange(int groupPosition) {
        int start, end;
        int dataSize = mDatas.size();
        if (liveHeaderPosition == groupPosition) {
            start = groupPosition + 2;
            end = noLiveHeaderPosition > 0 ? noLiveHeaderPosition : dataSize;
        } else if (noLiveHeaderPosition > 0) {
            start = noLiveHeaderPosition + 2;
            end = dataSize;
        } else {
            start = 0;
            end = dataSize;
        }
        return start + "/" + end;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Match match = (Match) getChild(groupPosition, childPosition);
        if (match == null) {
            return convertView != null ? convertView :
                    LayoutInflater.from(mContext).inflate(R.layout.bt_fb_match_list, parent, false);
        }

        ChildHolder holder;
        if (convertView == null) {
            BtFbMatchListBinding binding = BtFbMatchListBinding.inflate(
                    LayoutInflater.from(mContext), parent, false);
            holder = new ChildHolder(binding.getRoot());
            convertView = binding.getRoot();
            convertView.setTag(holder);
        } else {
            holder = (ChildHolder) convertView.getTag();
        }

        BtFbMatchListBinding binding = holder.binding;
        configureMatchView(binding, match, parent, groupPosition, childPosition,isLastChild);

        return convertView;
    }

    /**
     * 配置比赛视图
     */
    /**
     * 配置比赛视图
     */
    private void configureMatchView(BtFbMatchListBinding binding, Match match, ViewGroup parent,
                                    int groupPosition, int childPosition, boolean isLastChild) {
        // 队伍名称
        binding.tvTeamNameMain.setText(match.getTeamMain());
        binding.tvTeamNameVisitor.setText(match.getTeamVistor());
        configureTeamSideIndicators(binding, match);

        // 分数
        configureScore(binding, match);

        // 玩法计数
        binding.tvPlaytypeCount.setText(match.getPlayTypeCount() + "+>");

        // 比赛时间
        configureMatchTime(binding, match);

        // 图标状态
        configureIcons(binding, match);

        // 玩法组
        configurePlayGroups(binding, match, parent);

        // 点击事件
        setClickListeners(binding, match);

        // 背景和间距
        binding.vSpace.setVisibility(isLastChild ? View.VISIBLE : View.GONE);
        binding.cslRoot.setBackgroundResource(
                isLastChild ? R.drawable.bt_bg_match_item_bottom : R.drawable.bt_bg_match_item);
    }

    /**
     * 配置队伍主客场指示器
     */
    private void configureTeamSideIndicators(BtFbMatchListBinding binding, Match match) {
        Drawable serverDrawable = mContext.getResources().getDrawable(R.drawable.bt_bg_server);
        if (match.needCheckHomeSide() && match.isGoingon()) {
            if (match.isHomeSide()) {
                binding.tvTeamNameMain.setCompoundDrawablesWithIntrinsicBounds(null, null, serverDrawable, null);
                binding.tvTeamNameVisitor.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            } else {
                binding.tvTeamNameMain.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                binding.tvTeamNameVisitor.setCompoundDrawablesWithIntrinsicBounds(null, null, serverDrawable, null);
            }
        } else {
            binding.tvTeamNameMain.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            binding.tvTeamNameVisitor.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }

    /**
     * 配置分数显示
     */
    private void configureScore(BtFbMatchListBinding binding, Match match) {
        if (!match.isGoingon()) {
            binding.tvScoreMain.setText("");
            binding.tvScoreVisitor.setText("");
        } else {
            List<Integer> scoreList = match.getScore(Constants.getScoreType());
            if (scoreList != null && scoreList.size() > 1) {
                binding.tvScoreMain.setText(String.valueOf(scoreList.get(0)));
                binding.tvScoreVisitor.setText(String.valueOf(scoreList.get(1)));
            }
        }
    }

    /**
     * 配置比赛时间
     */
    private void configureMatchTime(BtFbMatchListBinding binding, Match match) {
        if (!match.isGoingon()) {
            binding.tvMatchTime.setText(TimeUtils.longFormatString(match.getMatchTime(), TimeUtils.FORMAT_MM_DD_HH_MM));
        } else {
            String stage = match.getStage();
            if (stage != null) {
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
        }
    }

    /**
     * 配置图标状态
     */
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

    /**
     * 配置玩法组
     */
    private void configurePlayGroups(BtFbMatchListBinding binding, Match match, ViewGroup parent) {
        LinearLayout llTypeGroup = (LinearLayout) binding.hsvPlayTypeGroup.getChildAt(0);
        LinearLayout firstPagePlayType = (LinearLayout) llTypeGroup.getChildAt(0);
        PlayGroup playGroup = TextUtils.equals(platform, PLATFORM_PM) ||
                TextUtils.equals(platform, PLATFORM_PMXC) ?
                new PlayGroupPm(match.getPlayTypeList()) :
                new PlayGroupFb(match.getPlayTypeList());
        List<PlayGroup> playGroupList = playGroup.getPlayGroupList(match.getSportId());

        if (!playGroupList.isEmpty()) {
            // 第一页玩法
            List<PlayType> firstPlayTypes = playGroupList.get(0).getOriginalPlayTypeList();
            for (int i = 0; i < firstPagePlayType.getChildCount(); i++) {
                setPlayTypeGroup(match, parent, (LinearLayout) firstPagePlayType.getChildAt(i), firstPlayTypes.get(i));
            }

            // 处理第二页和指针
            binding.llPointer.removeAllViews();
            LinearLayout secondPagePlayType = (LinearLayout) llTypeGroup.getChildAt(1);
            if (playGroupList.size() > 1) {
                secondPagePlayType.setVisibility(View.VISIBLE);
                binding.hsvPlayTypeGroup.setChildCount(2);
                List<PlayType> secondPlayTypes = playGroupList.get(1).getOriginalPlayTypeList();
                for (int i = 0; i < secondPagePlayType.getChildCount(); i++) {
                    setPlayTypeGroup(match, parent, (LinearLayout) secondPagePlayType.getChildAt(i), secondPlayTypes.get(i));
                }
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
                    if (scoreDataView != null) {
                        binding.llScoreData.addView(scoreDataView);
                    }
                }
            }
        }
    }

    /**
     * 设置点击事件
     */
    private void setClickListeners(BtFbMatchListBinding binding, Match match) {
        View.OnClickListener detailClick = v -> BtDetailActivity.start(mContext, match);
        binding.llRoot.setOnClickListener(detailClick);
        binding.rlPlayCount.setOnClickListener(detailClick);
    }

    /**
     * 优化的 ChildHolder
     */
    private static class ChildHolder {
        final BtFbMatchListBinding binding;

        ChildHolder(View view) {
            binding = BtFbMatchListBinding.bind(view);
        }
    }

    /**
     * 初始化分页pointer
     *
     * @param binding
     */
    private void initPointer(BtFbMatchListBinding binding) {
        if (binding.llPointer.getChildCount() == 0) { // 只在第一次初始化
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
        for (int i = 0; i < binding.llPointer.getChildCount(); i++) {
            View pointer = binding.llPointer.getChildAt(i);
            pointer.setSelected(i == currentPage);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) pointer.getLayoutParams();
            params.width = ConvertUtils.dp2px(i == currentPage ? 12 : 7);
            pointer.setLayoutParams(params);
        }
        binding.hsvPlayTypeGroup.setOnScrollListener(mOnScrollListener);
    }

    private void setPlayTypeGroup(Match match, ViewGroup parent, LinearLayout rootPlayType, PlayType playType) {
        ViewGroup.LayoutParams params = rootPlayType.getLayoutParams();
        params.width = parent.getWidth() / 2 / 3;
        rootPlayType.setLayoutParams(params);

        for (int j = 0; j < rootPlayType.getChildCount(); j++) {
            View view = rootPlayType.getChildAt(j);
            if (view instanceof TextView) {
                ((TextView) view).setText(playType.getPlayTypeName());
            } else {

                LinearLayout optionView = (LinearLayout) view;
                List<Option> options = playType.getOptionList(match.getSportId());
                if (j - 1 == playType.getOptionList(match.getSportId()).size()) {
                    optionView.setVisibility(View.GONE);
                } else {
                    optionView.setVisibility(View.VISIBLE);
                    Option option;
                    if (j - 1 >= options.size()) {//处理数组越界
                        option = null;
                    } else {
                        option = options.get(j - 1);
                    }
                    TextView uavailableTextView = (TextView) optionView.getChildAt(0);
                    TextView nameTextView = (TextView) optionView.getChildAt(1);
                    DiscolourTextView oddTextView = (DiscolourTextView) optionView.getChildAt(2);

                    if (option == null) {
                        uavailableTextView.setVisibility(View.VISIBLE);
                        oddTextView.setVisibility(View.GONE);
                        nameTextView.setVisibility(View.GONE);
                        uavailableTextView.setText("-");
                        uavailableTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                        optionView.setOnClickListener(view1 -> {
                        });
                    } else {
                        OptionList optionList = playType.getOptionLists().get(0);
                        if (!optionList.isOpen()) {
                            uavailableTextView.setVisibility(View.VISIBLE);
                            oddTextView.setVisibility(View.GONE);
                            nameTextView.setVisibility(View.GONE);
                            uavailableTextView.setText("");
                            uavailableTextView.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.mipmap.bt_icon_option_locked), null, null, null);
                            optionView.setOnClickListener(view1 -> {
                            });
                        } else {
                            uavailableTextView.setVisibility(View.GONE);
                            oddTextView.setVisibility(View.VISIBLE);
                            if (TextUtils.isEmpty(option.getSortName())) {
                                nameTextView.setVisibility(View.GONE);
                            } else {
                                nameTextView.setVisibility(View.VISIBLE);
                                nameTextView.setText(option.getSortName());
                            }
                            oddTextView.setOptionOdd(option);
                            BetConfirmOption betConfirmOption = BetConfirmOptionUtil.getInstance(match, playType, optionList, option);
                            optionView.setTag(betConfirmOption);
                            if (BtCarManager.isCg()) {
                                boolean has = BtCarManager.has(betConfirmOption);
                                optionView.setSelected(has);
                                oddTextView.setSelected(has);
                                nameTextView.setSelected(has);
                            } else {
                                if (optionView.isSelected()) {
                                    optionView.setSelected(false);
                                    oddTextView.setSelected(false);
                                    nameTextView.setSelected(false);
                                }
                            }
                            setOptionClickListener(optionView, optionList, option);
                        }
                    }
                }
            }
        }
    }

    private void setOptionClickListener(LinearLayout llOption, OptionList optionList, Option option) {
        llOption.setOnClickListener(view1 -> {
            if (!optionList.isOpen() || option == null) {
                return;
            }
            BetConfirmOption betConfirmOption = (BetConfirmOption) view1.getTag();
            if (BtCarManager.isCg()/* && !BtCarManager.isEmpty()*/) { // 如果是串关，往购物车增加
                if (!optionList.isAllowCrossover()/* && !BtCarManager.has(betConfirmOption)*/) {
                    ToastUtils.showShort(mContext.getResources().getText(R.string.bt_bt_is_not_allow_crossover));
                    return;
                }
                if (!BtCarManager.has(betConfirmOption)) {
                    BtCarManager.addBtCar(betConfirmOption);
                } else {
                    BtCarManager.removeBtCar(betConfirmOption);
                }
                //option.setSelected(BtCarManager.has(betConfirmOption));
                RxBus.getDefault().post(new BetContract(BetContract.ACTION_OPEN_CG));
            } else {
                if (ClickUtil.isFastClick()) {
                    return;
                }
                BtCarDialogFragment btCarDialogFragment = new BtCarDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable(BtCarDialogFragment.KEY_BT_OPTION, (BetConfirmOption) view1.getTag());
                btCarDialogFragment.setArguments(bundle);
                if (mContext instanceof BaseActivity) {
                    btCarDialogFragment.show(((BaseActivity) mContext).getSupportFragmentManager(), "btCarDialogFragment");
                }
            }
        });
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /*public void updateHeader(View view, int position){
        getGroup(position)
    }*/

    private static class GroupHolder {
        final BtFbLeagueGroupBinding binding;

        GroupHolder(View view) {
            binding = BtFbLeagueGroupBinding.bind(view);
        }
    }

//    private static class ChildHolder {
//        View itemView;
//
//        public ChildHolder(View view) {
//            itemView = view;
//        }
//    }

    public void updateVisibleItems(ExpandableListView listView) {
        isUpdateFootBallOrBasketBallState = true;
        listView.post(() -> {
            // 获取可见范围的起始和结束位置
            int first = listView.getFirstVisiblePosition();
            int last = listView.getLastVisiblePosition();

            // 遍历所有可见项
            for (int i = first; i <= last; i++) {
                // 获取展开列表位置信息
                long packedPosition = listView.getExpandableListPosition(i);
                int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
                int childPosition = ExpandableListView.getPackedPositionChild(packedPosition);

                // 确保组位置有效
                if (groupPosition < getGroupCount()) {
                    View view = listView.getChildAt(i - first);
                    if (view != null) {
                        TextView tvMatchTime = view.findViewById(R.id.tv_match_time);
                        if (tvMatchTime != null) {
                            // 获取当前比赛数据
                            Match match = (Match) getChild(groupPosition, childPosition);
                            if (match != null) {
                                updateMatchTimeDisplay(tvMatchTime, match);
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * 更新比赛时间显示
     *
     * @param tvMatchTime 时间显示的TextView
     * @param match       比赛数据对象
     */
    private void updateMatchTimeDisplay(TextView tvMatchTime, Match match) {
        String stage = match.getStage();
        String sportId = match.getSportId();

        //其它类型运动
        if (!isFootBallOrBasketBall(match.getSportId())) {
            return;
        }

        // 处理休息或结束状态
        if (stage.contains("休息") || stage.contains("结束") || stage.contains("未开赛") || stage.contains("未开始")) {
            return;
        }

        // 获取并处理时间数据
        int normalTime = getTagIntValue(tvMatchTime, R.id.tag_normal_time);
        //CfLog.i("=========== 比赛时间 =============" + stage + " " + match.getTime());

        if (sportId.equals("1")) {
            // 足球时间处理
            updateFootballTime(tvMatchTime, match, normalTime, stage);
        } else if ((sportId.equals("2") || sportId.equals("3")) && match.getSportName().equals("篮球")) {
            // 篮球时间处理
            updateBasketballTime(tvMatchTime, match, normalTime, stage);
        }
    }

    /**
     * 更新足球时间
     */
    private void updateFootballTime(TextView tvMatchTime, Match match, int normalTime, String stage) {
        if (normalTime != match.getTimeS()) {
            // 接口时间发生变更,重置增加时间
            tvMatchTime.setTag(R.id.tag_normal_time, match.getTimeS());
            tvMatchTime.setTag(R.id.tag_add_time, 0);
            tvMatchTime.setText(stage + " " + formatTime(match.getTimeS()));
        } else {
            // 计时增加
            int seconds = getTagIntValue(tvMatchTime, R.id.tag_add_time);
            seconds++;
            tvMatchTime.setTag(R.id.tag_add_time, seconds);
            int lastTime = getTagIntValue(tvMatchTime, R.id.tag_last_time);
            int currentTime = match.getTimeS() + seconds;
            //CfLog.i("=========== 比赛ID =============" + match.getId());
            //CfLog.i("=========== 增加的秒数 =============" + seconds);
            //CfLog.i("=========== 原始的秒数 =============" + match.getTimeS());
            //CfLog.i("=========== 上次的时间 =============" + formatTime(lastTime));
            //CfLog.i("=========== 原始时间 =============" + formatTime(match.getTimeS()));
            //CfLog.i("=========== 添加时间 =============" + formatTime(currentTime));
            tvMatchTime.setText(stage + " " + formatTime(currentTime));
            tvMatchTime.setTag(R.id.tag_last_time, currentTime);
        }
    }

    /**
     * 更新篮球时间
     */
    private void updateBasketballTime(TextView tvMatchTime, Match match, int normalTime, String stage) {
        if (normalTime != match.getTimeS()) {
            // 接口时间变更
            tvMatchTime.setTag(R.id.tag_normal_time, match.getTimeS());
            tvMatchTime.setTag(R.id.tag_add_time, 0);
        } else {
            // 计时减少
            int seconds = getTagIntValue(tvMatchTime, R.id.tag_add_time);
            seconds++;
            tvMatchTime.setTag(R.id.tag_add_time, seconds);

            int currentTime = normalTime > seconds ? normalTime - seconds : normalTime;
            int lastTime = getTagIntValue(tvMatchTime, R.id.tag_last_time);
            //篮球时间是递减的,保证不能显示负数
            if (lastTime > 0 && currentTime > lastTime) {
                tvMatchTime.setText(stage + " " + formatTime(lastTime));
                //CfLog.i("=========== 篮球按秒记时(使用上次时间) =============" + match.getId() + " : " + stage + " " + formatTime(lastTime));
            } else {
                tvMatchTime.setText(stage + " " + formatTime(currentTime));
               // CfLog.i("=========== 篮球按秒记时 =============" + match.getId() + " : " + stage + " " + formatTime(currentTime));
            }
        }
    }

    /**
     * 获取Tag中的整数值
     */
    private int getTagIntValue(TextView view, int key) {
        Object tag = view.getTag(key);
        return tag instanceof Integer ? (int) tag : 0;
    }

    private boolean isFootBallOrBasketBall(String sportId){
        // 判断是否为足球或篮球
        boolean isFootballOrBasketball = TextUtils.equals(Constants.getFbSportId(), sportId) || TextUtils.equals(Constants.getBsbSportId(), sportId);
        return isFootballOrBasketball;
    }

    public String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d : %02d", minutes, seconds);
    }
}
