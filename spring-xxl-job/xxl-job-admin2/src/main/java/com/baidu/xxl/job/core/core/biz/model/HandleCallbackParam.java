package com.baidu.xxl.job.core.core.biz.model;

import java.io.Serializable;

/**
 * @ClassName HandleCallbackParam
 * @Description TODO
 * @Author dlavender
 * @Date 2022/7/18 12:58
 * @Version 1.0
 **/
public class HandleCallbackParam implements Serializable {
    private static final long serivalVersionUID = 42L;
    private long logID;
    private long logDateTim;
    private int handleCode;
    private String handleMsg;

    public HandleCallbackParam(){}
    public HandleCallbackParam(long logID,long logDateTim,int handleCode,String handleMsg){
        this.logID = logID;
        this.logDateTim = logDateTim;
        this.handleCode = handleCode;
        this.handleMsg = handleMsg;
    }

    public long getLogID() {
        return logID;
    }

    public void setLogID(long logID) {
        this.logID = logID;
    }

    public long getLogDateTim() {
        return logDateTim;
    }

    public void setLogDateTim(long logDateTim) {
        this.logDateTim = logDateTim;
    }

    public int getHandleCode() {
        return handleCode;
    }

    public void setHandleCode(int handleCode) {
        this.handleCode = handleCode;
    }

    public String getHandleMsg() {
        return handleMsg;
    }

    public void setHandleMsg(String handleMsg) {
        this.handleMsg = handleMsg;
    }

    @Override
    public String toString() {
        return "HandleCallbackParam{" +
                "logID=" + logID +
                ", logDateTim=" + logDateTim +
                ", handleCode=" + handleCode +
                ", handleMsg='" + handleMsg + '\'' +
                '}';
    }
}
