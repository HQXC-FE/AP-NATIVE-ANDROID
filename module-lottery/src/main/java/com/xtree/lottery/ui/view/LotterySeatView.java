package com.xtree.lottery.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.xtree.lottery.R;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by KAKA on 2024/5/9.
 * Describe: 彩票位数选择
 */
public class LotterySeatView extends LinearLayout {

    public interface onSeatListener{
        void onSeat(Set<String> seats);
    }

    private onSeatListener onSeatListener;
    private String[] seats = {"万位", "千位", "百位", "十位", "个位"};
    private Set<String> checkedSeats = new HashSet<>();
    //保留默认设置数量，用于认定有效设置数
    private int defultSeatCount = 0;

    public LotterySeatView(Context context) {
        super(context);
        initView();
    }

    public LotterySeatView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        setOrientation(LinearLayout.HORIZONTAL);
        setWeightSum(seats.length); // 设置weightSum为添加的Button数量

        for (int i = 0; i < seats.length; i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_lottery_seat, null);
            CheckBox checkBox = (CheckBox) view.findViewById(R.id.lottery_seat_checkbox);
            checkBox.setText(seats[i]);
            checkBox.setTag(i + 1);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        checkedSeats.add(String.valueOf(checkBox.getTag()));
                    } else {
                        checkedSeats.remove(String.valueOf(checkBox.getTag()));
                    }

                    if (onSeatListener != null) {
                        onSeatListener.onSeat(checkedSeats);
                    }
                }
            });

            LayoutParams params = new LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.MATCH_PARENT
            );
            params.weight = 1; // 设置layout_weight为1，使空间均分
            view.setLayoutParams(params);
            addView(view);
        }
    }

    /**
     * 获取选中的位数
     */
    public Set<String> getSeats() {
        return checkedSeats;
    }

    /**
     * 获取选中的位数
     */
    public String getFormatSeats() {
        if (checkedSeats.size() < defultSeatCount) {
            return null;
        }
        return String.join(",", checkedSeats);
    }

    /**
     * 位数选中监听
     */
    public void setOnSeatListener(LotterySeatView.onSeatListener onSeatListener) {
        this.onSeatListener = onSeatListener;
    }

    /**
     * 设置位数按钮默认选中状态
     */
    public void setSeatChecked(char[] seatChecked) {
        if (seatChecked == null) {
            return;
        }

        int count = 0;
        for (int i = 0; i < seatChecked.length; i++) {
            View view = getChildAt(i);
            CheckBox checkBox = (CheckBox) view.findViewById(R.id.lottery_seat_checkbox);
            if (seatChecked[i] == '1') {
                checkBox.setChecked(true);
                count++;
            } else {
                checkBox.setChecked(false);
            }
        }

        defultSeatCount = count;
    }
}
