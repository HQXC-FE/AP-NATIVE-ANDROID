package com.xtree.recharge.ui.viewmodel;


import androidx.annotation.NonNull;

import com.xtree.base.base.ItemViewModel;
import com.xtree.base.binding.command.BindingAction;
import com.xtree.base.binding.command.BindingCommand;

/**
 * Created by goldze on 2018/7/18.
 */

public class ViewPagerItemViewModel extends ItemViewModel<RechargeViewModel> {
    public String text;

    public ViewPagerItemViewModel(@NonNull RechargeViewModel viewModel, String text) {
        super(viewModel);
        this.text = text;
    }

    public BindingCommand onClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            //点击之后将逻辑转到adapter中处理
            viewModel.itemClickEvent.setValue(text);
        }
    });
}
