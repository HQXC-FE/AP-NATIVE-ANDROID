package com.xtree.base.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.global.Constant;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.widget.CustomerServiceDialogActivity;

import me.xtree.mvvmhabit.base.AppManager;
import me.xtree.mvvmhabit.utils.SPUtils;


public class AppUtil {

    /**
     * 跳转到客服
     *
     * @param ctx Context
     */
    public static void goCustomerService(Context ctx) {

        ARouter.getInstance().build(RouterActivityPath.Mine.PAGER_CUSTOMER_SERVICE).navigation();
      /* BasePopupView basePopupView = new XPopup.Builder(ctx).dismissOnBackPressed(true)
                .dismissOnTouchOutside(false)
                .asCustom(CustomerServiceDialog.newInstance(ctx, "登录密码总是不正确"));
        basePopupView.show();*/
       /* String serviceLink ;

        if (!TextUtils.isEmpty(SPUtils.getInstance().getString(SPKeyGlobal.APP_SERVICE_LINK))){
            serviceLink =SPUtils.getInstance().getString(SPKeyGlobal.APP_SERVICE_LINK) ;
        }else {
            serviceLink =Constant.URL_CUSTOMER_SERVICE ;
        }
        *//*CfLog.e("goCustomerService  ---- serviceLink ==" +serviceLink);

        CfLog.e("goCustomerService  ---- sDomainUtil.getH5Domain2()" +DomainUtil.getH5Domain2() + serviceLink);*//*
      *//*  goBrowser(ctx, DomainUtil.getH5Domain2() + Constant.URL_CUSTOMER_SERVICE);*//*
        goBrowser(ctx, DomainUtil.getH5Domain2() + serviceLink);*/
    }

    /**
     * 跳转Dialog形式客服
     * @param ctx
     */
    public static void goCustomerServiceDialog(Context ctx) {
       BasePopupView basePopupView = new XPopup.Builder(ctx).dismissOnBackPressed(true)
                .dismissOnTouchOutside(false)
                .asCustom( CustomerServiceDialogActivity.newInstance(ctx));
        basePopupView.show();
       /* String serviceLink ;

        if (!TextUtils.isEmpty(SPUtils.getInstance().getString(SPKeyGlobal.APP_SERVICE_LINK))){
            serviceLink =SPUtils.getInstance().getString(SPKeyGlobal.APP_SERVICE_LINK) ;
        }else {
            serviceLink =Constant.URL_CUSTOMER_SERVICE ;
        }
        *//*CfLog.e("goCustomerService  ---- serviceLink ==" +serviceLink);

        CfLog.e("goCustomerService  ---- sDomainUtil.getH5Domain2()" +DomainUtil.getH5Domain2() + serviceLink);*//*
         *//*  goBrowser(ctx, DomainUtil.getH5Domain2() + Constant.URL_CUSTOMER_SERVICE);*//*
        goBrowser(ctx, DomainUtil.getH5Domain2() + serviceLink);*/
    }


    /**
     * 跳转到客服
     *
     * @param ctx Context
     */
    public static void goCustomerServiceWeb(Context ctx) {
        String serviceLink ;

        if (!TextUtils.isEmpty(SPUtils.getInstance().getString(SPKeyGlobal.APP_SERVICE_LINK))){
            serviceLink =SPUtils.getInstance().getString(SPKeyGlobal.APP_SERVICE_LINK) ;
        }else {
            serviceLink =Constant.URL_CUSTOMER_SERVICE ;
        }
    /*    CfLog.e("goCustomerService  ---- serviceLink ==" +serviceLink);

        CfLog.e("goCustomerService  ---- sDomainUtil.getH5Domain2()" +DomainUtil.getH5Domain2() + serviceLink);
           goBrowser(ctx, DomainUtil.getH5Domain2() + Constant.URL_CUSTOMER_SERVICE);*/
        goBrowser(ctx, DomainUtil.getH5Domain2() + serviceLink);
    }


    public static void goBrowser(Context ctx, String url) {
        if (ctx == null || url == null || url.isEmpty()) {
            return;
        }

        if (url.startsWith("/")) {
            url = DomainUtil.getH5Domain2() + url;
        } else if (!url.startsWith("http")) {
            url = DomainUtil.getH5Domain2() + "/" + url;
        } else {
            // 正常 url
        }
        CfLog.i("url: " + url);
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        ctx.startActivity(intent);
    }

    public static void goWeb403() {
        CfLog.i("*********");
        AppManager.getAppManager().AppExit();
        String url = DomainUtil.getH5Domain2() + Constant.URL_PAGE_403;
        ARouter.getInstance().build(RouterActivityPath.Widget.PAGER_FORBIDDEN)
                .withString("title", "访问限制")
                .withString("url", url).navigation();
    }

    public synchronized static void goGlobeVerify(String ip, String type) {

        CfLog.i("*********");
//        AppManager.getAppManager().AppExit();

        String html = Constant.URL_PAGE_GLOBE_VERIFY_WY;

        if (!TextUtils.isEmpty(type) && type.equals("wy")) {
            //网易验证
            html = Constant.URL_PAGE_GLOBE_VERIFY_WY;
        } else {
            //阿里验证
            html = Constant.URL_PAGE_GLOBE_VERIFY_ALI;
        }

        String url = DomainUtil.getH5Domain2() + html + "?ip=" + ip;

        ARouter.getInstance().build(RouterActivityPath.Mine.PAGER_GLOBE_VERIFY)
                .withString("title", "安全验证")
                .withString("url", url).navigation();
    }

    /**
     * 今日是否弹窗
     *
     * @return true:默认弹提示, false:今日不弹提示
     */
    public static boolean isTipToday(String key) {
        String cacheDay = SPUtils.getInstance().getString(key, "");
        String today = TimeUtils.getCurDate();
        return !today.equals(cacheDay);
    }

    /**
     * 检测手机号是否规范 (HQAP2-3552) <br/>
     * 1开头共11位，不能是10、11或12开头
     *
     * @param num 手机号
     * @return true:是 false:否
     */
    public static boolean isPhone(String num) {
        String regex = "^1[3456789]\\d{9}$"; // 手机号
        return num.matches(regex);
    }

    /**
     * 检测邮箱是否规范 (HQAP2-3552)  <br/>
     * 依据Email正则表达式 ^\w+(-+.\w+)*@\w+(-.\w+)*.\w+(-.\w+)*$
     *
     * @param num 邮箱
     * @return true:是 false:否
     */
    public static boolean isEmail(String num) {
        String regex = "^[\\w]+([-+.][\\w]+)*@[\\w]+([-.][\\w]+)*\\.[\\w]+([-.][\\w]+)*$"; // 邮箱
        return num.matches(regex);
    }

    /**
     * 检测邮箱是否符合多端(HQAP2-4526)
     *
     * 依据Email正则表达式 ^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}(?:\.[a-zA-Z]{2,})?$
     *
     * @param num 邮箱
     * @return true:是 false:否
     */
    public static boolean isMultiSegmentEmail(String num){
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}(?:\\.[a-zA-Z]{2,})?$"; // 多段邮箱
        return num.matches(regex);
    }

    /**
     * 是否是微信号
     * @return
     */
    public static boolean isWechatAccount(String num){
        String regex = "^[a-zA-Z_-][a-zA-Z0-9_-]{5,19}$"; //
        return num.matches(regex);
    }

    /**
     *是否是支付宝账号,和微信号同样
     * @param ctx
     * @param tvw
     */
    public static boolean isAlipayAccount(String num){
        String regex = "^[a-zA-Z_-][a-zA-Z0-9_-]{5,19}$"; //
        return num.matches(regex);
    }

    public static void setTypeFaceDin(Context ctx, TextView tvw) {
        AssetManager am = ctx.getAssets();
        Typeface tf = Typeface.createFromAsset(am, "fonts/D-DIN-PRO-500-Medium.ttf");
        tvw.setTypeface(tf);
    }

    public static String getAppVersion(Context context) {
        try {
            // 获取 PackageManager 实例
            PackageManager packageManager = context.getPackageManager();
            // 获取当前应用的 PackageInfo 对象
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            // 获取版本号
            String versionName = packageInfo.versionName;
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

}