package me.xtree.mvvmhabit.http;

import retrofit2.adapter.rxjava2.HttpException;

/**
 * 业务异常类
 */
public class BusinessException extends Exception {
    public int code;
    public String message;
    public Object data; // 处理异常,传数据,备用
    /**
     * 是否网络错误，如404，503等
     */
    public boolean isHttpError;

    public BusinessException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public BusinessException(Throwable throwable, int code, boolean isHttpError) {
                super(throwable);
        this.code = code;
        if(throwable instanceof HttpException){
            this.code = ((HttpException) throwable).code();
        }
        this.isHttpError = isHttpError;
    }

    public BusinessException(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public BusinessException(Throwable throwable, int code) {
        super(throwable);
        this.code = code;
    }

    @Override
    public String toString() {
        return "BusinessException {" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
