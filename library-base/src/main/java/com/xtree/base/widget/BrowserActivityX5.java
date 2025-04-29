package com.xtree.base.widget;

import static com.xtree.base.utils.EventConstant.EVENT_CHANGE_URL_FANZHA_FINSH;
import static com.xtree.base.utils.EventConstant.EVENT_TOP_SPEED_FAILED;
import static com.xtree.base.utils.EventConstant.EVENT_TOP_SPEED_FINISH;
import static com.xtree.base.utils.EventConstant.EVENT_UPLOAD_EXCEPTION;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.tencent.smtt.export.external.interfaces.ConsoleMessage;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.xtree.base.BuildConfig;
import com.xtree.base.R;
import com.xtree.base.global.Constant;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.fastest.TopSpeedDomainFloatingWindows;
import com.xtree.base.request.UploadExcetionReq;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.router.RouterFragmentPath;
import com.xtree.base.utils.AppUtil;
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

import me.xtree.mvvmhabit.base.ContainerActivity;
import me.xtree.mvvmhabit.utils.SPUtils;
import me.xtree.mvvmhabit.utils.ToastUtils;

@Route(path = RouterActivityPath.Widget.PAGER_BROWSER_X5)
public class BrowserActivityX5 extends AppCompatActivity {
    public static final String ARG_TITLE = "title";
    public static final String ARG_URL = "url";
    public static final String ARG_IS_CONTAIN_TITLE = "isContainTitle";
    public static final String ARG_IS_SHOW_LOADING = "isShowLoading";
    public static final String ARG_IS_GAME = "isGame";
    public static final String ARG_IS_THIRD = "isThirdDomain";
    public static final String ARG_IS_LOTTERY = "isLottery";
    public static final String ARG_IS_HIDE_TITLE = "isHideTitle";

    private View vTitle;
    private View clTitle;
    private TextView tvwTitle;
    private ImageView ivwBack;
    private ViewGroup viewGroup;
    private ImageView ivwCs;
    private ImageView ivwMsg;
    private ImageView ivwRecharge;
    private ImageView ivwJump;
    private View layoutRight;
    private TopSpeedDomainFloatingWindows mTopSpeedDomainFloatingWindows;

    private String title = "";
    private String url = "";
    private String token; // token
    private boolean isLottery = false; // 是否彩票, 彩票需要header,需要注入IOS标题头样式
    private boolean isGame = false; // 三方游戏, 不需要header和token
    private boolean isThirdDomain = false; // 是否是三方域名的三方游戏
    private X5WebView x5WebView;
    boolean isFirstOpenBrowser = true; // 是否第一次打开webView组件(解决第一次打开webView时传递header/cookie/token失效)
    boolean isShowLoading = false; // 展示loading弹窗
    boolean isHideTitle = false; // 是否隐藏标题
    boolean isContainTitle = false; // 网页自身是否包含标题(少数情况下会包含)
    ValueCallback<Uri> mUploadCallbackBelow;
    ValueCallback<Uri[]> mUploadCallbackAboveL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser_x5);

        EventBus.getDefault().register(this);
        FightFanZhaUtils.init();
        x5WebView = X5WebView.getInstance(this);
        initView();
        title = getIntent().getStringExtra(ARG_TITLE);
        isContainTitle = getIntent().getBooleanExtra(ARG_IS_CONTAIN_TITLE, false);
        isShowLoading = getIntent().getBooleanExtra(ARG_IS_SHOW_LOADING, false);
        isGame = getIntent().getBooleanExtra(ARG_IS_GAME, false);
        isThirdDomain = getIntent().getBooleanExtra(ARG_IS_THIRD, false);
        isLottery = getIntent().getBooleanExtra(ARG_IS_LOTTERY, false);
        isHideTitle = getIntent().getBooleanExtra(ARG_IS_HIDE_TITLE, false);
        token = SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN);
        isFirstOpenBrowser = SPUtils.getInstance().getBoolean(SPKeyGlobal.IS_FIRST_OPEN_BROWSER, true);

        if (isHideTitle) {
            clTitle.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(title)) {
            tvwTitle.setText(title);
        }
        if (isContainTitle) {
            // 创建一个 ConstraintLayout 对象
            ConstraintLayout constraintLayout = findViewById(R.id.cl_root);
            // 创建一个 ConstraintSet 对象
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(constraintLayout);
            // 设置某个 View 的 layout_constraintTop_toTopOf 属性
            constraintSet.connect(R.id.wv_main, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
            // 应用 ConstraintSet 中的设置
            constraintSet.applyTo(constraintLayout);
            findViewById(R.id.cl_title).setOnClickListener(v -> {
                // 解决点击左右上角会触发被遮挡的按钮
                CfLog.i("******");
            });
        }

        url = getIntent().getStringExtra("url");

        Uri uri = getIntent().getData();
        if (uri != null && TextUtils.isEmpty(url)) {
            url = uri.toString();
        }

        CfLog.i("url: " + url);
        if (TextUtils.isEmpty(url)) {
            finish();
        } else {
            if (isShowLoading) {
                LoadingDialog.show2(this);
            }
            try {
                initX5WebView(url);
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        }

        if (isGame) {
            layoutRight.setVisibility(View.VISIBLE);
            initRight();
        }

        // 测试
        if (BuildConfig.DEBUG && FightFanZhaUtils.isOpenTest) {
            FightFanZhaUtils.mockJumpFanZha(X5WebView.getInstance(this).getWebView(), url);
        }
    }

    private void initView() {
        vTitle = findViewById(R.id.v_title);
        clTitle = findViewById(R.id.cl_title);
        tvwTitle = findViewById(R.id.tvw_title);
        ivwBack = findViewById(R.id.ivw_back);
        viewGroup = findViewById(R.id.wv_main);
        ivwCs = findViewById(R.id.ivw_cs);
        ivwMsg = findViewById(R.id.ivw_msg);
        ivwRecharge = findViewById(R.id.ivw_recharge);
        ivwJump = findViewById(R.id.ivw_jump);
        layoutRight = findViewById(R.id.layout_right);

        ivwBack.setOnClickListener(v -> finish());

        viewGroup.setFitsSystemWindows(true);

        mTopSpeedDomainFloatingWindows = new TopSpeedDomainFloatingWindows(this);
        mTopSpeedDomainFloatingWindows.show();
    }

    private void initX5WebView(String url) throws UnknownHostException {
        // debug模式
        if (BuildConfig.DEBUG) {
            x5WebView.setDebug();
        }

        // 設定 WebView 行為模式（選填）
        if (isLottery) {
            x5WebView.setMode(X5WebView.WebViewMode.LOTTERY);
        } else if (isGame) {
            x5WebView.setMode(X5WebView.WebViewMode.GAME);
        } else {
            x5WebView.setMode(X5WebView.WebViewMode.DEFAULT);
        }

        // 設定 WebViewClient
        x5WebView.setWebViewClient(new CustomWebViewClient());
        // 設定 WebChromeClient
        x5WebView.setWebChromeClient(new CustomWebChromeClient());
        // 設定 JS Bridge（注意這裡 "android" 是 JS 調用的名稱）
        x5WebView.addJavascriptInterface(new WebAppInterface(this, ivwBack, getCallBack()));
        // 將 WebView 加入容器
        x5WebView.bindToContainer(viewGroup);
        // 加载
        x5WebView.loadUrl(url);
    }

    public void reload() {
        x5WebView.reload();
    }

    public WebAppInterface.ICallBack getCallBack() {

        return new WebAppInterface.ICallBack() {
            @Override
            public void close() {
                String url2 = getIntent().getStringExtra("url") + "";
                if (!url2.contains(Constant.URL_VIP_CENTER)) {
                    finish();
                }
            }

            @Override
            public void goBack() {
                finish();
            }

            @Override
            public void callBack(String type, Object obj) {
                CfLog.i("type: " + type);
                if (TextUtils.equals(type, "captchaVerifySucceed")) {
                    TagUtils.tagEvent(getBaseContext(), "captchaVerifyOK");
                    ARouter.getInstance().build(RouterActivityPath.Main.PAGER_SPLASH)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            .navigation();
                    finish();
                } else if (TextUtils.equals(type, "captchaVerifyFail")) {
                    TagUtils.tagEvent(getBaseContext(), "captchaVerifyFail");
                    CfLog.e("type error... type: " + type);
                    doFail(type, obj);
                } else {
                    TagUtils.tagEvent(getBaseContext(), "captchaVerifyError");
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
        reload(); // 重新加载 webView (H5端加载了)
    }

    private void initRight() {
        ivwCs.setOnClickListener(v -> {
            // 客服
            AppUtil.goCustomerService(this);
        });
        ivwMsg.setOnClickListener(v -> {
            // 消息
            startContainerFragment(RouterFragmentPath.Mine.PAGER_MSG, null);
        });
        ivwRecharge.setOnClickListener(v -> {
            // 充值
            Bundle bundle = new Bundle();
            bundle.putBoolean("isShowBack", true);
            startContainerFragment(RouterFragmentPath.Recharge.PAGER_RECHARGE, bundle);
        });
        ivwJump.setOnClickListener(v -> {
            //传递token
            String urlBase64 = Base64.encodeToString(url.getBytes(), Base64.DEFAULT);
            String jumpUrl = DomainUtil.getH5Domain2() + "/static/sessionkeeper.html?token=" + token + "&tokenExpires=3600&url=" + urlBase64;
            CfLog.i("jumpUrl: " + jumpUrl);
            // 跳至外部浏览器
            AppUtil.goBrowser(getBaseContext(), jumpUrl);
        });
    }

    public void startContainerFragment(String path, Bundle bundle) {
        Intent intent = new Intent(this, ContainerActivity.class);
        intent.putExtra(ContainerActivity.ROUTER_PATH, path);
        if (bundle != null) {
            intent.putExtra(ContainerActivity.BUNDLE, bundle);
        }
        startActivity(intent);
    }

    private void hideLoading() {
        LoadingDialog.finish();
    }

    /**
     * 图片选择
     */
    private void gotoSelectMedia() {
        PictureSelector.create(this)
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

                    }
                });
    }

    /**
     * @param ctx            上下文
     * @param title          标题
     * @param url            链接
     * @param isContainTitle url对应的网页是否包含标题,默认false
     */
    public static void start(Context ctx, String title, String url, boolean isContainTitle) {
        CfLog.i(title + ", isContainTitle: " + isContainTitle + ", url: " + url);
        Intent it = new Intent(ctx, BrowserActivityX5.class);
        it.putExtra(ARG_TITLE, title);
        it.putExtra(ARG_URL, url);
        it.putExtra(ARG_IS_CONTAIN_TITLE, isContainTitle);
        ctx.startActivity(it);
    }

    public static void start(Context ctx, String title, String url, boolean isContainTitle, boolean isGame) {
        CfLog.i(title + ", isContainTitle: " + false + ", url: " + url);
        Intent it = new Intent(ctx, BrowserActivityX5.class);
        it.putExtra(ARG_TITLE, title);
        it.putExtra(ARG_URL, url);
        it.putExtra(ARG_IS_CONTAIN_TITLE, isContainTitle);
        it.putExtra(ARG_IS_GAME, isGame);
        ctx.startActivity(it);
    }

    public static void start(Context ctx, String title, String url, boolean isContainTitle, boolean isGame, boolean isShowLoading) {
        CfLog.i(title + ", isContainTitle: " + false + ", url: " + url);
        Intent it = new Intent(ctx, BrowserActivityX5.class);
        it.putExtra(ARG_TITLE, title);
        it.putExtra(ARG_URL, url);
        it.putExtra(ARG_IS_CONTAIN_TITLE, isContainTitle);
        it.putExtra(ARG_IS_GAME, isGame);
        it.putExtra(ARG_IS_SHOW_LOADING, isShowLoading);
        ctx.startActivity(it);
    }

    public static void start(Context ctx, String url) {
        CfLog.i("url: " + url);
        Intent it = new Intent(ctx, BrowserActivityX5.class);
        it.putExtra(ARG_URL, url);
        it.putExtra(ARG_IS_HIDE_TITLE, true);
        ctx.startActivity(it);
    }

    public static void startThirdDomain(Context ctx, String title, String playUrl) {
        CfLog.i("URL: " + playUrl);
        Intent it = new Intent(ctx, BrowserActivityX5.class);
        it.putExtra(ARG_URL, playUrl);
        it.putExtra(ARG_TITLE, title);
        it.putExtra(ARG_IS_THIRD, true);
        it.putExtra(BrowserActivityX5.ARG_IS_GAME, true);
        ctx.startActivity(it);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        X5WebView.getInstance(this).cleanCache();

        FightFanZhaUtils.reset();
        EventBus.getDefault().unregister(this);

        if (mTopSpeedDomainFloatingWindows != null) {
            mTopSpeedDomainFloatingWindows.removeView();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventVo event) {
        switch (event.getEvent()) {
            case EVENT_TOP_SPEED_FINISH:
                mTopSpeedDomainFloatingWindows.refresh();
                break;
            case EVENT_TOP_SPEED_FAILED:
                mTopSpeedDomainFloatingWindows.onError();
                break;
            case EVENT_CHANGE_URL_FANZHA_FINSH:
                if (!isGame) {
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
                    X5WebView.getInstance(this).getWebView().loadUrl(goUrl);
                }
                break;
        }
    }

    private void uploadH5Error(String msg, String url) {
        if (isGame) {
            UploadExcetionReq req = new UploadExcetionReq();
            if (isThirdDomain) {
                req.setLogTag("thirdgame_domain_block");
                req.setLogType("三方域名：" + title + "打开失败");
            } else {
                req.setLogTag("thirdgame_domain_exception");
                req.setLogType("公司域名：" + title + "打开失败");
            }

            req.setApiUrl(url);
            req.setMsg(msg);

            EventBus.getDefault().post(new EventVo(EVENT_UPLOAD_EXCEPTION, req));
        }
    }

    private void setJs() {
        String token = SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN);
        String sessid = SPUtils.getInstance().getString(SPKeyGlobal.USER_SHARE_SESSID);
        String json = SPUtils.getInstance().getString(SPKeyGlobal.HOME_PROFILE);

        HashMap mProfileVo = new Gson().fromJson(json, new TypeToken<HashMap>() {
        }.getType());

        long expires = System.currentTimeMillis() + 24 * 60 * 60 * 1000;
        HashMap map = new HashMap<>();
        map.put("data", mProfileVo);
        map.put("expires", expires);
        String userProfile = new Gson().toJson(map);

        map.clear();
        map.put("data", token);
        map.put("expires", expires);
        String auth = new Gson().toJson(map);

        if (isLottery) {
            setLotteryCookieInside(token, sessid, userProfile, auth);
        } else {
            if (!token.isEmpty()) {
                setCookieInside(token, sessid, userProfile, auth);
            }
        }
    }

    private void setCookieInside(String token, String sessid, String userProfile, String auth) {
        StringBuilder js = new StringBuilder();
        js.append("(function() {\n")
                .append("const d = new Date();\n")
                .append("d.setTime(d.getTime() + (24*60*60*1000));\n")
                .append("let expires = \"expires=\"+ d.toUTCString();\n")
                .append("document.cookie = \"auth=").append(token).append(";\" + expires + \";path=/\";\n")
                .append("document.cookie = \"_sessionHandler=").append(sessid).append(";\" + expires + \";path=/\";\n")
                .append("localStorage.setItem('USER-PROFILE', '").append(userProfile).append("');\n")
                .append("localStorage.setItem('AUTH', '").append(auth).append("');\n")
                .append("})()");

        CfLog.i(js.toString().replace("\n", " \t"));
        X5WebView.getInstance(this).getWebView().evaluateJavascript(js.toString(), null);

    }

    private void setLotteryCookieInside(String token, String sessid, String userProfile, String auth) {
        StringBuilder js = new StringBuilder();
        js.append("(function() {\n")
                .append("const d = new Date();\n")
                .append("const style = document.createElement('style');\n")
                .append("style.type = 'text/css';\n")
                .append("style.id = 'iOS_inject';\n")
                .append("document.head.appendChild(style);\n")
                .append("document.querySelector('#iOS_inject').innerHTML = ")
                .append("'.headerH5{display: none !important;} ")
                .append(".all-lottery-all{ margin-top: 0 !important;} ")
                .append(".msg{ display: none !important;} ")
                .append(".menu{ display: none !important;} ")
                .append(".countdown{ margin-right: .8rem;}';\n")
                .append("d.setTime(d.getTime() + (24*60*60*1000));\n")
                .append("let expires = \"expires=\"+ d.toUTCString();\n")
                .append("document.cookie = \"auth=").append(token).append(";\" + expires + \";path=/\";\n")
                .append("document.cookie = \"_sessionHandler=").append(sessid).append(";\" + expires + \";path=/\";\n")
                .append("localStorage.setItem('USER-PROFILE', '").append(userProfile).append("');\n")
                .append("localStorage.setItem('AUTH', '").append(auth).append("');\n")
                .append("})()");

        CfLog.i(js.toString().replace("\n", " \t"));
        X5WebView.getInstance(this).getWebView().evaluateJavascript(js.toString(), null);
    }

    public boolean isGame() {
        return isGame;
    }

    public class CustomWebViewClient extends WebViewClient {
        private final String initialUrl = url; // 替换为你的初始 URL
        private String mUrl;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            CfLog.d("onPageStarted url:  " + url);
            setJs();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            CfLog.d("onPageFinished url: " + url);
            mUrl = url;
            hideLoading();
        }

        @Override
        public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, com.tencent.smtt.export.external.interfaces.SslError sslError) {
            super.onReceivedSslError(webView, sslErrorHandler, sslError);
        }


        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);

            hideLoading();
            Toast.makeText(getBaseContext(), R.string.network_failed, Toast.LENGTH_SHORT).show();
            // 仅处理初始 URL 的加载错误
            if (TextUtils.equals(request.getUrl().toString(), initialUrl)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int errorCode = error.getErrorCode();
                    String description = error.getDescription().toString();
                    String failingUrl = request.getUrl().toString();

                    //ERROR_AUTHENTICATION (-4): 用户身份验证失败，例如身份验证凭据不正确。
                    //ERROR_BAD_URL (-12): URL 格式无效。
                    //ERROR_CONNECT (-6): 连接到服务器失败。
                    //ERROR_FAILED_SSL_HANDSHAKE (-11): SSL 握手失败。
                    //ERROR_FILE (-13): 常规文件错误。
                    //ERROR_FILE_NOT_FOUND (-14): 文件未找到。
                    //ERROR_HOST_LOOKUP (-2): 主机名无法解析，通常是 DNS 错误。
                    //ERROR_IO (-7): 读写错误。
                    //ERROR_PROXY_AUTHENTICATION (-5): 代理服务器需要身份验证。
                    //ERROR_REDIRECT_LOOP (-9): 遇到重定向循环。
                    //ERROR_TIMEOUT (-8): 连接超时。
                    //ERROR_TOO_MANY_REQUESTS (-15): 请求数量过多。
                    //ERROR_UNKNOWN (-1): 未知错误。
                    //ERROR_UNSUPPORTED_AUTH_SCHEME (-3): 不支持的身份验证方案。
                    //ERROR_UNSUPPORTED_SCHEME (-10): 不支持的 URI 方案。
                    String msg = "errorCode: " + errorCode + ", description: " + description + ", failingUrl: " + failingUrl;
                    CfLog.e(msg);
                    // 处理非 HTTP 错误，例如网络错误或 DNS 解析错误
                    uploadH5Error(msg, failingUrl);
                }
            }
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);

            int statusCode = errorResponse.getStatusCode(); // 获取状态码
            String errorUrl = request.getUrl().toString(); // 获取请求的 URL   这个url不对，返回和初始url不一致

            CfLog.d("HTTP Error:  " + "errorUrl:" + errorUrl + ",   initialUrl:" + initialUrl + ",   URL: " + mUrl + ",   Status Code: " + statusCode);
            // 仅处理初始 URL 的 HTTP 错误
            if ((TextUtils.equals(mUrl, initialUrl) || TextUtils.equals(mUrl, null) || TextUtils.equals(mUrl, errorUrl)) && !isFirstOpenBrowser) {
                String msg = "状态码:" + statusCode + "；加载链接：" + initialUrl;
                //处理403 404 500 502等错误
                uploadH5Error(msg, initialUrl);
            }
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
            if (FightFanZhaUtils.checkRequest(getBaseContext(), webResourceRequest, isGame, url)) {
                return FightFanZhaUtils.replaceLoadingHtml(isGame);
            }
            return super.shouldInterceptRequest(webView, webResourceRequest);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
            if (FightFanZhaUtils.checkRequest(getBaseContext(), webResourceRequest, isGame, url)) {
                return false;
            }
            return super.shouldOverrideUrlLoading(webView, webResourceRequest);
        }
    }

    public class CustomWebChromeClient extends WebChromeClient {
        @Override
        public void onReceivedTitle(WebView webView, String s) {
            super.onReceivedTitle(webView, s);
            FightFanZhaUtils.checkHeadTitle(webView, s, isGame, url);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            // 网页加载进度
            if (newProgress > 75) {
                LoadingDialog.finish();
            }
        }

        // debug模式
        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            CfLog.d("WebView", consoleMessage.message() + " -- From line "
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
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            CfLog.i("*********");
            // (1)该方法回调时说明版本API >= 21，此时将结果赋值给 mUploadCallbackAboveL，使之 != null
            mUploadCallbackAboveL = filePathCallback;
            gotoSelectMedia();
            return true;
        }
    }
}