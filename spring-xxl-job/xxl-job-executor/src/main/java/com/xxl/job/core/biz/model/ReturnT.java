package com.xxl.job.core.biz.model;

import java.io.Serializable;

/**
 * @author dyw
 * @date 2022-07-03  22:12
 */
public class ReturnT<T> implements Serializable {
    public static final long serialVersionUID = 42L;
    public static final int SUCCESS_CODE = 200;
    public static final int FAIL_CODE = 500;

    public static final ReturnT<String> SUCCESS = new ReturnT<>(null);
    public static final ReturnT<String> FAIL = new ReturnT<>(FAIL_CODE,null);

    private int code;
    private String msg;
    private T context;

    public ReturnT(){}

    public ReturnT(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ReturnT(T context) {
        this.context = context;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getContext() {
        return context;
    }

    public void setContext(T context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return "ReturnT{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", context=" + context +
                '}';
    }
}
