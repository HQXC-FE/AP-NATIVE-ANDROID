package com.xtree.lottery.ui.view.viewmodel;

import androidx.databinding.Observable;
import androidx.databinding.ObservableField;

import com.xtree.lottery.ui.lotterybet.data.LotteryMoneyData;
import com.xtree.lottery.ui.view.LotteryMoneyView;
import com.xtree.lottery.ui.view.model.LotteryMoneyModel;

/**
 * Created by KAKA on 2024/5/2.
 * Describe:
 */
public class LotteryMoneyViewModel {

    public static final String FACTOR_SUFFIX = "倍";
    public ObservableField<String> factorData = new ObservableField<>();
    public ObservableField<LotteryMoneyModel> unitData = new ObservableField<>();

    private LotteryMoneyView.onChangeMoneyListener onChangeMoneyListener = null;

    public void initData() {
        factorData.set("1" + FACTOR_SUFFIX);

        factorData.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (onChangeMoneyListener != null) {
                    onChangeMoneyListener.onChange(getMoneyData());
                }
            }
        });

        unitData.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (onChangeMoneyListener != null) {
                    onChangeMoneyListener.onChange(getMoneyData());
                }
            }
        });
    }

    public long getFactor() {
        try {
            String s = factorData.get();
            long f;
            if (s == null) return 1;
            if (s.contains(FACTOR_SUFFIX)) {
                f = Long.parseLong(s.replace(FACTOR_SUFFIX, ""));
            } else {
                f = Long.parseLong(s);
            }
            return f;
        } catch (NumberFormatException e) {
            // 处理转换失败的情况
            e.printStackTrace();
            return 1;
        }
    }

    public void add() {
        long f = getFactor();
        f++;
        factorData.set(f + FACTOR_SUFFIX);
    }

    public void fixTimes(long times) {
        factorData.set(times + FACTOR_SUFFIX);
    }

    public void subtrac() {
        long f = getFactor();
        f--;
        if (f <= 0) {
            f = 1;
        }
        factorData.set(f + FACTOR_SUFFIX);
    }

    public void concat(String number) {
        long f = getFactor();
        factorData.set(f + number + FACTOR_SUFFIX);
    }

    public float getMoney() {
        long f = getFactor();
        LotteryMoneyModel lotteryMoneyModel = unitData.get();
        if (lotteryMoneyModel != null) {
            return f * lotteryMoneyModel.getRate();
        }
        return 0f;
    }

    public LotteryMoneyData getMoneyData() {
        long f = getFactor();
        LotteryMoneyModel lotteryMoneyModel = unitData.get();
        return new LotteryMoneyData(lotteryMoneyModel, f);
    }

    public void setOnChangeMoneyListener(LotteryMoneyView.onChangeMoneyListener onChangeMoneyListener) {
        this.onChangeMoneyListener = onChangeMoneyListener;
    }
}
