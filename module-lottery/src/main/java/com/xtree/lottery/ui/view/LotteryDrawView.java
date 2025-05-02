package com.xtree.lottery.ui.view;

import static com.xtree.lottery.rule.betting.Matchers.jssmAlias;
import static com.xtree.lottery.rule.betting.Matchers.k3Alias;
import static com.xtree.lottery.rule.betting.Matchers.lhcAlias;
import static com.xtree.lottery.rule.betting.Matchers.pk10Alias;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.Observable;

import com.xtree.base.mvvm.ExKt;
import com.xtree.base.utils.CfLog;
import com.xtree.lottery.R;
import com.xtree.lottery.data.config.Lottery;
import com.xtree.lottery.data.source.vo.RecentLotteryVo;
import com.xtree.lottery.databinding.LayoutLotteryDrawBinding;
import com.xtree.lottery.ui.view.viewmodel.LotteryDrawViewModel;
import com.xtree.lottery.utils.DiceCutter;
import com.xtree.lottery.utils.LhcHelper;
import com.xtree.lottery.utils.PK10Helper;

import java.util.List;
import java.util.stream.Collectors;

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
                List<String> numbs = binding.getModel().drawCode.get();
                if (numbs != null) {

                    binding.lotteryDrawGroup.removeAllViews();
                    String alias = binding.getModel().alias.get();
                    for (String numb : numbs) {
                        TextView view = new TextView(getContext());
                        if (!TextUtils.isEmpty(alias) && ExKt.includes(k3Alias, alias)) {
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ConvertUtils.dp2px(30), ConvertUtils.dp2px(35));
                            params.rightMargin = ConvertUtils.dp2px(6);
                            view.setLayoutParams(params);
                            view.setBackground(new BitmapDrawable(view.getResources(), DiceCutter.diceResult(binding.getRoot().getResources(), Integer.parseInt(numb))));
                        } else if (!TextUtils.isEmpty(alias) && (ExKt.includes(pk10Alias, alias) || ExKt.includes(jssmAlias, alias))) {
                            GradientDrawable bitmapDrawable1 = PK10Helper.INSTANCE.getBallBackground(alias, numb);
                            LayoutParams params = new LinearLayout.LayoutParams(ConvertUtils.dp2px(18f), ConvertUtils.dp2px(30f));
                            params.rightMargin = ConvertUtils.dp2px(1.5f);
                            view.setLayoutParams(params);
                            view.setBackground(bitmapDrawable1);
                            view.setTextSize(14f);
                            view.setGravity(Gravity.CENTER);
                            view.setTypeface(null, Typeface.NORMAL);
                            view.setText(numb);
                        } else if (!TextUtils.isEmpty(alias) && ExKt.includes(lhcAlias, alias)) {
                            LayoutParams params = new LinearLayout.LayoutParams(ConvertUtils.dp2px(25f), ConvertUtils.dp2px(25f));
                            params.rightMargin = ConvertUtils.dp2px(2f);
                            view.setLayoutParams(params);
                            view.setBackgroundResource(LhcHelper.INSTANCE.getNumberColor(numb));
                            view.setGravity(Gravity.CENTER);
                            view.setTextSize(14f);
                            if (numb.equals("——")) {
                                view.setTextColor(ContextCompat.getColor(getContext(), R.color.lt_color_text18));
                            } else {
                                view.setTypeface(null, Typeface.NORMAL);
                                view.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                            }
                            view.setText(numb);
                        } else {
                            view = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.view_lottery_draw_ball, null);
                            LayoutParams params = new LinearLayout.LayoutParams(ConvertUtils.dp2px(28f), ConvertUtils.dp2px(28f));
                            params.rightMargin = ConvertUtils.dp2px(6f);
                            view.setLayoutParams(params);
                            view.setText(numb);
                        }
                        binding.lotteryDrawGroup.addView(view);
                    }
                }
            }
        });
    }

    public void setDrawCode(RecentLotteryVo bonusNumber) {
        binding.getModel().drawDate.set(bonusNumber.getIssue());
        String alias = binding.getModel().alias.get();
        if (!TextUtils.isEmpty(alias) && ExKt.includes(lhcAlias, alias)) {
            binding.getModel().drawCode.set(LhcHelper.INSTANCE.makeLhcIssue2(bonusNumber.getCode()));
        } else {
            binding.getModel().drawCode.set(bonusNumber.getSplit_code());
        }
    }

    public void setDrawCode(String number) {
        try {
            binding.getModel().drawCode.set(number.chars()
                    .mapToObj(c -> String.valueOf((char) c))
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            CfLog.e(e.getMessage());
        }

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
     * 刷新上期开奖、模拟开奖
     */
    public static abstract class OnLotteryDrawListener {
        public abstract void onRefresh(View view);

        public void onSimulate(View view) {

        }
    }
}
