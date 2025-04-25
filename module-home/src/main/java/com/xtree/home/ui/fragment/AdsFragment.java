package com.xtree.home.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.xtree.base.global.Constant;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.widget.X5WebView;
import com.xtree.home.BR;
import com.xtree.home.R;
import com.xtree.home.databinding.FragmentAdsBinding;
import com.xtree.home.ui.viewmodel.HomeViewModel;
import com.xtree.home.ui.viewmodel.factory.AppViewModelFactory;

import me.xtree.mvvmhabit.base.BaseFragment;

@Route(path = RouterFragmentPath.Home.AD)
public class AdsFragment extends BaseFragment<FragmentAdsBinding, HomeViewModel> {

    private X5WebView x5WebView;
    int sslErrorCount = 0;

    @Override
    public void initView() {
        initWebView();
    }

    @Override
    protected void initImmersionBar() {
    }

    /**
     * 使用hide和show后，可见不可见切换时，不再执行fragment生命周期方法，
     * 需要刷新时，使用onHiddenChanged代替
     */
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {   // 显示

        } else {  // 第一次可见，不会执行到这里，只会执行onResume
            //网络数据刷新

        }
    }

    public void initWebView() {

        binding.wvAdsMain.setFitsSystemWindows(true);
        x5WebView = X5WebView.getInstance(getContext());
        ViewGroup parent = (ViewGroup) x5WebView.getWebView().getParent();
        if (parent != null) {
            parent.removeView(x5WebView.getWebView()); // 確保不重複加入
        }

        setWebView(x5WebView.getWebView());

        x5WebView.setMode(X5WebView.WebViewMode.DEFAULT);
        x5WebView.setWebViewClient(new CustomWebViewClient());
        x5WebView.bindToContainer(binding.wvAdsMain);

        x5WebView.loadUrl(DomainUtil.getH5Domain2() + Constant.URL_APP_CENTER);
    }

    @Override
    public int initContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return R.layout.fragment_ads;
    }

    @Override
    public int initVariableId() {
        return BR.viewModel;
    }

    @Override
    public HomeViewModel initViewModel() {
        //使用自定义的ViewModelFactory来创建ViewModel，如果不重写该方法，则默认会调用LoginViewModel(@NonNull Application application)构造方法
        AppViewModelFactory factory = AppViewModelFactory.getInstance(getActivity().getApplication());
        return new ViewModelProvider(this, factory).get(HomeViewModel.class);
    }

    private void hideLoading() {
        binding.ivwAdsLoading.setVisibility(View.GONE);
        binding.ivwAdsLoading.clearAnimation();
    }

    private void tipSsl(WebView view, SslErrorHandler handler) {
        Activity activity = (Activity) view.getContext();
        if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
            return; // 避免 BadTokenException
        }
        activity.runOnUiThread(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage(com.xtree.base.R.string.ssl_failed_will_u_continue); // SSL认证失败，是否继续访问？
            builder.setPositiveButton(com.xtree.base.R.string.ok, (dialog, which) -> handler.proceed()); // 接受https所有网站的证书

            builder.setNegativeButton(com.xtree.base.R.string.cancel, (dialog, which) -> handler.cancel());

            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    private void setWebView(WebView webView) {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLoadsImagesAutomatically(true);
    }

    public class CustomWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            CfLog.d("onPageStarted url:  " + url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            CfLog.d("onPageFinished url: " + url);
            hideLoading();
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            //handler.proceed();
            CfLog.d("onPageFinished url: " + error);
            hideLoading();
            if (sslErrorCount < 4) {
                sslErrorCount++;
                tipSsl(view, handler);
            } else {
                handler.proceed();
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            CfLog.e("errorCode: " + errorCode + ", description: " + description + ", failingUrl: " + failingUrl);
            hideLoading();
            Toast.makeText(getContext(), com.xtree.base.R.string.network_failed, Toast.LENGTH_SHORT).show();
        }
    }
}
