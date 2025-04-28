package com.xtree.live.ui.main.fragment;

import static com.uber.autodispose.AutoDispose.autoDisposable;
import static com.xtree.base.utils.BtDomainUtil.KEY_PLATFORM;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_PM;
import static com.xtree.base.utils.BtDomainUtil.PLATFORM_PMXC;
import static com.xtree.live.ui.main.activity.LiveDetailActivity.KEY_MATCH;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gyf.immersionbar.ImmersionBar;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.ClickUtil;
import com.xtree.bet.bean.ui.Category;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.PlayType;
import com.xtree.bet.contract.BetContract;
import com.xtree.bet.manager.BtCarManager;
import com.xtree.bet.ui.fragment.BtCarDialogFragment;
import com.xtree.bet.ui.viewmodel.TemplateBtDetailViewModel;
import com.xtree.bet.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.bet.ui.viewmodel.factory.PMAppViewModelFactory;
import com.xtree.bet.ui.viewmodel.fb.FbBtDetailViewModel;
import com.xtree.bet.ui.viewmodel.pm.PmBtDetailViewModel;
import com.xtree.bet.util.MatchDeserializer;
import com.xtree.live.BR;
import com.xtree.live.R;
import com.xtree.live.databinding.FragmentBetBinding;
import com.xtree.live.ui.main.viewmodel.LiveViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.bus.RxBus;
import me.xtree.mvvmhabit.utils.RxUtils;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

@Route(path = RouterFragmentPath.Live.PAGER_LIVE_BET)

public class LiveBetFragment extends BaseFragment<FragmentBetBinding, TemplateBtDetailViewModel> implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private List<Category> mCategories = new ArrayList<>();
    private Match mMatch;
    private LiveBtDetailOptionFragment fragment;
    private int tabPos;
    private String mPlatform = SPUtils.getInstance().getString(KEY_PLATFORM);

    @Override
    public void initView() {
        CfLog.d("initView mMatch:" + mMatch);
        initImmersionBar();
        Gson gson = new GsonBuilder().serializeNulls().registerTypeAdapter(Match.class, new MatchDeserializer()).create();
        mMatch = gson.fromJson(SPUtils.getInstance().getString(KEY_MATCH), Match.class);
        binding.tabCategoryType.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabPos = tab.getPosition();
                for (int i = 0; i < binding.tabCategoryType.getTabCount(); i++) {
                    if (tabPos == i) {
                        binding.tabCategoryType.getTabAt(i).getCustomView().setBackgroundResource(R.drawable.shape_live_bet_selected_gradient_33);
                    } else {
                        binding.tabCategoryType.getTabAt(i).getCustomView().setBackgroundResource(com.xtree.bet.R.drawable.bt_bg_category_tab);
                    }
                }

                updateOptionData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        binding.rlCg.setOnClickListener(this);
        binding.ivExpand.setOnClickListener(this);
    }

    @Override
    public void initData() {
        //mMatch = getIntent().getParcelableExtra(KEY_MATCH);
        Gson gson = new GsonBuilder().serializeNulls().registerTypeAdapter(Match.class, new MatchDeserializer()).create();
        mMatch = gson.fromJson(SPUtils.getInstance().getString(KEY_MATCH), Match.class);
        CfLog.d("initData mMatch:" + mMatch);
        if (mMatch != null) {
            viewModel.getMatchDetail(mMatch.getId());
            viewModel.getCategoryList(String.valueOf(mMatch.getId()), mMatch.getSportId());
            viewModel.addSubscription();
            setCgBtCar();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        Observable.interval(5, 5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .as(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this))).subscribe(aLong -> {
            viewModel.getMatchDetail(mMatch.getId());
            viewModel.getCategoryList(String.valueOf(mMatch.getId()), mMatch.getSportId());
        });
    }


    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_bet;
    }

    @Override
    public int initVariableId() {
        return BR.model;
    }

    @Override
    public TemplateBtDetailViewModel initViewModel() {
        if (!TextUtils.equals(mPlatform, PLATFORM_PM) && !TextUtils.equals(mPlatform, PLATFORM_PMXC)) {
            com.xtree.bet.ui.viewmodel.factory.AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
            return new ViewModelProvider(this, factory).get(FbBtDetailViewModel.class);
        } else {
            PMAppViewModelFactory factory = PMAppViewModelFactory.getInstance(getActivity().getApplication());
            return new ViewModelProvider(this, factory).get(PmBtDetailViewModel.class);
        }
    }

    /**
     * 更新投注选项数据
     */
    private void updateOptionData() {
        if (fragment == null) {
            if (mCategories != null && !mCategories.isEmpty()) {
                fragment = LiveBtDetailOptionFragment.getInstance(mMatch, (ArrayList<PlayType>) mCategories.get(tabPos).getPlayTypeList());
                FragmentTransaction trans = getActivity().getSupportFragmentManager().beginTransaction();
                trans.replace(com.xtree.bet.R.id.fl_option, fragment);
                trans.commitAllowingStateLoss();
            }
        } else {
            if (mCategories != null && !mCategories.isEmpty()) {
                if (tabPos < mCategories.size()) {
                    RxBus.getDefault().post(new BetContract(BetContract.ACTION_OPTION_CHANGE, mCategories.get(tabPos).getPlayTypeList()));
                }
            }
        }
        /*if (detailPlayTypeAdapter == null) {
            detailPlayTypeAdapter = new MatchDetailAdapter(BtDetailActivity.this, mCategories.get(tabPos).getPlayTypeList());
            //binding.aelOption.setAdapter(detailPlayTypeAdapter);
        } else {
            detailPlayTypeAdapter.setData(mCategories.get(tabPos).getPlayTypeList());
        }*/
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == com.xtree.bet.R.id.rl_cg) {
            if (BtCarManager.size() <= 1) {
                ToastUtils.showLong(getText(com.xtree.bet.R.string.bt_bt_must_have_two_match));
                return;
            }
            if (ClickUtil.isFastClick()) {
                return;
            }
            BtCarDialogFragment btCarDialogFragment = new BtCarDialogFragment();
            btCarDialogFragment.show(getActivity().getSupportFragmentManager(), "btCarDialogFragment");
        } else if (id == com.xtree.bet.R.id.iv_expand) {
            CfLog.d("======== LiveBetFragment onClick expand =========");
            if (fragment != null) {
                fragment.expand();
            }
        }
    }

    @Override
    public void onRefresh() {
    }

    @Override
    public void initViewObservable() {
        viewModel.matchData.observe(this, match -> {
            this.mMatch = match;
            CfLog.d("initViewObservable mMatch:" + mMatch);
            if (match == null) {
                return;
            }

            binding.tvTeamMain.setText(mMatch.getTeamMain());
            binding.tvTeamVisisor.setText(mMatch.getTeamVistor());
            Glide.with(this).load(match.getIconMain())
                    //.apply(new RequestOptions().placeholder(placeholderRes))
                    .into(binding.ivLogoMain);

            Glide.with(this).load(match.getIconVisitor())
                    //.apply(new RequestOptions().placeholder(placeholderRes))
                    .into(binding.ivLogoVisitor);
        });

        viewModel.updateCagegoryListData.observe(this, categories -> {
            mCategories = categories;
        });
        viewModel.categoryListData.observe(this, categories -> {
            if (categories.isEmpty()) {
                binding.tabCategoryType.setVisibility(View.GONE);
                binding.flOption.setVisibility(View.GONE);
                binding.llEnd.llEmpty.setVisibility(View.VISIBLE);
            } else {
                binding.flOption.setVisibility(View.VISIBLE);
                binding.llEnd.llEmpty.setVisibility(View.GONE);
                binding.tabCategoryType.setVisibility(View.VISIBLE);
            }
            //if (mCategories.size() != categories.size()) {
            mCategories = categories;
            if (binding.tabCategoryType.getTabCount() == 0) {
                for (int i = 0; i < categories.size(); i++) {
                    View view = LayoutInflater.from(getContext()).inflate(com.xtree.bet.R.layout.live_layout_bet_catory_tab_item, null);
                    TextView tvName = view.findViewById(com.xtree.bet.R.id.tab_item_name);
                    String name = categories.get(i) == null ? "" : categories.get(i).getName();

                    tvName.setText(name);
                    ColorStateList colorStateList = getResources().getColorStateList(com.xtree.bet.R.color.bt_color_category_tab_text);
                    tvName.setTextColor(colorStateList);

                    binding.tabCategoryType.addTab(binding.tabCategoryType.newTab().setCustomView(view));

                }
            } else {
                for (int i = 0; i < categories.size(); i++) {
                    try {
                        if (binding.tabCategoryType == null) {
                            CfLog.e("=========binding.tabCategoryType == null=========");
                        }
                        if (categories.get(i) == null && binding.tabCategoryType != null && i < binding.tabCategoryType.getTabCount()) {
                            binding.tabCategoryType.removeTabAt(i);
                            if (binding.tabCategoryType.getTabCount() == 0) {
                                binding.flOption.setVisibility(View.GONE);
                                binding.llEnd.llEmpty.setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (Exception e) {
                        CfLog.e("binding.tabCategoryType.getTabCount()-------" + binding.tabCategoryType.getTabCount() + "-----" + i);
                        CfLog.e(e.getMessage());
                    }
                }
                viewModel.updateCategoryData();
            }
            updateOptionData();
            /*} else {
                mCategories = categories;
                updateOptionData();
            }*/
        });

        viewModel.betContractListData.observe(this, betContract -> {
            if (betContract.action == BetContract.ACTION_BTCAR_CHANGE) {
                setCgBtCar();
                updateOptionData();
                if (!BtCarManager.isCg()) {
                    binding.rlCg.postDelayed(() -> RxBus.getDefault().postSticky(new BetContract(BetContract.ACTION_OPEN_TODAY)), 500);
                }
            } else if (betContract.action == BetContract.ACTION_OPEN_CG) {
                setCgBtCar();
                updateOptionData();
            }
        });
    }

    /**
     * 设置串关数量与显示与否
     */
    public void setCgBtCar() {
        binding.tvCgCount.setText(String.valueOf(BtCarManager.size()));
        binding.rlCg.setVisibility(!BtCarManager.isCg() ? View.GONE : BtCarManager.isEmpty() ? View.GONE : View.VISIBLE);
    }

}
