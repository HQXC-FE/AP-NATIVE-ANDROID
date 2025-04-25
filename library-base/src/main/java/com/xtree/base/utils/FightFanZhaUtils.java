package com.xtree.base.utils;

import static com.xtree.base.utils.EventConstant.EVENT_UPLOAD_EXCEPTION;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebView;
import com.xtree.base.BuildConfig;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.net.fastest.ChangeH5LineUtil;
import com.xtree.base.request.UploadExcetionReq;
import com.xtree.base.vo.EventVo;
import com.xtree.base.widget.BrowserActivityX5;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.xtree.mvvmhabit.utils.Utils;

public class FightFanZhaUtils {


    //运营商dns劫持风险h5到反诈落地页
    /*
     *   比如 中国移动  完全对应产品那边发现的反诈劫持问题
     *   https://wap.cq.10086.cn/mapp/activities/fanzha2021/html-phone-yd/index.html
     *
     *   还有中国电信 和中国联通 的，暂时没捕获到，后续增加标本
     *  189.cn   10010.com
     *
     * */

    private final static String[] KEY_WORD = new String[]{"诈骗", "反诈",
            "公安", "工信部", "中国移动", "中国电信", "中国联通", "96110"
//            , "com.hicorenational.antifraud"
//            , "id1552823102"
            , "公检"
    };

    public static String fzH5 = "https://wap.cq.10086.cn/mapp/activities/fanzha2021/html-phone-yd/index.html";


    private static boolean solveingFanZhaHack;//正在处理当前webview的反诈劫持；shouldInterceptRequest中存在异步线程
    private static int retryCount = 0;

    // 静态 HTML 内容    感知到劫持后的 ui 填充
    private static String loadingHtml = "<html>" +
            "<head><title>flag安全加载</title></head>" +
            "<body><h1>当前域名访问信号不佳，正在为您匹配新的线路..</h1>" +
            "<p>如有疑问请洽客服...</p>" +
            "</body></html>";

    private static String tipsHtml = "<html>" +
            "<head><title>flag安全加载</title></head>" +
            "<body><h1>当前域名访问信号不佳，即将为您转至浏览器..</h1>" +
            "<p>如有疑问请洽客服...</p>" +
            "</body></html>";


    //可能会有多个webview载体 多次实例化 且多次被劫持
    public static void init() {
        CfLog.d("fanzha - init");
        solveingFanZhaHack = false; //新的实例，有权限去处理劫持
        retryCount = 0;
    }

    public static void reset() {
        CfLog.d("fanzha - reset");
        solveingFanZhaHack = false; //控制字段重置，反诈后异步的测速任务还是会走，刷最新域名。
    }


    //判断title

    /**
     * @param webView         当前容器的webview
     * @param onReceivedTitle 获取到的 html title
     * @param isThird         是否是三方页面
     * @param businessUrl     当前webview一开始 load的url
     */
    public static Boolean checkHeadTitle(WebView webView, String onReceivedTitle, boolean isThird, String businessUrl) {
        CfLog.d("fanzha-check ReceivedTitle string : " + onReceivedTitle);
        if (!TextUtils.isEmpty(onReceivedTitle)) {

            for (String s : KEY_WORD) {
                if (onReceivedTitle.contains(s)) {
                    if (solveingFanZhaHack) {
                        return true;
                    }
                    solveAfterCheck(webView, isThird, businessUrl, webView.getContext());
                    return true;
                }
            }
        }
        return false;
    }

    //判断webResourceRequest
    public static Boolean checkRequest(Context context, WebResourceRequest webResourceRequest, boolean isThird, String businessUrl) {
        if (checkRequestDetail(webResourceRequest)) {
            if (solveingFanZhaHack) {
                return true;
            }
            solveAfterCheck(null, isThird, businessUrl, context);
            return true;
        }
        return false;
    }


    private static Boolean checkRequestDetail(WebResourceRequest webResourceRequest) {
        if (webResourceRequest == null) {
            return false;
        }

        if (webResourceRequest.getUrl() == null) {
            return false;
        }

        if (TextUtils.isEmpty(webResourceRequest.getUrl().toString())) {
            return false;
        }

        String url = webResourceRequest.getUrl().toString();
        if (BuildConfig.DEBUG && isOpenTest) {
            CfLog.d("fanzha-check ShouldInterceptRequest url: " + url + " header data :" +
                    new Gson().toJson(webResourceRequest.getRequestHeaders()));
        }

        //包含fanzha2021  100% 是反诈
        //不能所有请求都拦截去拉取具体资源去分析，影响性能

        if (url.contains("fanzha2021")) {
            CfLog.d("fanzha-checkRequestDetail 捕获到反诈劫持请求");
            return true;
        }

        return false;
    }


    public static WebResourceResponse replaceLoadingHtml(boolean isGame) {
        String html = loadingHtml;
        if (isGame) {
            html = tipsHtml;
        }
        InputStream inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
        return new WebResourceResponse("text/html", "UTF-8", inputStream);
    }


    private static void solveAfterCheck(WebView webView, boolean isThird, String businessUrl, Context context) {

        if (!solveingFanZhaHack) {
            solveingFanZhaHack = true;
        }

        CfLog.d("fanzha-begin solveAfterCheck  ");

        //非三方h5  作h5域名重测速，且移除被劫持h5域名
        if (!isThird) {

            if (webView != null) {
                //先阻断反诈页面的加载  展示友好ui
                webView.loadData(loadingHtml, "text/html", "UTF-8");
            }
            if (!TextUtils.isEmpty(businessUrl)) {
                Set<String> stringSet = new HashSet<>();
                HashSet<String> cacheDomains = (HashSet<String>) CacheManager.get()
                        .getAsObject(SPKeyGlobal.KEY_H5_URL_RECORD_BY_FANZHA);

                if (cacheDomains != null) {
                    stringSet.addAll(cacheDomains);
                }

                stringSet.add(getDomain(businessUrl));

                String cacheJson = new Gson().toJson(stringSet);
                CfLog.d("fanzha-缓存的每条被劫持过的h5域名list json ：" + cacheJson);

                CacheManager.get().put(SPKeyGlobal.KEY_H5_URL_RECORD_BY_FANZHA, (Serializable) stringSet);

            }

            //异步重测速，方法内已过滤缓存的劫持域名
            //测速完成后，solveingFanZhaHack 重置
            if (retryCount < 5) { //如果重新测速5次  当前重刷的webview还是被劫持，就不测速去换了，只填充一个默认ui
                ChangeH5LineUtil.getInstance().start(true);
            }

            retryCount++;

        } else {

            if (webView != null) {
                webView.loadData(tipsHtml, "text/html", "UTF-8");
            }

            TagUtils.tagEvent(Utils.getContext(), "event_hijacked", businessUrl);

            if (context != null) {
                AppUtil.goBrowser(context, DomainUtil.getH5Domain2());
            }

            //上传到三方异常的接口

            UploadExcetionReq req = new UploadExcetionReq();
            req.setLogTag("thirdgame_domain_hijacked");
            req.setApiUrl(businessUrl);
            req.setMsg("反诈中心劫持 : " + businessUrl);
            EventBus.getDefault().post(new EventVo(EVENT_UPLOAD_EXCEPTION, req));

        }

    }


    public static List<String> filterDomains(List<String> h5fromCloud) {

        if (h5fromCloud == null || h5fromCloud.size() == 0) {
            return h5fromCloud;
        }

        List<String> backH5Urls = new ArrayList<>();
        backH5Urls.addAll(h5fromCloud);

        //需要缓存三方配置中的所有h5域名
        CacheManager.get().put(SPKeyGlobal.KEY_H5_URL_ALL, (Serializable) h5fromCloud);

        HashSet<String> cacheDomains = (HashSet<String>) CacheManager.get()
                .getAsObject(SPKeyGlobal.KEY_H5_URL_RECORD_BY_FANZHA);

        if (cacheDomains != null) {
            for (String item : cacheDomains) {
                CfLog.d("fanzha-getH5DomainRemoveFromFz 去除被劫持过的h5 host " + item);
                if (h5fromCloud.contains(item)) {
                    backH5Urls.remove(item);
                }
            }

            CfLog.d("fanzha-getH5DomainRemoveFromFz 新的list " + new Gson().toJson(backH5Urls));
            return backH5Urls;
        }

        return h5fromCloud;
    }

    //检查当前刷新替换的原h5域名是否已经被反诈标记，确保只替换出问题的当前页面
    //并且检查当前要替换的原h5域名 到底是不是三方配置中的h5域名，防止webview外层乱传 isGame\is3rdLink 字段。
    public static Boolean checkBeforeReplace(String oldUrlDomain) {
        HashSet<String> cachefzDomains = (HashSet<String>) CacheManager.get()
                .getAsObject(SPKeyGlobal.KEY_H5_URL_RECORD_BY_FANZHA);

        List<String> cloudH5Urls = (List<String>) CacheManager.get()
                .getAsObject(SPKeyGlobal.KEY_H5_URL_ALL);

        CfLog.d("fanzha-checkBeforeReplace cachefzDomains " + new Gson().toJson(cachefzDomains));
        CfLog.d("fanzha-checkBeforeReplace cloudH5Urls " + new Gson().toJson(cloudH5Urls));
        if (cachefzDomains != null) {
            if (cachefzDomains.contains(oldUrlDomain)
                    && cloudH5Urls.contains(oldUrlDomain)) {
                return true;
            }
        }
        return false;
    }


    public static String getDomain(String url) {

        String host = "";
        String head = "";

        if (url.startsWith("http://")) {
            head = "http://";
        }

        if (url.startsWith("https://")) {
            head = "https://";
        }

        if (url.startsWith("http://") || url.startsWith("https://")) {
            int startIndex = url.indexOf("//") + 2; // 找到 `//` 后的位置
            int endIndex = url.indexOf("/", startIndex); // 找到第一个 `/` 的位置

            // 如果没有找到 `/`，说明没有路径，直接到结尾
            if (endIndex == -1) {
                endIndex = url.length();
            }

            host = url.substring(startIndex, endIndex);
        }

        return head + host;
    }


    /**
     * 简单模拟反诈劫持流程
     */

    public static boolean isOpenTest = false;
    public static String[] animH5Url = new String[]{
            "https://h12.i2n5wp.xyz",
            "https://h12.y7e8nr.xyz",
            "https://h12.h8h8pm.xyz",
            "https://h22.k2a5dg.xyz",
            "https://h22.k2h9ae.xyz",
            "https://h22.l3q2ur.xyz",
            "https://h32.f9o4qf.xyz",
            "https://h32.r3v9ve.xyz",
            "https://h32.p7l9lh.xyz",
            "https://h42.q7s1iu.xyz",
            "https://h52.s4z9il.xyz",
            "https://h61.saaen.com:16801",
            "https://h61.caaau.com:16801",
            "https://h61.baafj.com:16801",
            "https://h61.aaagx.com:16801",
            "https://h61.xuanwujx.com:16801",
            "https://h61.yhlims.com:16801",
            "https://h61.bfangwang.com:16801"
    };


    public static void startMockFanZha(Context context) {
        isOpenTest = true;
        CfLog.d("fanzha-test startMockFanZha url: " + DomainUtil.getH5Domain2());
        BrowserActivityX5.start(context, "反诈", DomainUtil.getH5Domain2(), false, true);
    }

    public static boolean mockJumpFanZha(WebView webView, String url) {
        if (isOpenTest) {
            CfLog.d("fanzha-test mockHack url: " + url);
            List<String> ani = Arrays.asList(animH5Url);
            boolean isHas = false;

            for (String s : ani) {
                if (url.contains(s)) {
                    isHas = true;
                }
            }

            if (isHas) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl(fzH5);
                    }
                }, 2000);
                return true;
            }
        }

        return false;
    }


}
