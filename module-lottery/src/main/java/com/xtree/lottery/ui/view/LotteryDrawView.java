package com.xtree.lottery.ui.view;

import static com.xtree.lottery.rule.Matchers.k3Alias;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.Observable;

import com.xtree.base.mvvm.ExKt;
import com.xtree.lottery.R;
import com.xtree.lottery.data.config.Lottery;
import com.xtree.lottery.data.source.response.BonusNumbersResponse;
import com.xtree.lottery.databinding.LayoutLotteryDrawBinding;
import com.xtree.lottery.ui.view.viewmodel.LotteryDrawViewModel;
import com.xtree.lottery.utils.DiceCutter;

import java.util.ArrayList;

import me.xtree.mvvmhabit.utils.ConvertUtils;


/**
 * Created by KAKA on 2024/5/1.
 * Describe: 彩票开奖
 */
public class LotteryDrawView extends LinearLayout {

    private LayoutLotteryDrawBinding binding;
    private LotteryDrawViewModel lotteryDrawViewModel;

    public LotteryDrawView(Context context) {
        super(context);
        binding = LayoutLotteryDrawBinding.inflate(LayoutInflater.from(context), this, true);

        initView();
    }

    public LotteryDrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        binding = LayoutLotteryDrawBinding.inflate(LayoutInflater.from(context), this, true);

        initView();
    }

    private void initView() {
        lotteryDrawViewModel = new LotteryDrawViewModel();
        binding.setModel(lotteryDrawViewModel);

        binding.getModel().drawCode.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                ArrayList<String> numbs = binding.getModel().drawCode.get();
                if (numbs != null) {

                    binding.lotteryDrawGroup.removeAllViews();
                    String alias = binding.getModel().alias.get();
                    for (String numb : numbs) {
                        View view = null;
                        if (ExKt.includes(k3Alias, alias)) {
                            view =new ImageView(getContext());
                            LinearLayout.LayoutParams params =  new LinearLayout.LayoutParams( ConvertUtils.dp2px(30), ConvertUtils.dp2px(35));
                            params.rightMargin=ConvertUtils.dp2px(6);
                            view.setLayoutParams(params);
                            Bitmap diceSource = BitmapFactory.decodeResource(binding.getRoot().getResources(), R.mipmap.dice);
                            ((ImageView) view).setImageBitmap(DiceCutter.cutDiceImage(diceSource, Integer.parseInt(numb), 1));
                        } else {
                            view = LayoutInflater.from(getContext()).inflate(R.layout.view_lottery_draw_ball, null);
                            ((TextView) view).setText(numb);
                        }
                        binding.lotteryDrawGroup.addView(view);
                    }
                }
            }
        });
    }

    public void setDrawCode(BonusNumbersResponse.DataDTO bonusNumber) {
        binding.getModel().drawDate.set(bonusNumber.getIssue() + "期：");
        binding.getModel().drawCode.set((ArrayList<String>) bonusNumber.getSplitCode());
    }

    public void setLottery(Lottery lottery) {
        binding.getModel().alias.set(lottery.getAlias());
    }

    /**
     * 设置开奖监听
     */
    public void setOnLotteryDrawListener(OnLotteryDrawListener listener) {
        binding.getModel().setOnLotteryDrawListener(listener);
    }

    /**
     * 刷新上期开奖
     */
    public interface OnLotteryDrawListener {
        void onRefresh(View view);
    }
}
