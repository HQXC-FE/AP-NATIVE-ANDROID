package com.xtree.live.ui.main.model.banner;

import android.text.TextUtils;
import android.widget.ImageView;

import androidx.databinding.ObservableField;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.xtree.base.mvvm.banner.OnBannerViewListener;
import com.xtree.base.mvvm.recyclerview.BindModel;
import com.xtree.base.utils.AppUtil;
import com.xtree.live.data.source.response.BannerParamsResponse;
import com.youth.banner.holder.BannerImageHolder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.sentry.Sentry;

public class LiveBannerModel implements OnBannerViewListener {
    public MutableLiveData<List<BindModel>> datas = new MutableLiveData<>(new ArrayList<>());
    public ObservableField<String> bannerBg = new ObservableField<>();//切换banner的背景
    public WeakReference<FragmentActivity> mActivity = null;


    @Override
    public void onBannerClick(BindModel data, int position) {
        if (data instanceof LiveBannerItemModel) {
            LiveBannerItemModel model = (LiveBannerItemModel) data;
            try {
                if (!TextUtils.isEmpty(model.androidUrl.get())) {
                    AppUtil.goBrowser(mActivity.get(), model.androidUrl.get());
                } else if (TextUtils.isEmpty(model.params.get())) {//内部跳转逻辑
                    //使用params target_type
                    //1 ，值为live_room 是否包含live_id 构造直播间详情页URL（live-room/{live_id}
                    //2 ，值为host_profile 是否包含host_id 构造主播主页URL（host-profile/{host_id}
                    //3 ，值为campaign_page 是否包含campaign_id 构造活动页URL（campaign-page/{campaign_id}
                    //4 ，值为replay_page 是否包含replay_id 构造直播回放URL(replay-page/{replay-id}

                    BannerParamsResponse paramsResponse = new Gson().fromJson(model.params.get(), BannerParamsResponse.class);
                    if ("live_room".equals(paramsResponse.getTarget_type())) {

                    } else if ("host_profile".equals(paramsResponse.getTarget_type())) {

                    } else if ("campaign_page".equals(paramsResponse.getTarget_type())) {

                    } else if ("replay_page".equals(paramsResponse.getTarget_type())) {

                    }

                }
            } catch (JsonSyntaxException e) {
                Sentry.captureException(e);
            }
        }
    }

    @Override
    public void onPageSelected(int position) {
      /*  BindModel data = datas.getValue().get(position);
        if (data instanceof LiveBannerItemModel) {
            bannerBg.set(((LiveBannerItemModel) data).backImg.get());
        }*/
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
