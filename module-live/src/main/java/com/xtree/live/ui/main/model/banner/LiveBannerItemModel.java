package com.xtree.live.ui.main.model.banner;

import androidx.databinding.ObservableField;

import com.xtree.base.mvvm.recyclerview.BindModel;

public class LiveBannerItemModel extends BindModel {

    public ObservableField<String> img = new ObservableField<>();
    public ObservableField<String> foreImg = new ObservableField<>();
    public ObservableField<String> backImg = new ObservableField<>();
    public ObservableField<String> androidUrl = new ObservableField<>();
    public ObservableField<String> params = new ObservableField<>();

}
