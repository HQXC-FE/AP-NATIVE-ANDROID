package com.xtree.base.widget;

import android.content.Context;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.core.BottomPopupView;
import com.xtree.base.R;

import java.io.InputStream;

public class LoadingDialog extends BottomPopupView {
    private static BasePopupView ppw;

    public LoadingDialog(@NonNull Context context) {
        super(context);
    }

    public static BasePopupView show(Context context) {
        if (ppw == null || ppw.isDismiss()) {
            LoadingDialog dialog = new LoadingDialog(context);
            ppw = new XPopup.Builder(context)
                    .dismissOnTouchOutside(false)
                    .dismissOnBackPressed(true)
                    .asCustom(dialog)
                    .show();
        }
        return ppw;
    }

    public static void finish() {
        if (ppw != null && ppw.isShow()) {
            ppw.dismiss();
        }
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        initView();
    }

    private void initView() {
        //ImageView ivwLoading = findViewById(R.id.ivw_loading);
        LottieAnimationView lavIcon = findViewById(R.id.lav_icon);
        ConstraintLayout clLoading = findViewById(R.id.cl_loading);

        clLoading.setOnClickListener(v -> {
        });

        // 设置图像文件夹路径
        lavIcon.setImageAssetDelegate(asset -> {
            // 获取 raw 资源的 ID
            int resId = lavIcon.getContext().getResources().getIdentifier(
                    asset.getFileName().replace(".png", "").replace(".webp", ""),
                    "raw",
                    lavIcon.getContext().getPackageName()
            );

            if (resId == 0) return null; // 资源不存在

            InputStream inputStream = lavIcon.getContext().getResources().openRawResource(resId);
            return BitmapFactory.decodeStream(inputStream); // 解析成 Bitmap
        });


        // 从 assets 文件夹中加载 JSON 文件
        lavIcon.setAnimation("loading.json");
        lavIcon.playAnimation();

        //Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_loading_normal);
        //animation.setRepeatMode(Animation.RESTART);
        //animation.setDuration(1500);
        //ivwLoading.startAnimation(animation);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_loading;
    }
}
