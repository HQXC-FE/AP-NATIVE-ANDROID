package com.xtree.live.ui.main.model.banner;

import android.widget.ImageView;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.xtree.base.mvvm.banner.OnBannerViewListener;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.youth.banner.holder.BannerImageHolder;

import java.util.ArrayList;
import java.util.List;

public class LiveBannerModel implements OnBannerViewListener {
    public MutableLiveData<List<BindModel>> datas = new MutableLiveData<>(new ArrayList<>());
    public ObservableField<String> bannerBg = new ObservableField<>();//切换banner的背景


    @Override
    public void onBannerClick(BindModel data, int position) {

    }

    @Override
    public void onPageSelected(int position) {
        BindModel data = datas.getValue().get(position);
        if (data instanceof LiveBannerItemModel) {
            bannerBg.set(((LiveBannerItemModel) data).backImg.get());
        }
    }

    @Override
    public void onBindView(BannerImageHolder holder, BindModel data, int position, int size) {
        if (data instanceof LiveBannerItemModel) {
            LiveBannerItemModel model = (LiveBannerItemModel) data;
            holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(holder.itemView.getContext()).load(model.foreImg.get()).into(holder.imageView);
        }
    }
}
