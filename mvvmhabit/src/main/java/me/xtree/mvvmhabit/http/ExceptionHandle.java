package me.xtree.mvvmhabit.http;

import android.net.ParseException;

import com.google.gson.JsonParseException;
import com.google.gson.stream.MalformedJsonException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.net.ConnectException;

import retrofit2.HttpException;

/**
 * Created by goldze on 2017/5/11.
 */
public class ExceptionHandle {

    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int SERVICE_UNAVAILABLE = 503;

    public static ResponseThrowable handleException(Throwable e) {
        ResponseThrowable ex;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new ResponseThrowable(e, ERROR.HTTP_ERROR, true);
            switch (httpException.code()) {
                case UNAUTHORIZED:
                    ex.message = "操作未授权";
                    break;
                case FORBIDDEN:
                    ex.code = FORBIDDEN;
                    ex.message = "抱歉，当前国家或地区不支持服务"; //"请求被拒绝";
                    break;
                case NOT_FOUND:
                    ex.message = "资源不存在";
                    break;
                case REQUEST_TIMEOUT:
                    ex.message = "服务器执行超时";
                    break;
                case INTERNAL_SERVER_ERROR:
                    ex.message = "服务器内部错误";
                    break;
                case SERVICE_UNAVAILABLE:
                    ex.message = "服务器不可用";
                    break;
                default:
                    ex.message = "网络错误";
                    break;
            }
            ex.message = ex.message + " [" + httpException.code() + "]";
            // return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException || e instanceof MalformedJsonException) {
            ex = new ResponseThrowable(e, ERROR.PARSE_ERROR);
            ex.message = "解析错误";
            // return ex;
        } else if (e instanceof ConnectException) {
            ex = new ResponseThrowable(e, ERROR.NETWORD_ERROR, true);
            ex.message = "连接失败";
            // return ex;
        } else if (e instanceof javax.net.ssl.SSLException) {
            ex = new ResponseThrowable(e, ERROR.SSL_ERROR, true);
            ex.message = "证书验证失败";
            // return ex;
        } else if (e instanceof ConnectTimeoutException) {
            ex = new ResponseThrowable(e, ERROR.TIMEOUT_ERROR, true);
            ex.message = "连接超时";
            // return ex;
        } else if (e instanceof java.net.SocketTimeoutException) {
            ex = new ResponseThrowable(e, ERROR.TIMEOUT_ERROR, true);
            ex.message = "连接超时";
            // return ex;
        } else if (e instanceof java.net.UnknownHostException) {
            ex = new ResponseThrowable(e, ERROR.HOST_ERROR, true);
            ex.message = "主机地址未知";
            // return ex;
        } else if (e instanceof NullPointerException) {
            ex = new ResponseThrowable(e, ERROR.TIMEOUT_ERROR, true);
            ex.message = "主机地址未知";
            // return ex;
        } else if (e instanceof HijackedException) {
            ex = new ResponseThrowable(e, ERROR.HIJACKED_ERROR, true);
            ex.message = "主机地址未知";
            return ex;
        } else {
            ex = new ResponseThrowable(e, ERROR.UNKNOWN);
            ex.message = "未知错误";
            //return ex;
        }
        //仅展示中文错误即可，所以注释下面拼接错误，ikun定的方案
        //ex.message = ex.message + " [" + ex.getMessage() + "]";
        return ex;
    }



    /**
     * 约定异常
     */
    public class ERROR {
        /**
         * 未知错误
         */
        public static final int UNKNOWN = 1000;
        /**
         * 解析错误
         */
        public static final int PARSE_ERROR = 1001;
        /**
         * 网络错误
         */
        public static final int NETWORD_ERROR = 1002;
        /**
         * 协议出错
         */
        public static final int HTTP_ERROR = 1003;

        /**
         * 证书出错
         */
        public static final int SSL_ERROR = 1005;

        /**
         * 连接超时
         */
        public static final int TIMEOUT_ERROR = 1006;

        /**
         * 域名解析错误
         */
        public static final int HOST_ERROR = 1007;

        /**
         * 域名被劫持
         */
        public static final int HIJACKED_ERROR = 1008;

    }

}

