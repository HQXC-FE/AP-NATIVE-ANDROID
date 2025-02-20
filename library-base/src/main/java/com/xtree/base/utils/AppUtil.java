package com.xtree.base.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.xtree.base.global.Constant;
import com.xtree.base.global.SPKeyGlobal;
import com.xtree.base.router.RouterActivityPath;
import com.xtree.base.widget.CustomerServiceDialogActivity;

import me.xtree.mvvmhabit.utils.SPUtils;

public class AppUtil {
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
    /**
     * 跳转到客服
     *
     * @param ctx Context
     */
    public static void goCustomerServiceWeb(Context ctx) {
        StringBuffer serviceLink = new StringBuffer() ;
        /**
         * 已登录用户，嗨客服拼接用户信息
         *
         * 用户账号：
         * &sid=username
         *
         * 注册来源推广码，profile接口register_promotion_code字段
         * &remark=encodeURIComponent(JSON.stringify({promo: register_promotion_code}))
         *
         *
         * 未登录用户
         * 推广码传递注册用的推广码
         * &remark=encodeURIComponent(JSON.stringify({promo: 推广码}))
         */
        if (!TextUtils.isEmpty(SPUtils.getInstance().getString(SPKeyGlobal.APP_SERVICE_LINK))){
            serviceLink.append(SPUtils.getInstance().getString(SPKeyGlobal.APP_SERVICE_LINK));
            String username = SPUtils.getInstance().getString(SPKeyGlobal.USER_NAME );
            if (TextUtils.isEmpty(username) || username == null){
                serviceLink.append("&remark={\"promo\"%3A\""+SPUtils.getInstance().getString(SPKeyGlobal.APP_REGISTER_CODE)+"\"}");
            }else
            {
                //登录 没有推广码
                https://ap3sport.oxldkm.com/im/chat?platformCode=THRB&channelLink=OKGV5vPNGc&sid=zfqd2008
                if (TextUtils.isEmpty(SPUtils.getInstance().getString(SPKeyGlobal.APP_REGISTER_CODE)) || SPUtils.getInstance().getString(SPKeyGlobal.APP_REGISTER_CODE) == null){
                    serviceLink.append("&sid="+SPUtils.getInstance().getString(SPKeyGlobal.USER_NAME));
                }else{
                    serviceLink.append("&sid="+SPUtils.getInstance().getString(SPKeyGlobal.USER_NAME)+"&remark={\"promo\"%3A\""+SPUtils.getInstance().getString(SPKeyGlobal.APP_REGISTER_CODE)+"\"}");
                }
            }
        }else {
            serviceLink.append(Constant.URL_CUSTOMER_SERVICE);
        }
        CfLog.e("goCustomerService  ---- serviceLink ==" +serviceLink);
        goBrowser(ctx, DomainUtil.getH5Domain2() + serviceLink.toString());
    }

    /**
     * 跳转到客服
     *
     * @param ctx Context
     */
    public static void goCustomerService(Context ctx) {
        ARouter.getInstance().build(RouterActivityPath.Mine.PAGER_CUSTOMER_SERVICE).navigation();
        //goBrowser(ctx, DomainUtil.getDomain2() + Constant.URL_CUSTOMER_SERVICE);
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
    }

    public static void goBrowser(Context ctx, String url) {
        if (ctx == null || url == null || url.isEmpty()) {
            return;
        }

        if (url.startsWith("/")) {
            url = DomainUtil.getDomain2() + url;
        } else if (!url.startsWith("http")) {
            url = DomainUtil.getDomain2() + "/" + url;
        } else {
            // 正常 url
        }
        CfLog.i("url: " + url);
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        ctx.startActivity(intent);
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
     * 检测QQ号是否规范
     * QQ号码至少包含5位数字的连续数字。
     *
     * @param num QQ
     * @return true:是 false:否
     */
    public static boolean isQQ(String num) {
        String regex = "^\\d{5,}$"; // QQ
        return num.matches(regex);
    }

    /**
     * 检测微信号是否规范
     * 微信号必须以字母、下划线或中划线开头，长度在6到20位之间，可以包含数字、字母、下划线和减号。
     *
     * @param num 微信
     * @return true:是 false:否
     */
    public static boolean isWX(String num) {
        String regex = "^[a-zA-Z_-][a-zA-Z0-9_-]{5,19}$"; // 微信
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

}