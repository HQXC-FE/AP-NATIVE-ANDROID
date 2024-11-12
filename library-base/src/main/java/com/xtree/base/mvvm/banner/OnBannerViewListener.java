package com.xtree.base.mvvm.banner;

import com.xtree.base.mvvm.recyclerview.BindModel;
import com.youth.banner.holder.BannerImageHolder;

public interface OnBannerViewListener {
    void onBindView(BannerImageHolder holder, BindModel data, int position, int size);

    void onBannerClick(BindModel data, int position);

    void onPageSelected(int position);
}
