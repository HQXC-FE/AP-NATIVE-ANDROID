package com.xtree.lottery.ui.lotterybet;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModelProvider;

import com.lxj.xpopup.util.KeyboardUtils;
import com.xtree.base.vo.UserMethodsResponse;
import com.xtree.lottery.BR;
import com.xtree.lottery.R;
import com.xtree.lottery.data.config.Lottery;
import com.xtree.lottery.data.source.request.LotteryBetRequest;
import com.xtree.lottery.data.source.vo.IssueVo;
import com.xtree.lottery.data.source.vo.MenuMethodsData;
import com.xtree.lottery.data.source.vo.RecentLotteryVo;
import com.xtree.lottery.databinding.FragmentLotteryBetsBinding;
import com.xtree.lottery.inter.ParentChildCommunication;
import com.xtree.lottery.rule.betting.data.RulesEntryData;
import com.xtree.lottery.ui.lotterybet.model.LotteryBetsModel;
import com.xtree.lottery.ui.lotterybet.viewmodel.LotteryBetsViewModel;
import com.xtree.lottery.ui.view.LotteryDrawView;
import com.xtree.lottery.ui.view.model.LotteryMoneyModel;
import com.xtree.lottery.ui.viewmodel.LotteryViewModel;
import com.xtree.lottery.ui.viewmodel.factory.AppViewModelFactory;
import com.xtree.lottery.utils.AnimUtils;
import com.xtree.lottery.utils.LotteryAnalyzer;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.bus.RxBus;

/**
 * Created by KAKA on 2024/5/1.
 * Describe: 彩种投注
 */
public class LotteryBetsFragment extends BaseFragment<FragmentLotteryBetsBinding, LotteryBetsViewModel> implements ParentChildCommunication {
    private ParentChildCommunication communication;
    private ArrayList<IssueVo> mIssues;

    public static LotteryBetsFragment newInstance(Lottery lottery) {

        RxBus.getDefault().postSticky(lottery);
        return new LotteryBetsFragment();
    }

    protected void initImmersionBar() {

    }

    @Override
    public void initView() {
        binding.getRoot().setOnTouchListener((v, event) -> {
            KeyboardUtils.hideSoftInput(getActivity().getWindow());
            return false;
        });

        binding.lotteryBetsPrizeOption.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_lottery_bets;
    }

    @Override
    public int initVariableId() {
        return BR.model;
    }

    @Override
    public LotteryBetsViewModel initViewModel() {
        LotteryBetsViewModel viewmodel = new ViewModelProvider(getActivity()).get(LotteryBetsViewModel.class);
        AppViewModelFactory factory = AppViewModelFactory.getInstance(requireActivity().getApplication());
        viewmodel.setModel(factory.getmRepository());
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

        binding.getModel().initData(getActivity(), lottery);

        binding.lotteryBetsMoneyView.setOnChangeMoneyListener(moneyData -> {
            //更新金额数据
            binding.getModel().moneyLiveData.setValue(moneyData);

            //更新投注数据
            List<LotteryBetRequest.BetOrderData> betOrderList = binding.lotteryBetsBetlayout.getBet();
            Object codes = binding.lotteryBetsBetlayout.getCodes();

            if (betOrderList != null && betOrderList.size() > 0) {
                for (LotteryBetRequest.BetOrderData betOrderData : betOrderList) {
                    LotteryMoneyModel moneyModel = binding.lotteryBetsMoneyView.getMoneyData().getMoneyModel();
                    int factor = binding.lotteryBetsMoneyView.getMoneyData().getFactor();
                    betOrderData.setMode(moneyModel.getModelId());
                    betOrderData.setTimes(factor);
                    betOrderData.setOmodel(viewModel.prizeData.getValue().getValue());

                    RulesEntryData.BetDTO betDTO = new RulesEntryData.BetDTO();
                    RulesEntryData.BetDTO.ModeDTO modeDTO = new RulesEntryData.BetDTO.ModeDTO();
                    modeDTO.setModeid(moneyModel.getModelId());
                    modeDTO.setName(moneyModel.getName());
                    modeDTO.setRate(String.valueOf(moneyModel.getRate()));
                    betDTO.setMode(modeDTO);
                    betDTO.setTimes(factor);
                    betDTO.setDisplay(new RulesEntryData.BetDTO.DisplayDTO());
                    betDTO.setSubmit(new RulesEntryData.SubmitDTO());
                    betDTO.setCodes(codes);
                    betDTO.setPoschoose(betOrderData.getPoschoose());
                    viewModel.rule(betDTO);
                }
            } else {
                binding.getModel().betLiveData.setValue(null);
            }
        });

        binding.lotteryBetsDrawview.setOnLotteryDrawListener(new LotteryDrawView.OnLotteryDrawListener() {
            @Override
            public void onRefresh(View view) {
                binding.getModel().lotteryViewModel.getRecentLottery();
                AnimUtils.rotateView(view);
            }

            @Override
            public void onSimulate(View view, ObservableField<List<String>> drawCode) {
                binding.getModel().lotteryViewModel.simulatedNumber(view,drawCode);
            }
        });

        binding.getModel().combinedPrizeBetLiveData.observe(this, combinedData -> {
            LotteryBetsModel lotteryBetsModel = combinedData.betModel;
            UserMethodsResponse.DataDTO.PrizeGroupDTO prizeGroup = combinedData.prizeGroup;
            Boolean isPrize = combinedData.isPrize;
            if ("lhc".equals(lottery.getLinkType())) {
                //六合彩视图需要奖金组获取赔率
                if (prizeGroup == null) {
                    return;
                }
                setBetData(lotteryBetsModel, prizeGroup, lottery);
            } else if (!isPrize) {
                setBetData(lotteryBetsModel, prizeGroup, lottery);
            } else {//非六合彩刷新奖金组
                binding.lotteryBetsBetlayout.setPrizeGroup(prizeGroup);
            }
        });

        binding.lotteryBetsBetlayout.setOnLotteryBetListener((betOrderList, codes) -> {
            if (betOrderList != null && betOrderList.size() > 0) {
                for (LotteryBetRequest.BetOrderData betOrderData : betOrderList) {
                    LotteryMoneyModel moneyModel = binding.lotteryBetsMoneyView.getMoneyData().getMoneyModel();
                    int factor = binding.lotteryBetsMoneyView.getMoneyData().getFactor();
                    betOrderData.setMode(moneyModel.getModelId());
                    betOrderData.setTimes(factor);

                    if (viewModel.prizeData.getValue() != null) {
                        betOrderData.setOmodel(viewModel.prizeData.getValue().getValue());
                    }

                    RulesEntryData.BetDTO betDTO = new RulesEntryData.BetDTO();
                    RulesEntryData.BetDTO.ModeDTO modeDTO = new RulesEntryData.BetDTO.ModeDTO();
                    modeDTO.setModeid(moneyModel.getModelId());
                    modeDTO.setName(moneyModel.getName());
                    modeDTO.setRate(String.valueOf(moneyModel.getRate()));
                    betDTO.setMode(modeDTO);
                    betDTO.setTimes(factor);
                    betDTO.setDisplay(new RulesEntryData.BetDTO.DisplayDTO());
                    betDTO.setSubmit(new RulesEntryData.SubmitDTO());
                    betDTO.setCodes(codes);
                    betDTO.setPoschoose(betOrderData.getPoschoose());
                    viewModel.rule(betDTO);
                }
            } else {
                binding.getModel().betLiveData.setValue(null);
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
                binding.lotteryBetsBetlayout.setLotteryNumbsHistory(lotteryNumbs);
                binding.lotteryBetsDrawview.setLottery(lottery);
                binding.lotteryBetsDrawview.setDrawCode(bonusNumbers.get(0));

            }
        });

        binding.lotteryBetsSavebetLayout.setOnClickListener(v -> {
            binding.getModel().saveBetOrder();
            binding.lotteryBetsBetlayout.clearBet();
        });

    }

    private void setBetData(LotteryBetsModel betsModel, @Nullable UserMethodsResponse.DataDTO.PrizeGroupDTO prizeGroup, Lottery lottery) {
        //设置选注形态
        binding.lotteryBetsBetlayout.setData(viewModel.rulesResultDataLiveData, betsModel, prizeGroup, lottery);

        //设置投注金额
        if (betsModel.getMenuMethodLabelData() != null && betsModel.getMenuMethodLabelData().getMoneyModes() != null) {
            ArrayList<LotteryMoneyModel> moneyModelList = new ArrayList<>();
            for (MenuMethodsData.LabelsDTO.Labels1DTO.Labels2DTO.MoneyModesDTO moneyMode : betsModel.getMenuMethodLabelData().getMoneyModes()) {
                moneyModelList.add(new LotteryMoneyModel(moneyMode.getName(), moneyMode.getRate(), moneyMode.getModeid()));
            }
            binding.lotteryBetsMoneyView.setMoneyUnit(moneyModelList, lottery);
        }
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();
        viewModel.clearBetEvent.observe(this, s -> binding.lotteryBetsBetlayout.clearBet());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        viewModel.getUserBalance(null);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // 确保 Activity 实现了接口
        if (context instanceof ParentChildCommunication) {
            communication = (ParentChildCommunication) context;
        } else {
            throw new RuntimeException("Activity must implement ParentChildCommunication");
        }
    }

    // Fragment 提供方法给 Activity 调用
    @Override
    public void onFragmentSendData(@NonNull ArrayList<IssueVo> data) {
    }

    // Fragment 主动通过接口通知 Activity
    @Override
    public void onActivitySendData(@NotNull ArrayList<IssueVo> data) {
        if (communication != null) {
            //communication.onFragmentSendData("Data from Fragment");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        communication = null;
    }

}
