package com.xtree.lottery.ui.view.betviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.vo.UserMethodsResponse;
import com.xtree.base.widget.MsgDialog;
import com.xtree.base.widget.TipDialog;
import com.xtree.lottery.R;
import com.xtree.lottery.data.config.Lottery;
import com.xtree.lottery.data.source.request.LotteryBetRequest;
import com.xtree.lottery.rule.betting.data.RulesEntryData;
import com.xtree.lottery.ui.lotterybet.model.LotteryBetsModel;

import java.util.List;

/**
 * Created by KAKA on 2024/5/3.
 * Describe: 投注组件的基础视图
 */
public abstract class BetBaseView extends FrameLayout {

    public ObservableField<List<LotteryBetRequest.BetOrderData>> betData = new ObservableField<>();
    public ObservableField<Object> betCodes = new ObservableField<>();
    protected LiveData<RulesEntryData.RulesResultData> rulesResultDataLiveData;
    private LotteryBetsModel model;
    private UserMethodsResponse.DataDTO.PrizeGroupDTO prizeGroup;
    private Lottery lottery;
    private BasePopupView pop;

    public BetBaseView(@NonNull Context context) {
        super(context);
    }

    public BetBaseView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public UserMethodsResponse.DataDTO.PrizeGroupDTO getPrizeGroup() {
        return prizeGroup;
    }

    public void setPrizeGroup(UserMethodsResponse.DataDTO.PrizeGroupDTO prizeGroup) {
        this.prizeGroup = prizeGroup;
    }

    public LotteryBetsModel getModel() {
        return model;
    }

    public void setModel(LiveData<RulesEntryData.RulesResultData> rulesResultDataLiveData, LotteryBetsModel model, @Nullable UserMethodsResponse.DataDTO.PrizeGroupDTO prizeGroup, Lottery lottery) {
        this.rulesResultDataLiveData = rulesResultDataLiveData;
        this.model = model;
        this.prizeGroup = prizeGroup;
        this.lottery = lottery;
    }

    public abstract void clearBet();

    /**
     * 提示弹窗
     */
    public void showTipDialog(String msg) {
        MsgDialog dialog = new MsgDialog(getContext(), getContext().getString(R.string.txt_kind_tips), msg, true, new TipDialog.ICallBack() {
            @Override
            public void onClickLeft() {

            }

            @Override
            public void onClickRight() {
                if (pop != null) {
                    pop.dismiss();
                }
            }
        });

        pop = new XPopup.Builder(getContext())
                .dismissOnTouchOutside(true)
                .dismissOnBackPressed(true)
                .asCustom(dialog).show();
    }
}
