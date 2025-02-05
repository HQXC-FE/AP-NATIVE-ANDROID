package com.xtree.lottery.ui.lotterybet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.util.KeyboardUtils;
import com.xtree.lottery.BR;
import com.xtree.lottery.R;
import com.xtree.lottery.data.config.Lottery;
import com.xtree.lottery.data.source.request.LotteryBetRequest;
import com.xtree.lottery.data.source.vo.RecentLotteryVo;
import com.xtree.lottery.databinding.FragmentLotteryHandicapBinding;
import com.xtree.lottery.ui.lotterybet.model.LotteryBetsModel;
import com.xtree.lottery.ui.lotterybet.viewmodel.LotteryHandicapViewModel;
import com.xtree.lottery.ui.view.LotteryBetView;
import com.xtree.lottery.ui.view.LotteryDrawView;
import com.xtree.lottery.ui.view.LotteryRoadMapDialog;
import com.xtree.lottery.ui.viewmodel.LotteryViewModel;
import com.xtree.lottery.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.lottery.utils.AnimUtils;
import com.xtree.lottery.utils.LotteryAnalyzer;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.bus.RxBus;

/**
 * Created by KAKA on 2024/5/16.
 * Describe: 盘口玩法
 */
public class LotteryHandicapFragment extends BaseFragment<FragmentLotteryHandicapBinding, LotteryHandicapViewModel> {

    private Lottery lottery;

    private int mTabPosition = 0;
    private String mTabTitle = "";

    public static LotteryHandicapFragment newInstance(Lottery lottery) {
        RxBus.getDefault().postSticky(lottery);
        return new LotteryHandicapFragment();
    }

    protected void initImmersionBar() {

    }

    @Override
    public void initView() {

        binding.getRoot().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                KeyboardUtils.hideSoftInput(getActivity().getWindow());
                return false;
            }
        });

    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_lottery_handicap;
    }

    @Override
    public int initVariableId() {
        return BR.model;
    }

    @Override
    public LotteryHandicapViewModel initViewModel() {
        LotteryHandicapViewModel viewmodel = new ViewModelProvider(getActivity()).get(LotteryHandicapViewModel.class);
        AppViewModelFactory instance = AppViewModelFactory.getInstance(getActivity().getApplication());
        viewmodel.setModel(instance.getmRepository());
        viewmodel.lotteryViewModel = new ViewModelProvider(getActivity()).get(LotteryViewModel.class);
        return viewmodel;
    }

    @Override
    public void initData() {
        super.initData();

        Lottery lottery = RxBus.getDefault().getStickyEvent(Lottery.class);

        if (lottery == null) {
            return;
        }

        this.lottery = lottery;

        binding.getModel().initData(getActivity(), lottery);

        binding.lotteryHandicapDrawview.setOnLotteryDrawListener(new LotteryDrawView.OnLotteryDrawListener() {
            @Override
            public void onRefresh(View view) {
                binding.getModel().lotteryViewModel.getRecentLottery();
                AnimUtils.rotateView(view);
            }
        });

        binding.getModel().currentBetModel.observe(this, new Observer<LotteryBetsModel>() {
            @Override
            public void onChanged(LotteryBetsModel lotteryBetsModel) {
                if (lotteryBetsModel != null) {
                    //设置选注形态
                    binding.lotteryHandicapBetlayout.setData(lotteryBetsModel, null, lottery);
                    mTabPosition = lotteryBetsModel.getPosition();
                    mTabTitle = lotteryBetsModel.getTitle();
//                    //设置投注金额
//                    ArrayList<LotteryMoneyModel> moneyModelList = new ArrayList<>();
//                    for (MenuMethodsResponse.DataDTO.LabelsDTO.Labels1DTO.Labels2DTO.MoneyModesDTO moneyMode : lotteryBetsModel.getMenuMethodLabelData().getMoneyModes()) {
//                        moneyModelList.add(new LotteryMoneyModel(moneyMode.getName(), moneyMode.getRate(), moneyMode.getModeid()));
//                    }
                }
            }
        });

        binding.lotteryHandicapBetlayout.setOnLotteryBetListener(new LotteryBetView.OnLotteryBetListener() {
            @Override
            public void onBetChange(List<LotteryBetRequest.BetOrderData> betOrderList, Object codes) {
                if (betOrderList != null && betOrderList.size() > 0) {
                    int money = Integer.parseInt(viewModel.moneyLiveData.getValue());
                    for (LotteryBetRequest.BetOrderData betOrderData : betOrderList) {
                        //默认是2分倍率
                        betOrderData.setMode(3);
                        betOrderData.setMoney(money);
                        betOrderData.setTimes((int) (money / 0.02));
                        betOrderData.setOmodel(viewModel.prizeData.getValue().getMode());
                        betOrderData.setNums(1);
                    }
                    binding.getModel().betLiveData.setValue(betOrderList);
                } else {
                    binding.getModel().betLiveData.setValue(null);
                }
            }
        });

        binding.getModel().lotteryViewModel.liveDataRecentList.observe(this, bonusNumbers -> {
            if (bonusNumbers != null && bonusNumbers.size() > 0) {
                ArrayList<String> lotteryNumbs = new ArrayList<>();
                for (RecentLotteryVo datum : bonusNumbers) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (String s : datum.getSplit_code()) {
                        stringBuilder.append(s + LotteryAnalyzer.SPLIT);
                    }
                    lotteryNumbs.add(stringBuilder.deleteCharAt(stringBuilder
                                    .lastIndexOf(LotteryAnalyzer.SPLIT))
                            .toString()
                    );
                }
                binding.lotteryHandicapBetlayout.setLotteryNumbsHistory(lotteryNumbs);

                binding.lotteryHandicapDrawview.setDrawCode(bonusNumbers.get(0));
            }
        });

        binding.lotteryHandicapRoadmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new XPopup.Builder(getContext())
                        .asCustom(LotteryRoadMapDialog.newInstance(
                                getContext(), getViewLifecycleOwner(), lottery.getId(), mTabPosition, mTabTitle)).show();
            }
        });

        binding.lotteryHandicapDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.lotteryHandicapBetlayout.clearBet();
                viewModel.betLiveData.setValue(null);
            }
        });
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.clearBetEvent.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                binding.lotteryHandicapBetlayout.clearBet();
            }
        });
    }

}
