package com.xtree.live.ui.main.fragment;

import static com.xtree.base.utils.BtDomainUtil.KEY_PLATFORM;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.bet.bean.ui.Category;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.PlayType;
import com.xtree.bet.contract.BetContract;
import com.xtree.bet.util.MatchDeserializer;
import com.xtree.live.BR;
import com.xtree.live.R;
import com.xtree.live.data.factory.AppViewModelFactory;
import com.xtree.live.databinding.FragmentBetBinding;
import com.xtree.live.ui.main.viewmodel.LiveViewModel;

import java.util.ArrayList;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.bus.RxBus;
import me.xtree.mvvmhabit.utils.SPUtils;

@Route(path = RouterFragmentPath.Live.PAGER_LIVE_BET)
public class LiveBetFragment extends BaseFragment<FragmentBetBinding, LiveViewModel> {
    private final static String KEY_MATCH = "KEY_MATCH_ID";
    private List<Category> mCategories = new ArrayList<>();
    private Match mMatch;
    private LiveBtDetailOptionFragment fragment;
    private int tabPos;
    private String mPlatform = SPUtils.getInstance().getString(KEY_PLATFORM);
    @Override
    public void initView() {
        Gson gson = new GsonBuilder().serializeNulls().registerTypeAdapter(Match.class, new MatchDeserializer()).create();
        mMatch = gson.fromJson(SPUtils.getInstance().getString(KEY_MATCH), Match.class);
//        viewModel.getMatchDetail(mMatch.getId());
//        viewModel.getCategoryList(String.valueOf(mMatch.getId()), mMatch.getSportId());
//        viewModel.addSubscription();

        binding.tabCategoryType.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabPos = tab.getPosition();
                for(int i = 0; i < binding.tabCategoryType.getTabCount(); i ++){
                    if(tabPos == i){
                        binding.tabCategoryType.getTabAt(i).getCustomView().setBackgroundResource(R.drawable.shape_gradient_33);
                    }else {
                        binding.tabCategoryType.getTabAt(i).getCustomView().setBackgroundResource(R.drawable.shape_gradient_white_33);
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
    public LiveViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(LiveViewModel.class);
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

}
