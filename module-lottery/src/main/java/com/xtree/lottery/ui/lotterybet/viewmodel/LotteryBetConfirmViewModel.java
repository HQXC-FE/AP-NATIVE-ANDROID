package com.xtree.lottery.ui.lotterybet.viewmodel;

import static com.xtree.lottery.utils.EventConstant.EVENT_CLEAR_SOLO;

import android.app.Application;

import androidx.annotation.NonNull;
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
import com.xtree.lottery.utils.EventVo;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
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

    public final MutableLiveData<ArrayList<BindModel>> datas = new MutableLiveData<>(new ArrayList<>());

    public final MutableLiveData<ArrayList<Integer>> itemType = new MutableLiveData<>(
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
        datas.getValue().clear();
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

        datas.setValue(bindModels);

        double money = 0;
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
        for (BindModel bindModel : datas.getValue()) {
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

        HashMap<String, Object> parmes = new HashMap<>();
        if (chasingNumberParams.getValue() != null && chasingNumberParams.getValue().getParmes() != null) {
            parmes.putAll(chasingNumberParams.getValue().getParmes());
        }
        model.bet(lotteryBetRequest, parmes).subscribe(new HttpCallBack<BaseResponse>() {
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

        double money = 0;

        for (int i = 0; i < bindModels.size(); i++) {
            LotteryBetRequest.BetOrderData m = (LotteryBetRequest.BetOrderData) bindModels.get(i);
            if (m.isSolo()) {
                bindModels.remove(bindModels.get(i));
            } else {
                money += m.getMoney();
            }
        }

        //清除单挑广播
        EventBus.getDefault().post(new EventVo(EVENT_CLEAR_SOLO, ""));

        if (bindModels.size() == 0) {
            finish();
            return;
        }

        containSolo.setValue(false);

        totalMoney.setValue(String.valueOf(money));

        datas.setValue(bindModels);
    }
}
