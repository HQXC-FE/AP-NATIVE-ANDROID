package com.xtree.bet.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xtree.bet.R;
import com.xtree.bet.bean.ui.BetConfirmOption;
import com.xtree.bet.bean.ui.BetConfirmOptionFb;
import com.xtree.bet.bean.ui.Match;
import com.xtree.bet.bean.ui.Option;
import com.xtree.bet.bean.ui.OptionList;
import com.xtree.bet.bean.ui.PlayType;
import com.xtree.bet.contract.BetContract;
import com.xtree.bet.databinding.BtFbDetailOptionListOptionItemBinding;
import com.xtree.bet.databinding.BtFbListItemPlayTypeItemOptionBinding;
import com.xtree.bet.manager.BtCarManager;
import com.xtree.bet.ui.fragment.BtCarDialogFragment;
import com.xtree.bet.weight.DiscolourTextView;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import me.xtree.mvvmhabit.base.BaseActivity;
import me.xtree.mvvmhabit.base.BaseFragment;
import me.xtree.mvvmhabit.bus.RxBus;
import me.xtree.mvvmhabit.utils.ConvertUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

public class OptionAdapter extends BaseAdapter<Option> {
    private Match match;
    private PlayType playType;
    private BtFbListItemPlayTypeItemOptionBinding binding;
    public OptionAdapter(Context context, Match match, PlayType playType, List<Option> datas) {
        super(context, datas);
        this.playType = playType;
        this.match = match;
    }

    @Override
    public int layoutId() {
        return R.layout.bt_fb_list_item_play_type_item_option;
    }

    @Override
    protected void convert(ViewHolder holder, Option option, int position) {
        binding = BtFbListItemPlayTypeItemOptionBinding.bind(holder.itemView);
        TextView uavailableTextView = binding.tvOptionUnable;
        TextView nameTextView = binding.tvOptionName;
        DiscolourTextView oddTextView = binding.tvOptionOdd;
        LinearLayout optionView = binding.getRoot();

        if (option == null || option.getOdd() < 0) {
            uavailableTextView.setVisibility(View.VISIBLE);
            oddTextView.setVisibility(View.GONE);
            nameTextView.setVisibility(View.GONE);
            uavailableTextView.setText("-");
            uavailableTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            optionView.setOnClickListener(view1 -> {
            });
        } else {
            OptionList optionList = playType.getOptionLists().get(0);
            if (!optionList.isOpen()) {
                uavailableTextView.setVisibility(View.VISIBLE);
                oddTextView.setVisibility(View.GONE);
                nameTextView.setVisibility(View.GONE);
                uavailableTextView.setText("");
                uavailableTextView.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.mipmap.bt_icon_option_locked), null, null, null);
                optionView.setOnClickListener(view1 -> {
                });
            } else {
                uavailableTextView.setVisibility(View.GONE);
                oddTextView.setVisibility(View.VISIBLE);
                nameTextView.setVisibility(View.VISIBLE);
                nameTextView.setText(option.getSortName());
                oddTextView.setText(String.valueOf(option.getOdd()));
                BetConfirmOption betConfirmOption = (BetConfirmOption) optionView.getTag();
                if (betConfirmOption == null) {
                    betConfirmOption = new BetConfirmOptionFb(match, playType, optionList, option);
                } else {
                    if(option.getOdd() > betConfirmOption.getOption().getOdd()){
                        oddTextView.startUp();
                    } else if (option.getOdd() < betConfirmOption.getOption().getOdd()) {
                        oddTextView.startDown();
                    }
                    betConfirmOption.setData(match, playType, optionList, option);
                }
                optionView.setTag(betConfirmOption);

                if (BtCarManager.isCg()) {
                    boolean has = BtCarManager.has(betConfirmOption);
                    optionView.setSelected(has);
                    oddTextView.setSelected(has);
                    nameTextView.setSelected(has);
                } else {
                    if (optionView.isSelected()) {
                        optionView.setSelected(false);
                        oddTextView.setSelected(false);
                        nameTextView.setSelected(false);
                    }
                }
                setOptionClickListener(optionView, optionList, option);
            }
        }
    }

    private void setOptionClickListener(LinearLayout llOption, OptionList optionList, Option option) {
        llOption.setOnClickListener(view1 -> {
            if (!optionList.isOpen() || option == null) {
                return;
            }
            BetConfirmOption betConfirmOption = (BetConfirmOption) view1.getTag();
            if (BtCarManager.isCg()) { // 如果是串关，往购物车增加
                if (!optionList.isAllowCrossover() && !BtCarManager.has(betConfirmOption)) {
                    ToastUtils.showShort(mContext.getResources().getText(R.string.bt_bt_is_not_allow_crossover));
                    return;
                }
                if (!BtCarManager.has(betConfirmOption)) {
                    BtCarManager.addBtCar(betConfirmOption);
                } else {
                    BtCarManager.removeBtCar(betConfirmOption);
                }
                option.setSelected(BtCarManager.has(betConfirmOption));
            } else {
                BtCarDialogFragment btCarDialogFragment = new BtCarDialogFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable(BtCarDialogFragment.KEY_BT_OPTION, (BetConfirmOption) view1.getTag());
                btCarDialogFragment.setArguments(bundle);
                if (mContext instanceof BaseActivity) {
                    btCarDialogFragment.show(((BaseActivity) mContext).getSupportFragmentManager(), "btCarDialogFragment");
                }
            }
        });
    }
}
