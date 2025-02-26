package com.xtree.bet.ui.adapter;

import static com.xtree.base.utils.BtDomainUtil.KEY_PLATFORM;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_PM;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_PMXC;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
        init();
        notifyDataSetChanged();
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
        //Log.e("test", "=====groupPosition======" + groupPosition);
        if (mDatas == null || mDatas.isEmpty() || mDatas.size() <= groupPosition) {
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.bt_fb_league_group, null);
            }
            return convertView;
        }
        League league = mDatas.get(groupPosition);

        GroupHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.bt_fb_league_group, null);
            convertView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            holder = new GroupHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }

        if (holder == null || holder.itemView == null) {
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.bt_fb_league_group, null);
            }
            return convertView;
        }

        BtFbLeagueGroupBinding binding = BtFbLeagueGroupBinding.bind(holder.itemView);
        if (!league.isHead()) {
            binding.llHeader.setVisibility(View.GONE);
            binding.rlLeague.setVisibility(View.VISIBLE);
            binding.tvLeagueName.setText(league.getLeagueName());
            Glide.with(mContext).load(league.getIcon())
                    //.apply(new RequestOptions().placeholder(placeholderRes))
                    .into(binding.ivIcon);
            binding.vSpace.setVisibility(isExpanded ? View.GONE : View.VISIBLE);
            binding.groupIndicator.setImageResource(isExpanded ? R.mipmap.bt_icon_expand : R.mipmap.bt_icon_unexpand);
            binding.rlLeague.setBackgroundResource(isExpanded ? R.drawable.bt_bg_league_top : R.drawable.bt_bg_league_top_collapse);
            league.setExpand(isExpanded);
        } else {
            binding.llHeader.setVisibility(View.VISIBLE);
            binding.rlLeague.setVisibility(View.GONE);

            if (league.getHeadType() == League.HEAD_TYPE_LIVE_OR_NOLIVE) {
                binding.rlHeader.setVisibility(View.VISIBLE);
                binding.tvSportName.setVisibility(View.GONE);
                binding.ivExpand.setSelected(league.isExpand());
                binding.tvHeaderName.setText(league.getLeagueName());
                if (league.getLeagueName().equals(mContext.getResources().getString(R.string.bt_game_going_on))) {
                    binding.ivHeader.setBackgroundResource(R.mipmap.bt_icon_going_on);
                } else if (league.getLeagueName().equals(mContext.getResources().getString(R.string.bt_game_waiting))) {
                    binding.ivHeader.setBackgroundResource(R.mipmap.bt_icon_waiting);
                } else if (league.getLeagueName().equals(mContext.getResources().getString(R.string.bt_all_league))) {
                    binding.ivHeader.setBackgroundResource(R.mipmap.bt_icon_all_league);
                }
                binding.rlHeader.setOnClickListener(view -> {
                    binding.ivExpand.setSelected(!league.isExpand());
                    int start;
                    int end;
                    if (liveHeaderPosition == groupPosition) { // 点击进行中
                        start = groupPosition + 2;
                        end = noLiveHeaderPosition > 0 ? noLiveHeaderPosition : mDatas.size();
                    } else if (noLiveHeaderPosition > 0) { // 点击未开赛
                        start = noLiveHeaderPosition + 2;
                        end = mDatas.size();
                    } else { // 点击未开赛
                        start = 0;
                        end = mDatas.size();
                    }
                    RxBus.getDefault().post(new BetContract(BetContract.ACTION_EXPAND, start + "/" + end + ""));
                });
            } else {
                binding.tvSportName.setText(league.getLeagueName() + "(" + league.getMatchCount() + ")");
                binding.rlHeader.setVisibility(View.GONE);
                binding.tvSportName.setVisibility(View.VISIBLE);
            }
            binding.vSpace.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        ChildHolder holder;
        Match match = (Match) getChild(groupPosition, childPosition);
        if (match == null) {
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.bt_fb_match_list, null);
            }
            return convertView;
        }

        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.bt_fb_match_list, null);
            holder = new ChildHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ChildHolder) convertView.getTag();
        }

        if (holder == null || holder.itemView == null) {
            holder = new ChildHolder(convertView);
            convertView.setTag(holder);
        }

        BtFbMatchListBinding binding = BtFbMatchListBinding.bind(holder.itemView);

        binding.tvTeamNameMain.setText(match.getTeamMain());
        binding.tvTeamNameVisitor.setText(match.getTeamVistor());
        if (match.needCheckHomeSide() && match.isGoingon()) {
            if (match.isHomeSide()) {
                binding.tvTeamNameMain.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.bt_bg_server), null);
                binding.tvTeamNameVisitor.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            } else {
                binding.tvTeamNameMain.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                binding.tvTeamNameVisitor.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.bt_bg_server), null);
            }
        } else {
            binding.tvTeamNameMain.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            binding.tvTeamNameVisitor.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }

        List<Integer> scoreList = match.getScore(Constants.getScoreType());
        if (!match.isGoingon()) {
            binding.tvScoreMain.setText("");
            binding.tvScoreVisitor.setText("");
        } else if (scoreList != null && scoreList.size() > 1) {
            binding.tvScoreMain.setText(String.valueOf(scoreList.get(0)));
            binding.tvScoreVisitor.setText(String.valueOf(scoreList.get(1)));
        }

        binding.tvPlaytypeCount.setText(match.getPlayTypeCount() + "+>");
        // 比赛未开始
        if (!match.isGoingon()) {
            binding.tvMatchTime.setText(TimeUtils.longFormatString(match.getMatchTime(), TimeUtils.FORMAT_MM_DD_HH_MM));
        } else {
            String mc = match.getStage();
            if (mc != null) {
                if (TextUtils.equals(Constants.getFbSportId(), match.getSportId()) || TextUtils.equals(Constants.getBsbSportId(), match.getSportId())) { // 足球和篮球
                    if (mc.contains("休息") || mc.contains("结束")) {
                        binding.tvMatchTime.setText(match.getStage());
                    } else {
                        //没有保存当前时间的视为当前不展示的item,正常跟随adapter刷新,展示的就跟随读秒刷新
//                        if(binding.tvMatchTime.getTag(R.id.tag_normal_time) == null && binding.tvMatchTime.getTag(R.id.tag_add_time) == null){
//                            binding.tvMatchTime.setText(mc + " " + match.getTime());
//                        }
                    }
                } else {
                    binding.tvMatchTime.setText(match.getStage());
                }
            }
        }

        binding.ivCourt.setSelected(match.hasAs());
        binding.ivLive.setSelected(match.hasVideo());
        binding.ivCornor.setVisibility(match.hasCornor() ? View.VISIBLE : View.GONE);
        binding.ivCornor.setSelected(match.hasCornor());
        binding.ivNeutrality.setVisibility(match.isNeutrality() ? View.VISIBLE : View.GONE);
        binding.ivNeutrality.setSelected(match.isNeutrality());

        LinearLayout llTypeGroup = (LinearLayout) binding.hsvPlayTypeGroup.getChildAt(0);

        LinearLayout firstPagePlayType = (LinearLayout) llTypeGroup.getChildAt(0);

        PlayGroup playGroup;

        if (!TextUtils.equals(platform, PLATFORM_PM) && !TextUtils.equals(platform, PLATFORM_PMXC)) {
            playGroup = new PlayGroupFb(match.getPlayTypeList());
        } else {
            playGroup = new PlayGroupPm(match.getPlayTypeList());
        }
        List<PlayGroup> playGroupList = playGroup.getPlayGroupList(match.getSportId());

        if (!playGroupList.isEmpty()) {

            for (int i = 0; i < firstPagePlayType.getChildCount(); i++) {
                setPlayTypeGroup(match, parent, (LinearLayout) firstPagePlayType.getChildAt(i), playGroupList.get(0).getOriginalPlayTypeList().get(i));
            }
            binding.llPointer.removeAllViews();
            LinearLayout sencondPagePlayType = (LinearLayout) llTypeGroup.getChildAt(1);
            if (playGroupList.size() > 1) {
                sencondPagePlayType.setVisibility(View.VISIBLE);
                binding.hsvPlayTypeGroup.setChildCount(2);
                List<PlayType> playTypeList = playGroupList.get(1).getOriginalPlayTypeList();
                for (int i = 0; i < sencondPagePlayType.getChildCount(); i++) {
                    setPlayTypeGroup(match, parent, (LinearLayout) sencondPagePlayType.getChildAt(i), playTypeList.get(i));
                }
                binding.llPointer.setVisibility(View.VISIBLE);
                binding.llScoreData.setVisibility(View.GONE);
                initPointer(binding);
            } else {
                sencondPagePlayType.setVisibility(View.GONE);
                binding.llPointer.setVisibility(View.GONE);
                binding.llScoreData.removeAllViews();
                if (match.isGoingon()) {
                    binding.llScoreData.setVisibility(View.VISIBLE);
                    BaseDetailDataView mScoreDataView = BaseDetailDataView.getInstance(mContext, match, true);
                    if (mScoreDataView != null) {
                        binding.llScoreData.addView(mScoreDataView);
                    }
                }
            }

            binding.llRoot.setOnClickListener(view -> {
                BtDetailActivity.start(mContext, match);
            });

            binding.rlPlayCount.setOnClickListener(view -> {
                BtDetailActivity.start(mContext, match);
            });

            binding.vSpace.setVisibility(childPosition == getRealChildrenCount(groupPosition) - 1 ? View.VISIBLE : View.GONE);
            binding.cslRoot.setBackgroundResource(childPosition == getRealChildrenCount(groupPosition) - 1 ? R.drawable.bt_bg_match_item_bottom : R.drawable.bt_bg_match_item);
        }
        return convertView;
    }

    /**
     * 初始化分页pointer
     *
     * @param binding
     */
    private void initPointer(BtFbMatchListBinding binding) {
        for (int i = 0; i < 2; i++) {
            ImageView ivPointer = new ImageView(mContext);
            ivPointer.setBackgroundResource(R.drawable.bt_bg_play_type_group_pointer_selected);
            int width = i == binding.hsvPlayTypeGroup.getCurrentPage() ? ConvertUtils.dp2px(12) : ConvertUtils.dp2px(7);
            int height = ConvertUtils.dp2px(2);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
            params.rightMargin = ConvertUtils.dp2px(2);
            ivPointer.setSelected(i == binding.hsvPlayTypeGroup.getCurrentPage());
            ivPointer.setLayoutParams(params);
            binding.llPointer.addView(ivPointer);
        }
        binding.hsvPlayTypeGroup.setOnPageSelectedListener(currentPage -> {
            for (int i = 0; i < binding.llPointer.getChildCount(); i++) {
                if (currentPage == i) {
                    binding.llPointer.getChildAt(i).setSelected(true);
                } else {
                    binding.llPointer.getChildAt(i).setSelected(false);
                }
                int width = currentPage == i ? ConvertUtils.dp2px(12) : ConvertUtils.dp2px(7);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) binding.llPointer.getChildAt(i).getLayoutParams();
                params.width = width;
                binding.llPointer.getChildAt(i).setLayoutParams(params);

            }
        });
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
        public GroupHolder(View view) {
            itemView = view;
        }

        View itemView;
    }

    private static class ChildHolder {
        View itemView;

        public ChildHolder(View view) {
            itemView = view;
        }
    }

    public void updateVisibleItems(ExpandableListView listView) {
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

        // 判断是否为足球或篮球
        boolean isFootballOrBasketball = TextUtils.equals(Constants.getFbSportId(), sportId) || TextUtils.equals(Constants.getBsbSportId(), sportId);

        //其它类型运动
        if (!isFootballOrBasketball) {
            return;
        }

        // 处理休息或结束状态
        if (stage.contains("休息") || stage.contains("结束") || stage.contains("未开赛") || stage.contains("未开始")) {
            CfLog.i("======= 休息 结束 未开赛 =====");
            return;
        }

        // 获取并处理时间数据
        int normalTime = getTagIntValue(tvMatchTime, R.id.tag_normal_time);
        CfLog.i("=========== 比赛时间 =============" + stage + " " + match.getTime());

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
            if (stage.contains("未开赛")) {
                return;
            }
            // 计时增加
            int seconds = getTagIntValue(tvMatchTime, R.id.tag_add_time);
            seconds++;
            tvMatchTime.setTag(R.id.tag_add_time, seconds);
            int lastTime = getTagIntValue(tvMatchTime, R.id.tag_last_time);
            int currentTime = match.getTimeS() + seconds;
            CfLog.i("=========== 比赛ID =============" + match.getId());
            CfLog.i("=========== 增加的秒数 =============" + seconds);
            CfLog.i("=========== 原始的秒数 =============" + match.getTimeS());
            CfLog.i("=========== 上次的时间 =============" + formatTime(lastTime));
            CfLog.i("=========== 原始时间 =============" + formatTime(match.getTimeS()));
            CfLog.i("=========== 添加时间 =============" + formatTime(currentTime));
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

            if (stage.contains("未开赛")) {
                return;
            }
            //篮球时间是递减的,保证不能显示负数
            if (lastTime > 0 && currentTime > lastTime) {
                tvMatchTime.setText(stage + " " + formatTime(lastTime));
                System.out.println("=========== 篮球按秒记时(使用上次时间) =============" + match.getId() + " : " + stage + " " + formatTime(lastTime));
            } else {
                tvMatchTime.setText(stage + " " + formatTime(currentTime));
                System.out.println("=========== 篮球按秒记时 =============" + match.getId() + " : " + stage + " " + formatTime(currentTime));
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

    public String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d : %02d", minutes, seconds);
    }

}
