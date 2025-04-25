package com.xtree.base.widget;

import static com.xtree.base.utils.EventConstant.EVENT_CHANGE_URL_FANZHA_FINSH;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.tencent.smtt.export.external.interfaces.ConsoleMessage;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.xtree.base.BuildConfig;
import com.xtree.base.R;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.DomainUtil;
import com.xtree.base.utils.FightFanZhaUtils;
import com.xtree.base.utils.TagUtils;
import com.xtree.base.vo.EventVo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

public class BrowserDialogX5 extends BottomPopupView {
    private Context mContext;
    private TextView tvwTitle;
    private View vTitle;
    private View clTitle;
    private ImageView ivwClose;
    private ViewGroup mWebView;
    private ImageView ivwLoading;
    private ImageView ivwLaunch;
    private boolean isFirstLoad = true; // 是否头一次打开当前网页,加载cookie时用
    private String token;
    private X5WebView x5WebView;

    protected String title;
    protected String url;
    protected int maxHeight = 85; // 最大高度百分比 10-100
    protected boolean isContainTitle = false; // 网页自身是否包含标题(少数情况下会包含)
    protected boolean isActivity = false; // 是否来自活动页面
    protected boolean is3rdLink = false; // 是否跳转到三方链接(如果是,就不用带header和cookie了)
    protected boolean isHideTitle = false; // 是否隐藏标题栏

    boolean isFirstOpenBrowser = true; // 是否第一次打开webView组件(解决第一次打开webView时传递header/cookie/token失效)
    ValueCallback<Uri> mUploadCallbackBelow;
    ValueCallback<Uri[]> mUploadCallbackAboveL;

    public BrowserDialogX5(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    public BrowserDialogX5(@NonNull Context context, String title, String url) {
        super(context);
        mContext = context;
        this.title = title;
        this.url = url;
    }

    public BrowserDialogX5(@NonNull Context context, String title, String url, boolean isContainTitle, boolean isActivity) {
        super(context);
        mContext = context;
        this.title = title;
        this.url = url;
        this.isContainTitle = isContainTitle;
        this.isActivity = isActivity;
    }

    public static BrowserDialogX5 newInstance(@NonNull Context context, String title, String url) {
        BrowserDialogX5 dialog = new BrowserDialogX5(context, title, url);
        return dialog;
    }

    public static BrowserDialogX5 newInstance(@NonNull Context context, String url) {
        BrowserDialogX5 dialog = new BrowserDialogX5(context, "", url);
        dialog.isHideTitle = true;
        return dialog;
    }

    public BrowserDialogX5 setContainTitle(boolean isContainTitle) {
        this.isContainTitle = isContainTitle;
        return this;
    }

    public BrowserDialogX5 set3rdLink(boolean is3rdLink) {
        this.is3rdLink = is3rdLink;
        return this;
    }

    public BrowserDialogX5 setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
        return this;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        token = SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN);
        isFirstOpenBrowser = SPUtils.getInstance().getBoolean(SPKeyGlobal.IS_FIRST_OPEN_BROWSER, true);
        x5WebView = X5WebView.getInstance(mContext);

        initView();

        FightFanZhaUtils.init();

        if (isContainTitle) {
            vTitle.setVisibility(View.GONE);
        }

        if (isHideTitle) {
            vTitle.setVisibility(View.GONE);
            clTitle.setVisibility(View.GONE);
        }

        tvwTitle.setText(title);
        ivwLoading.setVisibility(View.GONE);

        try {
            initX5WebView(url);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        ivwClose.setOnClickListener(v -> dismiss());

        // 测试
        if (FightFanZhaUtils.isOpenTest && BuildConfig.DEBUG) {
            FightFanZhaUtils.mockJumpFanZha(X5WebView.getInstance(mContext).getWebView(), url);
        }
    }

    private void initView() {
        tvwTitle = findViewById(R.id.tvw_title);
        vTitle = findViewById(R.id.v_title);
        clTitle = findViewById(R.id.cl_title);
        ivwClose = findViewById(R.id.ivw_close);

        mWebView = findViewById(R.id.wv_main);
        ivwLoading = findViewById(R.id.ivw_loading);
        ivwLaunch = findViewById(R.id.ivw_launch);

        mWebView.setFitsSystemWindows(true);
        LoadingDialog.show2(mContext);
    }

    private void initX5WebView(String url) throws UnknownHostException {
        // debug模式
        if (BuildConfig.DEBUG) {
            x5WebView.setDebug();
        }
        // 設定模式
        x5WebView.setMode(X5WebView.WebViewMode.DEFAULT);
        // 設定 WebViewClient
        x5WebView.setWebViewClient(new CustomWebViewClient());
        // 設定 WebChromeClient
        x5WebView.setWebChromeClient(new CustomWebChromeClient());
        // 設定 JS Bridge（注意這裡 "android" 是 JS 調用的名稱）
        x5WebView.addJavascriptInterface(new WebAppInterface(mContext, ivwClose, getCallBack()));
        // 將 WebView 加入容器
        x5WebView.bindToContainer(mWebView);
        // 加载
        x5WebView.loadUrl(url);
    }

    public WebAppInterface.ICallBack getCallBack() {
        return new WebAppInterface.ICallBack() {
            @Override
            public void close() {
                ivwClose.post(() -> dismiss());
            }

            @Override
            public void goBack() {
                ivwClose.post(() -> dismiss());
            }

            @Override
            public void callBack(String type, Object obj) {
                CfLog.i("type: " + type);
                if (TextUtils.equals(type, "captchaVerifySucceed")) {
                    TagUtils.tagEvent(getContext(), "captchaVerifyOK");
                    ARouter.getInstance().build(RouterActivityPath.Main.PAGER_SPLASH)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            .navigation();
                } else if (TextUtils.equals(type, "captchaVerifyFail")) {
                    TagUtils.tagEvent(getContext(), "captchaVerifyFail");
                    CfLog.e("type error... type: " + type);
                    doFail(type, obj);
                } else {
                    TagUtils.tagEvent(getContext(), "captchaVerifyError");
                    CfLog.e("type error... type: " + type);
                    doFail(type, obj);
                }
            }
        };
    }

    private void doFail(String type, Object obj) {
        CfLog.e("type: " + type + ", obj: " + obj);
        ToastUtils.showShort(getResources().getString(R.string.txt_vf_failed));
        CfLog.i("reload... ");
        x5WebView.reload();
    }

    private void hideLoading() {
        ivwLoading.setVisibility(View.GONE);
        ivwLoading.clearAnimation();
        ivwLaunch.setVisibility(View.GONE);
    }

    /**
     * 图片选择
     */
    private void gotoSelectMedia() {
        PictureSelector.create(getContext())
                .openGallery(SelectMimeType.ofImage())
                .setMaxSelectNum(1)
                .setImageEngine(GlideEngine.createGlideEngine())
                .setCompressEngine(ImageFileCompressEngine.create())
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(ArrayList<LocalMedia> list) {

                        ArrayList<Uri> results = new ArrayList<>();
                        for (LocalMedia t : list) {
                            Uri mUri = null;
                            if (t.getWatermarkPath() != null && !t.getWatermarkPath().isEmpty()) {
                                // 水印 /storage/emulated/0/Android/data/com.xxx.xxx/files/Mark/Mark_20220609xxx.jpg
                                mUri = Uri.fromFile(new File(t.getWatermarkPath()));
                            } else if (t.isCompressed()) {
                                // 压缩后的 /storage/emulated/0/Android/data/com.xxx.xxx/cache/luban_disk_cache/CMP_20220609xxx.jpg
                                mUri = Uri.fromFile(new File(t.getCompressPath()));
                                //mUri = Uri.fromFile(new File(t.getPath())); // content://media/external/images/media/29003
                            } else {
                                // 实际路径,压缩前的 /storage/emulated/0/Pictures/Screenshots/Screenshot_20220609_xx.jpg
                                mUri = Uri.fromFile(new File(t.getRealPath()));
                            }
                            CfLog.i(mUri.toString());
                            results.add(mUri);
                        }
                        mUploadCallbackAboveL.onReceiveValue(results.toArray(new Uri[results.size()]));
                    }

                    @Override
                    public void onCancel() {
                        mUploadCallbackAboveL.onReceiveValue(null);
                    }
                });
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_browser;
    }

    @Override
    protected int getMaxHeight() {
        if (maxHeight < 5 || maxHeight > 100) {
            maxHeight = 85;
        }
        return (XPopupUtils.getScreenHeight(getContext()) * maxHeight / 100);
    }

    private void setWebCookie() {
        CfLog.i("******");
        if (is3rdLink) {
            CfLog.d("not need cookie.");
        } else {
            if (!TextUtils.isEmpty(token)) {
                setCookieInside();
            }
        }
    }

    private void setCookieInside() {
        String sessid = SPUtils.getInstance().getString(SPKeyGlobal.USER_SHARE_SESSID);
        String json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE);

        Gson gson = new Gson();
        Map<String, Object> mProfileVo = gson.fromJson(json, new TypeToken<Map<String, Object>>() {
        }.getType());

        long expires = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1);

        String userProfile = buildJsonData(gson, mProfileVo, expires);
        String auth = buildJsonData(gson, token, expires);

        StringBuilder js = new StringBuilder();
        js.append("(function() {\n")
                .append("  const d = new Date();\n");

        if (isActivity) {
            js.append("  var style = document.createElement('style');\n")
                    .append("  style.type = 'text/css';\n")
                    .append("  style.id = 'iOS_inject';\n")
                    .append("  style.innerHTML = '.popup-wrapper > .title{ visibility: hidden !important} ")
                    .append(".popup-wrapper{transform: translate3d(0, 0, 0) !important; animation: none !important}';\n")
                    .append("  document.head.appendChild(style);\n")
                    .append("  document.querySelector('#iOS_inject').innerHTML = '.rndx{display: none !important;}';\n");
        }

        js.append("  d.setTime(d.getTime() + (24*60*60*1000));\n")
                .append("  let expires = \"expires=\" + d.toUTCString();\n")
                .append("  document.cookie = \"auth=").append(token).append(";\" + expires + \";path=/\";\n")
                .append("  document.cookie = \"_sessionHandler=").append(sessid).append(";\" + expires + \";path=/\";\n")
                .append("  localStorage.setItem('USER-PROFILE', '").append(escapeForJs(userProfile)).append("');\n")
                .append("  localStorage.setItem('AUTH', '").append(escapeForJs(auth)).append("');\n")
                .append("})();\n");

        CfLog.i(js.toString().replace("\n", "\t"));
        X5WebView.getInstance(mContext).getWebView().evaluateJavascript(js.toString(), null);
    }

    // 封裝 data + expires 為 JSON 的方法
    private String buildJsonData(Gson gson, Object data, long expires) {
        Map<String, Object> map = new HashMap<>();
        map.put("data", data);
        map.put("expires", expires);
        return gson.toJson(map);
    }

    // 防止 JSON 被 JS 注入時破壞語法，例如包含引號
    private String escapeForJs(String input) {
        return input.replace("\\", "\\\\")
                .replace("'", "\\'")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    @Override
    public void onDestroy() {
        FightFanZhaUtils.reset();
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventVo event) {
        switch (event.getEvent()) {
            case EVENT_CHANGE_URL_FANZHA_FINSH:
                if (!is3rdLink) {
                    //只有自己的h5域名站作更换域名，重新Load
                    String newBaseUrl = DomainUtil.getH5Domain2();
                    if (TextUtils.isEmpty(newBaseUrl) || TextUtils.isEmpty(url)) {
                        return;
                    }
                    String oldBaseUrl = FightFanZhaUtils.getDomain(url);
                    if (!FightFanZhaUtils.checkBeforeReplace(oldBaseUrl)) {
                        return;
                    }
                    String goUrl = url.replace(oldBaseUrl, newBaseUrl);
                    CfLog.d("fanzha-刷新最新域名加载url： " + goUrl);
                    X5WebView.getInstance(mContext).getWebView().loadUrl(goUrl);
                }
                break;
        }
    }

    public class CustomWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            CfLog.d("onPageStarted url:  " + url);

            CfLog.d("is3rdLink: " + is3rdLink);
            if (is3rdLink) {
                return;
            }
            if (isFirstLoad) {
                isFirstLoad = false;
                setWebCookie();
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            CfLog.d("onPageFinished url: " + url);
            LoadingDialog.finish();
            hideLoading();
        }

        @Override
        public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
            sslErrorHandler.proceed();
        }


        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            CfLog.e("errorCode: " + errorCode + ", description: " + description + ", failingUrl: " + failingUrl);
            hideLoading();
            Toast.makeText(getContext(), R.string.network_failed, Toast.LENGTH_SHORT).show();
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
            if (FightFanZhaUtils.checkRequest(getContext(), webResourceRequest, is3rdLink, url)) {
                return FightFanZhaUtils.replaceLoadingHtml(is3rdLink);
            }
            return super.shouldInterceptRequest(webView, webResourceRequest);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
            if (FightFanZhaUtils.checkRequest(getContext(), webResourceRequest, is3rdLink, url)) {
                return false;
            }
            return super.shouldOverrideUrlLoading(webView, webResourceRequest);
        }
    }

    public class CustomWebChromeClient extends WebChromeClient {
        @Override
        public void onReceivedTitle(WebView webView, String s) {
            super.onReceivedTitle(webView, s);
            FightFanZhaUtils.checkHeadTitle(webView, s, is3rdLink, url);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            // 网页加载进度
            CfLog.d("******* newProgress: " + newProgress);
            if (newProgress > 0 && newProgress < 100) {
                if (newProgress >= 75) {
                    LoadingDialog.finish();
                }
            }
        }

        // debug模式
        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            CfLog.d("AgentWeb", consoleMessage.message() + " -- From line "
                    + consoleMessage.lineNumber() + " of "
                    + consoleMessage.sourceId());
            return true;
        }

        /**
         * For Android >= 4.1
         * 16(Android 4.1.2) <= API <= 20(Android 4.4W.2)回调此方法
         */
        public void openFileChooser(ValueCallback<Uri> valueCallback, String acceptType, String capture) {
            CfLog.i("*********");
            mUploadCallbackBelow = valueCallback;
            gotoSelectMedia();
        }

        /**
         * For Android >= 5.0
         * API >= 21(Android 5.0.1)回调此方法
         */
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, android.webkit.WebChromeClient.FileChooserParams fileChooserParams) {
            CfLog.i("*********");
            // (1)该方法回调时说明版本API >= 21，此时将结果赋值给 mUploadCallbackAboveL，使之 != null
            mUploadCallbackAboveL = filePathCallback;
            gotoSelectMedia();
            return true;
        }
    }
}
