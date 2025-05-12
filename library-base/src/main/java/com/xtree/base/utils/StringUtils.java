package com.xtree.base.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.xtree.base.R;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    public static String[] getPinYinInitials(String chinese) {
        String[] initials = new String[]{String.valueOf(chinese.charAt(0))};
        if (isChinese(String.valueOf(chinese.charAt(0)))) {
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(chinese.charAt(0));
            initials = new String[pinyinArray.length];

            for (int i = 0; i < pinyinArray.length; i++) {
                initials[i] = pinyinArray[i].substring(0, 1).toUpperCase();
            }
        }
        return initials;
    }

    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isDouble(String value) {
        try {
            Double.parseDouble(value);
            if (value.contains(".")) return true;
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isNumber(String str) {
        Pattern pattern = Pattern.compile("-?[0-9]+(\\.[0-9]+)?");
        String bigStr;
        try {
            bigStr = new BigDecimal(str).toString();
        } catch (Exception exception) {
            return false;//
        }
        Matcher isNumber = pattern.matcher(bigStr);
        if (!isNumber.matches()) {
            return false;
        }
        return true;
    }

    /**
     * float 数据格式转换成逗号分割的String
     */
    public static String formatToSeparate(float data) {
        DecimalFormat df = new DecimalFormat("#,###.00");
        return df.format(data);

    }

    public static void copy(String txt) {
        CfLog.d(txt);
        ClipboardManager cm = (ClipboardManager) Utils.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData cd = ClipData.newPlainText("txt", txt);
        cm.setPrimaryClip(cd);
        ToastUtils.showLong(R.string.txt_copied);
    }

    public static boolean isStartHttp(final String txt) {
        return txt.startsWith("http");
    }

    /**
     * 是否中文
     *
     * @param str
     * @return
     */
    public static boolean isChinese(String str) {
        return str.matches("[\\u4e00-\\u9fa5]+");
    }

    /**
     * 获取本机app版本
     */
    public static String getVersionName(final Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
           return "";
        }
        String version = info.versionName;
        return version;
    }

    /**
     * 获取App versionCode
     */
    public static String getVersionCode(final Context context) {
        PackageInfo pi = null;
        try {
            pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            CfLog.e(e.toString());
        }

        String versionCode;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            versionCode = pi.getLongVersionCode() + "";
        } else {
            versionCode = pi.versionCode + "";
        }
        return versionCode;
    }
    /**切割提款用户名*/
    public  static String splitWithdrawUserName(String  userName){

        if (!userName.contains("@")){
            return userName;
        }else {
            String firstName = userName.substring(0, userName.indexOf("@"));
            CfLog.e("splitWithdrawUserName = " + firstName);
            return firstName;
        }

    }

    private StringUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 判断字符串是否为null或长度为0
     *
     * @param s 待校验字符串
     * @return {@code true}: 空<br> {@code false}: 不为空
     */
    public static boolean isEmpty(final CharSequence s) {
        return s == null || s.length() == 0;
    }

    /**
     * 判断字符串是否为null或全为空格
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空格<br> {@code false}: 不为null且不全空格
     */
    public static boolean isTrimEmpty(final String s) {
        return (s == null || s.trim().length() == 0);
    }

    /**
     * 判断字符串是否为null或全为空白字符
     *
     * @param s 待校验字符串
     * @return {@code true}: null或全空白字符<br> {@code false}: 不为null且不全空白字符
     */
    public static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断两字符串是否相等
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equals(final CharSequence a, final CharSequence b) {
        if (a == b) return true;
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 判断两字符串忽略大小写是否相等
     *
     * @param a 待校验字符串a
     * @param b 待校验字符串b
     * @return {@code true}: 相等<br>{@code false}: 不相等
     */
    public static boolean equalsIgnoreCase(final String a, final String b) {
        return a == null ? b == null : a.equalsIgnoreCase(b);
    }

    /**
     * null转为长度为0的字符串
     *
     * @param s 待转字符串
     * @return s为null转为长度为0字符串，否则不改变
     */
    public static String null2Length0(final String s) {
        return s == null ? "" : s;
    }

    /**
     * 返回字符串长度
     *
     * @param s 字符串
     * @return null返回0，其他返回自身长度
     */
    public static int length(final CharSequence s) {
        return s == null ? 0 : s.length();
    }

    /**
     * 首字母大写
     *
     * @param s 待转字符串
     * @return 首字母大写字符串
     */
    public static String upperFirstLetter(final String s) {
        if (isEmpty(s) || !Character.isLowerCase(s.charAt(0))) return s;
        return String.valueOf((char) (s.charAt(0) - 32)) + s.substring(1);
    }

    /**
     * 首字母小写
     *
     * @param s 待转字符串
     * @return 首字母小写字符串
     */
    public static String lowerFirstLetter(final String s) {
        if (isEmpty(s) || !Character.isUpperCase(s.charAt(0))) return s;
        return String.valueOf((char) (s.charAt(0) + 32)) + s.substring(1);
    }

    /**
     * 反转字符串
     *
     * @param s 待反转字符串
     * @return 反转字符串
     */
    public static String reverse(final String s) {
        int len = length(s);
        if (len <= 1) return s;
        int mid = len >> 1;
        char[] chars = s.toCharArray();
        char c;
        for (int i = 0; i < mid; ++i) {
            c = chars[i];
            chars[i] = chars[len - i - 1];
            chars[len - i - 1] = c;
        }
        return new String(chars);
    }

    /**
     * 转化为半角字符
     *
     * @param s 待转字符串
     * @return 半角字符串
     */
    public static String toDBC(final String s) {
        if (isEmpty(s)) return s;
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == 12288) {
                chars[i] = ' ';
            } else if (65281 <= chars[i] && chars[i] <= 65374) {
                chars[i] = (char) (chars[i] - 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }

    /**
     * 转化为全角字符
     *
     * @param s 待转字符串
     * @return 全角字符串
     */
    public static String toSBC(final String s) {
        if (isEmpty(s)) return s;
        char[] chars = s.toCharArray();
        for (int i = 0, len = chars.length; i < len; i++) {
            if (chars[i] == ' ') {
                chars[i] = (char) 12288;
            } else if (33 <= chars[i] && chars[i] <= 126) {
                chars[i] = (char) (chars[i] + 65248);
            } else {
                chars[i] = chars[i];
            }
        }
        return new String(chars);
    }

    /**
     * 判断字符串是否是数字
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        return str.matches("^[0-9]+(.[0-9]=)?$");
        /*int temp = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '.' && temp == 0) {
                temp++;
                continue;
            }
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
            if (str.charAt(str.length() - 1) == '.') {
                return false;
            }
        }
        return true;*/
    }
}
