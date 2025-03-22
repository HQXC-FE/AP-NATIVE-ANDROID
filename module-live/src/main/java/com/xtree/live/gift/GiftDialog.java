package com.xtree.live.gift;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.vo.ProfileVo;
import com.xtree.live.LiveConfig;
import com.xtree.live.R;
import com.xtree.live.chat.Subscription;
import com.xtree.live.inter.GiftViewMarginBottomListener;
import com.xtree.live.model.GiftBean;
import com.xtree.live.uitl.WordUtil;

import java.util.ArrayList;
import java.util.List;

import me.xtree.mvvmhabit.utils.SPUtils;

public class GiftDialog extends Dialog {

    private final Context mContext;
    private GiftAdapter mAdapter;
    private GiftBean mSelectedBean;
    private final @NonNull SendGiftListener mGiftListener;
    private TextView mDiamond;
    private CheckBox mBlockGiftShown;

    private ViewGroup mGiftDialogContainer;

    private GiftViewMarginBottomListener mHeightListener;

    private int restoreBottomMargin = 0;
    public GiftDialog(@NonNull Context context, @NonNull SendGiftListener listener, @Nullable GiftViewMarginBottomListener heightListener) {
        super(context, R.style.giftDialog);
        mContext = context;
        mGiftListener = listener;
        mHeightListener = heightListener;
        subscription = new Subscription();
        initDialog();
    }

    private void initDialog() {
        setContentView(R.layout.dialog_gift);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        getWindow().setWindowAnimations(R.style.bottomToTopAnim);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        getWindow().setAttributes(params);
        RecyclerView mGiftRv = findViewById(R.id.rvGift);
        mAdapter = new GiftAdapter(new ArrayList<>(), bean -> mSelectedBean = bean);
        mGiftRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mGiftRv.setAdapter(mAdapter);
        mBlockGiftShown = findViewById(R.id.blockLiveGiftShown);
        mGiftDialogContainer = findViewById(R.id.giftDialogContainer);
        mBlockGiftShown.setOnCheckedChangeListener((buttonView, isChecked) -> {
            LiveConfig.blockGiftShown(isChecked);
        });
        mDiamond = findViewById(R.id.diamondAmount);
        mBlockGiftShown = findViewById(R.id.blockLiveGiftShown);
        mBlockGiftShown.setChecked(LiveConfig.isBlockGiftShown());
        findViewById(R.id.closeGiftDialog).setOnClickListener(v -> dismiss());
        findViewById(R.id.sendGift).setOnClickListener(v -> {
            if (LiveConfig.isVisitor()) {
                dismiss();
                ARouter.getInstance().build(RouterActivityPath.Mine.PAGER_LOGIN_REGISTER).navigation();
                return;
            }
            if (mSelectedBean != null) {
                mGiftListener.onSendGift(mSelectedBean, balances -> {
                    mDiamond.setText(WordUtil.getString(R.string.diamond_account, balances));
                });
            }
        });
        updatedStatus();
    }

    @Override
    public void show() {
        restoreBottomMargin = mHeightListener.restoreBottomMargin();
        super.show();
    }

    public void updatedStatus() {
        if (LiveConfig.isVisitor()) {
            mDiamond.setText("--");
        } else {
//            UserInfo userInfo = AppConfig.getUserBean();
//            if (userInfo == null) {
//                mDiamond.setText("--");
//                return;
//            }

            String json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE);
            ProfileVo vo = new Gson().fromJson(json, ProfileVo.class);
            if (vo == null) {
                mDiamond.setText("--");
                return;
            }

            String balances = vo.availablebalance;
            if (!TextUtils.isEmpty(balances) && balances.indexOf(".") > 0) {
                balances = balances.replaceAll("0+?$", "");
                balances = balances.replaceAll("[.]$", "");
            }
            mDiamond.setText(WordUtil.getString(R.string.diamond_account, balances));
        }
    }

    public GiftAdapter getAdapter() {
        return mAdapter;
    }

    public List<GiftBean> getData() {
        List<GiftBean> list = new ArrayList<>();
        if (mAdapter != null) {
            list.addAll(mAdapter.getData());
        }
        return list;
    }

    public void setNewData(List<GiftBean> list) {
        if (list != null && !list.isEmpty() && mAdapter != null) {
            mAdapter.setList(list);
        }
    }
    Subscription subscription;
    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        subscription.onUnSubscribe();
        if(mHeightListener != null)mHeightListener.onMarginBottom(restoreBottomMargin);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        mBlockGiftShown.setChecked(LiveConfig.isBlockGiftShown());
        Window window = getWindow();
        if(window != null){
//            BarUtils.setNavBarLightMode(window, true);
            OnApplyWindowInsetsListener onApplyWindowInsetsListener = new OnApplyWindowInsetsListener() {
                @NonNull
                @Override
                public WindowInsetsCompat onApplyWindowInsets(@NonNull View root, @NonNull WindowInsetsCompat windowInsets) {
                    Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
                    mGiftDialogContainer.setPadding(0, 0, 0, insets.bottom);
                    mGiftDialogContainer.post(()->{
                        int height = mGiftDialogContainer.getHeight();
                        if(mHeightListener != null)mHeightListener.onMarginBottom(height);
                    });
                    return windowInsets;
                }
            };
            ViewCompat.setOnApplyWindowInsetsListener(window.getDecorView(), onApplyWindowInsetsListener);
            requestApplyWindowInsets(window.getDecorView());
        }
    }


    void requestApplyWindowInsets(View view) {
        if (view.isAttachedToWindow()) {
            // We're already attached, just request as normal
            view.requestApplyInsets();
        } else {
            // We're not attached to the hierarchy, add a listener to
            // request when we are
            view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(@NonNull View v) {
                    v.removeOnAttachStateChangeListener(this);
                    v.requestApplyInsets();
                }

                @Override
                public void onViewDetachedFromWindow(@NonNull View v) {

                }
            });
        }
    }

    public interface SendGiftListener {
        void onSendGift(GiftBean bean, SendGiftResult callback);
    }

    public interface SendGiftResult{
        void result(int balances);
    }
}
