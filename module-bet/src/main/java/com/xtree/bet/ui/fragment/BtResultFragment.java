package com.xtree.bet.ui.fragment;

import static com.xtree.base.utils.BtDomainUtil.KEY_PLATFORM;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_FB;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_FBXC;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_PM;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_PMXC;

import android.animation.ObjectAnimator;
import android.app.Application;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.tabs.TabLayout;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.TimeUtils;
import com.xtree.bet.BR;
import com.xtree.bet.R;
import com.xtree.bet.bean.ui.League;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.constant.SportTypeItem;
import com.xtree.bet.databinding.FragmentResultBinding;
import com.xtree.bet.ui.adapter.LeagueResultAdapter;
import com.xtree.bet.ui.adapter.TabSportResultAdapter;
import com.xtree.bet.ui.viewmodel.TemplateMainViewModel;
import com.xtree.bet.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.bet.ui.viewmodel.factory.PMAppViewModelFactory;
import com.xtree.bet.ui.viewmodel.fb.FBMainViewModel;
import com.xtree.bet.ui.viewmodel.pm.PMMainViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.Utils;

/**
 * 赛果
 */
@Route(path = RouterFragmentPath.Bet.PAGER_BET_RESULT)
public class BtResultFragment extends BaseFragment<FragmentResultBinding, TemplateMainViewModel> {
    private TabSportResultAdapter tabSportAdapter;
    private int playMethodPos;
    private String mPlatform = SPUtils.getInstance().getString(KEY_PLATFORM);
    private LeagueResultAdapter resultAdapter;
    public List<Date> dateResultList = new ArrayList<>();
    private int searchDatePos;
    private int sportTypePos = -1;
    private Map<String, List<SportTypeItem>> mResult;
    private List<League> allLeagueList = new ArrayList<League>();

    public static BtResultFragment getInstance() {
        BtResultFragment btResultDialogFragment = new BtResultFragment();
        return btResultDialogFragment;
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_result;
    }

    @Override
    public void initView() {
        binding.ivBack.setOnClickListener(v -> requireActivity().finish());
        tabSportAdapter = new TabSportResultAdapter(new ArrayList<>(), viewModel.getMatchGames());
        binding.tabSportType.setAdapter(tabSportAdapter);
        binding.tabSportType.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false));
        binding.tabSportType.setHasFixedSize(true);
        tabSportAdapter.setOnItemClickListener((adapter, view, position) -> {
            CfLog.i("position   " + position);
            if (sportTypePos != position) {
                hideSearchView();
                tabSportAdapter.setSelectedPosition(position);
                sportTypePos = position;
                long startTime = dateResultList.get(searchDatePos).getTime();
                showLoadDialog();
                viewModel.matchResultPage(String.valueOf(startTime), String.valueOf(startTime + 86400000L), playMethodPos, String.valueOf(getSportId()));
            }

        });
        tabSportAdapter.setSelectedPosition(0);

        if (TextUtils.equals(mPlatform, PLATFORM_FBXC) || TextUtils.equals(mPlatform, PLATFORM_FB)) {
            dateResultList = TimeUtils.getLastDays(30);
        } else {
            dateResultList = TimeUtils.getLastDays(7);
        }

        for (int i = 0; i < dateResultList.size(); i++) {
            TextView textView = new TextView(requireContext());
            if (i == 0) {
                textView.setText("今日");
            } else {
                textView.setText(TimeUtils.getTime(dateResultList.get(i), TimeUtils.FORMAT_MM_DD));
            }
            ColorStateList colorStateList = getResources().getColorStateList(R.color.bt_color_bet_top_tab_item_text);
            textView.setTextColor(colorStateList);
            if (i == 0) {
                textView.setTextSize(16);
            } else {
                textView.setTextSize(14);
            }
            binding.tabSearchDate.addTab(binding.tabSearchDate.newTab().setCustomView(textView));
        }
        binding.tabSearchDate.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                ((TextView) tab.getCustomView()).setTextSize(16);
                if (searchDatePos != tab.getPosition()) {
                    searchDatePos = tab.getPosition();
                    long startTime = dateResultList.get(searchDatePos).getTime();
                    if (!tabSportAdapter.getData().isEmpty()) {
                        showLoadDialog();
                        viewModel.matchResultPage(String.valueOf(startTime), String.valueOf(startTime + 86400000L), playMethodPos, String.valueOf(getSportId()));
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ((TextView) tab.getCustomView()).setTextSize(14);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initSearch() {
        binding.ivwGameSearch.setOnClickListener(v -> {
            binding.clGameSearch.setVisibility(View.VISIBLE);
            binding.ivwGameSearch.setVisibility(View.GONE);
            binding.tabSearchDate.setVisibility(View.INVISIBLE);
            String time;
            if (binding.tabSearchDate.getSelectedTabPosition() == 0) {
                time = "今日";
            } else {
                time = TimeUtils.getTime(dateResultList.get(binding.tabSearchDate.getSelectedTabPosition()), TimeUtils.FORMAT_MM_DD);
            }
            binding.tvTime.setText(time);
        });
        binding.tvwCancel.setOnClickListener(v -> {
            hideSearchView();
        });

        binding.edtGameSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void search(String query) {
        ArrayList<League> list = new ArrayList<League>();
        String normalizedQuery = query.toLowerCase(Locale.getDefault()).trim();// 去除空格并转换为小写
        if (query.isEmpty()) {
            list.addAll(allLeagueList);
        } else {
            for (League league : allLeagueList) {
                if (league.getLeagueName().contains(normalizedQuery)) {
                    list.add(league);
                } else {
                    for (Match match : league.getMatchList()) {
                        if (match.getTeamMain().contains(normalizedQuery) || match.getTeamVistor().contains(normalizedQuery)) {
                            list.add(league);
                        }
                    }
                }
            }
        }
        resultAdapter.setData(list);
        extractedLeague();
    }

    /**
     * 隐藏搜索UI
     */
    private void hideSearchView() {
        binding.clGameSearch.setVisibility(View.GONE);
        binding.ivwGameSearch.setVisibility(View.VISIBLE);
        binding.tabSearchDate.setVisibility(View.VISIBLE);
        if (!TextUtils.equals(binding.edtGameSearch.getText().toString(), "")) {
            binding.edtGameSearch.setText("");
        }
    }

    //private void autoClickItem(RecyclerView recyclerView, int position) {
    //    // 找到指定位置的ViewHolder
    //    RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
    //    if (viewHolder != null) {
    //        // 获取itemView并模拟点击
    //        viewHolder.itemView.performClick();
    //    } else {
    //        // 如果ViewHolder为空，可能是因为RecyclerView还没有完全渲染或item不可见，需要滚动到该位置并再尝试
    //        recyclerView.post(() -> {
    //            RecyclerView.ViewHolder vh = recyclerView.findViewHolderForAdapterPosition(position);
    //            if (vh != null) {
    //                vh.itemView.performClick();
    //            }
    //        });
    //    }
    //}

    @Override
    public void initData() {
        viewModel.postMerchant();
    }

    @Override
    public void initViewObservable() {
        //只会在初始化执行一次
        viewModel.resultData.observe(this, result -> {
            mResult = result;
            if (playMethodPos == 0) {
                tabSportAdapter.setList(mResult.get("1"));
            } else if (playMethodPos == 1) {
                tabSportAdapter.setList(mResult.get("2"));
            }
            long startTime = dateResultList.get(searchDatePos).getTime();
            showLoadDialog();
            viewModel.matchResultPage(String.valueOf(startTime), String.valueOf(startTime + 86400000L), playMethodPos, String.valueOf(getSportId()));
        });
        viewModel.resultLeagueData.observe(this, list -> {
            dismissLoadDialog();
            allLeagueList = list;
            if (resultAdapter == null) {
                resultAdapter = new LeagueResultAdapter(requireContext(), list);
                binding.rvLeague.setAdapter(resultAdapter);
                //防止用户搜索时，resultAdapter还为空
                initSearch();
            } else {//搞个加载圈
                resultAdapter.setData(list);
            }
            if (list.isEmpty()) {
                binding.llEmpty.llEmpty.setVisibility(View.VISIBLE);
            } else {
                binding.llEmpty.llEmpty.setVisibility(View.GONE);
            }
            extractedLeague();
        });
        viewModel.resultErrorLeagueData.observe(this, it -> dismissLoadDialog());
    }

    private void extractedLeague() {
        for (int i = 0; i < resultAdapter.getGroupCount(); i++) {
            binding.rvLeague.expandGroup(i);
        }
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public TemplateMainViewModel initViewModel() {
        if (!TextUtils.equals(mPlatform, PLATFORM_PM) && !TextUtils.equals(mPlatform, PLATFORM_PMXC)) {
            AppViewModelFactory factory = AppViewModelFactory.getInstance((Application) Utils.getContext());
            return new ViewModelProvider(this, factory).get(FBMainViewModel.class);
        } else {
            PMAppViewModelFactory factory = PMAppViewModelFactory.getInstance((Application) Utils.getContext());
            return new ViewModelProvider(this, factory).get(PMMainViewModel.class);
        }
    }

    public int getSportId() {
        if (TextUtils.equals(mPlatform, PLATFORM_PM) || TextUtils.equals(mPlatform, PLATFORM_PMXC)) {
            return tabSportAdapter.getItem(sportTypePos == -1 ? 0 : sportTypePos).menuId;
        } else {
            return tabSportAdapter.getItem(sportTypePos == -1 ? 0 : sportTypePos).id;
        }
    }

    public void showLoadDialog() {
//        if (binding.ivLoading.getVisibility() == View.GONE) {
//            binding.ivLoading.setVisibility(View.VISIBLE);
//            ObjectAnimator.ofFloat(binding.ivLoading, "rotation", 0f, 360f).setDuration(700).start();
//        }
        binding.lavLoading.playAnimation();
    }

    public void dismissLoadDialog() {
//        binding.ivLoading.clearAnimation();
//        binding.ivLoading.setVisibility(View.GONE);
        binding.lavLoading.cancelAnimation();
        binding.lavLoading.setVisibility(View.GONE);

    }

}
