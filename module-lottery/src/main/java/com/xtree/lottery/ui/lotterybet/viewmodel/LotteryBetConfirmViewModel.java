package com.xtree.lottery.ui.lotterybet.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.base.net.HttpCallBack;
import com.xtree.lottery.R;
import com.xtree.lottery.data.LotteryRepository;
import com.xtree.lottery.data.source.request.LotteryBetRequest;
import com.xtree.lottery.data.source.response.BonusNumbersResponse;
import com.xtree.lottery.ui.lotterybet.model.ChasingNumberRequestModel;

import java.util.ArrayList;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.http.BaseResponse;

/**
 * Created by KAKA on 2024/11/5.
 * Describe:
 */
public class LotteryBetConfirmViewModel extends BaseViewModel<LotteryRepository> {

    public LotteryBetConfirmViewModel(@NonNull Application application) {
        super(application);
    }

    public LotteryBetConfirmViewModel(@NonNull Application application, LotteryRepository model) {
        super(application, model);
    }

    public final ObservableField<ArrayList<BindModel>> datas = new ObservableField<>(new ArrayList<>());

    public final ObservableField<ArrayList<Integer>> itemType = new ObservableField<>(
            new ArrayList<Integer>() {
                {
                    add(R.layout.item_lottery_bet_confirm);
                }
            });
    public final ArrayList<BindModel> bindModels = new ArrayList<>();

    public final MutableLiveData<String> bonusNumberTitle = new MutableLiveData<>();
    public final MutableLiveData<String> totalMoney = new MutableLiveData<>();
    public final MutableLiveData<Boolean> containSolo = new MutableLiveData<>(false);
    //追号入参
    public final MutableLiveData<ChasingNumberRequestModel> chasingNumberParams = new MutableLiveData<>();

    public LotteryBetsViewModel betsViewModel;

    public void initData(FragmentActivity activity, ArrayList<LotteryBetRequest.BetOrderData> betList) {

        bindModels.clear();
        datas.get().clear();
        bonusNumberTitle.setValue("");

        betsViewModel.bonusNumbersLiveData.observe(activity, new Observer<BonusNumbersResponse>() {
            @Override
            public void onChanged(BonusNumbersResponse bonusNumbersResponse) {

                if (bonusNumbersResponse != null && bonusNumbersResponse.getData() != null) {
                    List<BonusNumbersResponse.DataDTO> bdatas = bonusNumbersResponse.getData();
                    if (!bdatas.isEmpty()) {
                        String issue = bdatas.get(0).getIssue();
                        if (issue != null) {
                            bonusNumberTitle.setValue("确认要加入" + issue + "期?");
                        }
                    }
                }

            }
        });
        betsViewModel.getBonusNumbers();

        bindModels.addAll(betList);

        datas.set(bindModels);

        int money = 0;
        boolean solo = false;
        for (LotteryBetRequest.BetOrderData betOrderData : betList) {
            money += betOrderData.getMoney();
            if (betOrderData.isSolo()) {
                solo = true;
            }
        }
        if (solo) {
            containSolo.setValue(true);
        }

        totalMoney.setValue(String.valueOf(money));
    }

    /**
     * 取消
     */
    public void cancle() {
        finish();
    }

    /**
     * 投注
     */
    public void bet() {
        LotteryBetRequest lotteryBetRequest = new LotteryBetRequest();
        ArrayList<LotteryBetRequest.BetOrderData> betOrders = new ArrayList<>();

        int money = 0;
        int nums = 0;
        for (BindModel bindModel : datas.get()) {
            LotteryBetRequest.BetOrderData data = (LotteryBetRequest.BetOrderData) bindModel;
            money += data.getMoney();
            nums += data.getNums();
            betOrders.add(data);
        }

        lotteryBetRequest.setLtProject(betOrders);
        lotteryBetRequest.setLotteryid(betsViewModel.lotteryLiveData.getValue().getId());
        lotteryBetRequest.setCurmid(betsViewModel.lotteryLiveData.getValue().getCurmid());
        lotteryBetRequest.setLtIssueStart(bonusNumberTitle.getValue());
        lotteryBetRequest.setLtProject(betOrders);
        lotteryBetRequest.setLtTotalMoney(money);
        lotteryBetRequest.setLtTotalNums(nums);
        lotteryBetRequest.setPlaySource(6);

        model.bet(lotteryBetRequest, chasingNumberParams.getValue().getParmes()).subscribe(new HttpCallBack<BaseResponse>() {
            @Override
            public void onResult(BaseResponse response) {
                chasingNumberParams.setValue(null);
            }
        });
    }

    /**
     * 清除单挑
     */
    public void doClear() {

    }
}
