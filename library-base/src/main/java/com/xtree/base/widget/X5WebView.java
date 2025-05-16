package com.xtree.base.widget;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import android.view.ViewGroup;
import android.webkit.WebSettings;

import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.utils.CfLog;
import com.xtree.base.utils.DomainUtil;

import java.util.HashMap;
import java.util.Map;

import me.xtree.mvvmhabit.utils.SPUtils;

public class X5WebView {

    public enum WebViewMode {
        DEFAULT,
        GAME,
        LOTTERY
    }

    private static X5WebView instance;
    private final WebView webView;
    private WebViewMode currentMode = WebViewMode.DEFAULT;

    private X5WebView(Context context) {
        // 使用 ApplicationContext，避免 Activity 泄漏
        webView = new WebView(context.getApplicationContext());
    }

    public static synchronized X5WebView getInstance(Context context) {
        if (instance == null) {
            instance = new X5WebView(context);
        }
        return instance;
    }

    public void setMode(WebViewMode mode) {
        this.currentMode = mode;
    }

    public void setWebViewClient(WebViewClient webViewClient) {
        webView.setWebViewClient(webViewClient);
    }

    public void setWebChromeClient(WebChromeClient webChromeClient) {
        webView.setWebChromeClient(webChromeClient);
    }

    public void addJavascriptInterface(WebAppInterface webAppInterface) {
        webView.addJavascriptInterface(webAppInterface, "android");
    }

    public void reload() {
        webView.reload();
    }

    public void setDebug() {
        WebView.setWebContentsDebuggingEnabled(true);
    }

    public void bindToContainer(ViewGroup viewGroup) {
        ViewGroup parent = (ViewGroup) webView.getParent();
        if (parent != null) {
            parent.removeView(webView);
        }

        viewGroup.removeAllViews(); // 防止重複添加
        viewGroup.addView(webView, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
    }

    public WebView getWebView() {
        return webView;
    }

    public void loadUrl(String url) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        String token = SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN);
        boolean isFirstOpenBrowser = SPUtils.getInstance().getBoolean(SPKeyGlobal.IS_FIRST_OPEN_BROWSER, true);

        String auth = "bearer " + token;
        String cookie = "auth=" + token
                + ";" + SPUtils.getInstance().getString(SPKeyGlobal.USER_SHARE_COOKIE_NAME)
                + "=" + SPUtils.getInstance().getString(SPKeyGlobal.USER_SHARE_SESSID)
                + ";";

        CfLog.d("Cookie: " + cookie);
        CfLog.d("Authorization: " + auth);

        Map<String, String> header = new HashMap<>();
        if (!TextUtils.isEmpty(token)) {
            header.put("Cookie", cookie);
            header.put("Authorization", auth);
            header.put("Cache-Control", "no-cache");
            header.put("Pragme", "no-cache");
        }

        if (currentMode == WebViewMode.GAME) {
            CfLog.d("not need header.");
            header.clear(); // 游戏 header和cookie只带其中一个即可; FB只能带cookie
        }
        header.put("App-RNID", "87jumkljo"); //
        CfLog.d("header: " + header); // new Gson().toJson(header)

        if (isFirstOpenBrowser && !TextUtils.isEmpty(token)) {
            String urlBase64 = Base64.encodeToString(url.getBytes(), Base64.DEFAULT);
            url = DomainUtil.getH5Domain2() + "/static/sessionkeeper.html?token=" + token
                    + "&tokenExpires=3600&url=" + urlBase64;
            SPUtils.getInstance().put(SPKeyGlobal.IS_FIRST_OPEN_BROWSER, false);
        }

        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setAcceptThirdPartyCookies(webView, true);
        cookieManager.setCookie(url, "auth=" + SPUtils.getInstance().getString(SPKeyGlobal.USER_TOKEN) + ";" + "_sessionHandler=" + SPUtils.getInstance().getString(SPKeyGlobal.USER_SHARE_SESSID));
        cookieManager.flush();


        // 開始加載
        if (header != null && !header.isEmpty()) {
            webView.loadUrl(url, header);
        } else {
            webView.loadUrl(url);
        }
    }

    public void cleanCache() {
        // 1. 清除 WebView 資源
        webView.clearHistory();
        webView.clearFormData();
        webView.loadUrl("about:blank"); // 清除視圖上的內容

        // 2. 清除 Cookie（可選）
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookies(null);
        cookieManager.flush();

        // 3. 清除 localStorage / sessionStorage（需注入 JavaScript）
        webView.evaluateJavascript("sessionStorage.clear();", null);
    }

    public void cleanAllCache() {
        // 1. 清除 WebView 資源
        webView.clearHistory();
        webView.clearFormData();
        webView.clearCache(true);
        webView.loadUrl("about:blank"); // 清除視圖上的內容

        // 2. 清除 Cookie（可選）
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookies(null);
        cookieManager.flush();

        // 3. 清除 localStorage / sessionStorage（需注入 JavaScript）
        webView.evaluateJavascript("localStorage.clear(); sessionStorage.clear();", null);
    }
}
