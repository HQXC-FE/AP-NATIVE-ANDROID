package com.xtree.mine.ui.activity;

import static com.xtree.base.router.RouterActivityPath.Mine.PAGER_APP_UPDATE;

import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;

import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.StringUtils;
import com.xtree.base.vo.AppUpdateVo;
import com.xtree.base.widget.AppUpdateDialog;
import com.xtree.base.widget.LoadingDialog;
import com.xtree.mine.BR;
import com.xtree.mine.R;
import com.xtree.mine.databinding.ActivityAppUpdateBinding;
import com.xtree.mine.ui.viewmodel.ChooseWithdrawViewModel;
import com.xtree.mine.ui.viewmodel.factory.AppViewModelFactory;

import me.xtree.mvvmhabit.base.BaseActivity;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

/**
 * AppUpdateActivity*/
@Route(path = PAGER_APP_UPDATE)
public class AppUpdateActivity extends BaseActivity<ActivityAppUpdateBinding, ChooseWithdrawViewModel> {
    private BasePopupView loadingView = null;
    private AppUpdateVo updateVo;
    private BasePopupView updateView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //修复Android 8.0版本 Activity 设置为透明主题造成的崩溃
        if (Build.VERSION.SDK_INT == 26 && isTranslucentOrFloating()) {
            fixOrientation(this);
        }

        super.onCreate(savedInstanceState);

    }

    @Override
    public int initContentView(Bundle savedInstanceState) {
        return R.layout.activity_app_update;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public void initView() {

    }

    @Override
    public ChooseWithdrawViewModel initViewModel() {
        AppViewModelFactory factory = AppViewModelFactory.getInstance(this.getApplication());
        return new ViewModelProvider(this, factory).get(ChooseWithdrawViewModel.class);
    }

    @Override
    public void initData() {
        LoadingDialog.show(this);
        //请求更新接口
        viewModel.getUpdate();
        //showAwardsRecord 显示活动流水
        //showChoose 显示提款列表

    }

    @Override
    public void initViewObservable() {
//App更新
        viewModel.liveDataUpdate.observe(this, vo -> {
            CfLog.e("******viewModel.liveDataUpdate =" +vo.toString());
            updateVo = vo;
            if (updateVo == null) {
               // ToastUtils.showSuccess(getResources().getString(R.string.txt_update_version));
                this.finish();
                return;
            }
            //存储服务器设置时间间隔
            SPUtils.getInstance().put(SPKeyGlobal.APP_INTERVAL_TIME, updateVo.interval_duration);
            //请求更新服务时间
            SPUtils.getInstance().put(SPKeyGlobal.APP_LAST_CHECK_TIME, System.currentTimeMillis());
            long versionCode = Long.valueOf(StringUtils.getVersionCode(this));
            CfLog.i("versionCode = " + versionCode);
            if (versionCode >= updateVo.version_code) {
                ToastUtils.showSuccess(getResources().getString(R.string.txt_update_version));

                this.finish();
                return;
            }

            //线上版本大于本机版本
            if (updateVo.type == 0) {
                //弱更
                if (versionCode >= vo.version_code_min) {
                    showUpdate(true, updateVo); // 弱更
                } else {
                    showUpdate(false, updateVo); // 强更
                }
            } else if (updateVo.type == 1) {
                //强更
                showUpdate(false, updateVo);
            } else if (updateVo.type == 2) {
                //热更
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }


    /**
     * 显示更新
     *
     * @param isWeakUpdate 是否弱更 true:是弱更 false:强更
     * @param vo           UpdateVo
     */
    private void showUpdate(final boolean isWeakUpdate, final AppUpdateVo vo) {
        if (updateView != null && updateView.isShow()) {
            return;
        }
        AppUpdateDialog dialog = new AppUpdateDialog(this, isWeakUpdate, vo, new AppUpdateDialog.IAppUpdateCallBack() {
            @Override
            public void onUpdateCancel() {
                updateView.dismiss();
            }

            @Override
            public void onUpdateForce() {
            }

          /*  @Override
            public void onDownloadError(String downUrl) {
               *//* showUpdateErrorDialog(isWeakUpdate , downUrl);
                updateView.dismiss();*//*
            }*/
        });

        updateView = new XPopup.Builder(this)
                .dismissOnBackPressed(false)
                .dismissOnTouchOutside(false)
                .asCustom(dialog);
        updateView.show();
    }
}
