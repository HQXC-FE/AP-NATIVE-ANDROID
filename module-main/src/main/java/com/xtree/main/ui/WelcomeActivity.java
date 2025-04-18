package com.xtree.main.ui;

import static com.xtree.base.utils.EventConstant.EVENT_CHANGE_TO_ACT;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.RoundedCorner;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.utils.TagUtils;
import com.xtree.base.vo.EventVo;
import com.xtree.base.widget.BrowserActivity;
import com.xtree.main.BR ;
import com.xtree.main.R;
import com.xtree.main.databinding.ActivityWelcomeBinding;
import com.xtree.main.ui.viewmodel.SplashViewModel;
import com.xtree.main.BuildConfig ;
import com.xtree.main.ui.viewmodel.factory.AppViewModelFactory;
import com.youth.banner.adapter.BannerAdapter;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.config.BannerConfig;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import me.xtree.mvvmhabit.base.BaseActivity;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

@Route(path = RouterActivityPath.Main.PAGER_WELCOME)

public class WelcomeActivity extends BaseActivity<ActivityWelcomeBinding, SplashViewModel> {
    private ArrayList bannerList = new ArrayList();
    private int MSG_IN_MAIN = 100 ; // 消息类型
    private int DELAY_MILLIS = 100 ; // 延长时间
    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            //loginMain();
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildConfig.DEBUG) {
            ToastUtils.showLong("Debug Model");
        }
        CfLog.e("WelcomeActivityWelcomeActivityWelcomeActivityWelcomeActivityWelcomeActivity");
    }

    @Override
    protected void onStop() {
        super.onStop();
        binding.ivWelcomeBanner.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding.ivWelcomeBanner.destroy();
    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_welcome ;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initView() {
        init();
        initTag();
        initBanner();

        binding.ivLogin.setOnClickListener(v -> {
            loginMain();
        });
    }
    @Override
    public SplashViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(this.getApplication());
        return new ViewModelProvider(this, factory).get(SplashViewModel.class);
    }

    @Override
    public void initViewObservable() {
        viewModel.inMainData.observe(this , vo ->{
           // mHandler.sendEmptyMessageDelayed(MSG_IN_MAIN, DELAY_MILLIS);
        });
    }

    private void init() {
        String api = DomainUtil.getDomainApiString(); // 不能为空,必须正确
        String url = DomainUtil.getDomainUrlString() ;// 如果为空或者不正确,转用API的
        String apiCache = SPUtils.getInstance().getString(SPKeyGlobal.KEY_API_URL, api);
        String h5Cache = SPUtils.getInstance().getString(SPKeyGlobal.KEY_H5_URL, url);

        if (api.startsWith("http://") || api.startsWith("https://")) {
            DomainUtil.setApiUrl(apiCache);
        }

        if (url.startsWith("http://") || url.startsWith("https://")) {
            DomainUtil.setH5Url(h5Cache) ;
        } else {
            DomainUtil.setH5Url(apiCache) ;
        }
    }


    private void initTag() {
        String[] token  = new String[2];
        token[0] = getString(R.string.mixpanel_token) ;
        token[1] = getString(R.string.ms_secret_key) ;
        String channel = getString(R.string.channel_name) ;
        String userId = SPUtils.getInstance().getString(SPKeyGlobal.USER_ID);
        Boolean isTag = getResources().getBoolean(R.bool.is_tag) && !BuildConfig.DEBUG ;
        TagUtils.init(getBaseContext(), token, channel, userId, isTag) ;
        TagUtils.tagDailyEvent(getBaseContext()) ;
    }
    private void initBanner(){
        binding.ivWelcomeBanner.setAdapter(new BannerImageAdapter<IMGBean>(IMGBean.getData()) {
            @Override
            public void onBindView(BannerImageHolder holder, IMGBean data, int position, int size) {
                RoundedCorners roundedCorner = new RoundedCorners(10);
                RequestOptions  options = RequestOptions.bitmapTransform(roundedCorner);
                Glide.with(holder.itemView).load(data.imageRes).apply(options).into(holder.imageView);
            }
        })
                .addBannerLifecycleObserver(this)
                .setIndicator(new CircleIndicator(getBaseContext()));

    }
    private void  loginMain(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }



    public  static class IMGBean {
        public int imageRes ;
        public byte[] imageUrl;

        public  IMGBean(int imageRes){
            this.imageRes = imageRes ;
        }

        public void setImageRes(int imageRes) {
            this.imageRes = imageRes;
        }

        public int getImageRes() {
            return imageRes;
        }

        public static List<IMGBean> getData(){
            ArrayList<IMGBean> list = new ArrayList<>();
            list.add(new IMGBean(R.drawable.welcome_banner_image_1));
            list.add(new IMGBean(R.drawable.welcome_banner_image_2));
            list.add(new IMGBean(R.drawable.welcome_banner_image_3));
            return list ;
        }
    }
}
