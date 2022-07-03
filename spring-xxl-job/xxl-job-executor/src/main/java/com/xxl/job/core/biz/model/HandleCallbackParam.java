package com.xxl.job.core.biz.model;

import java.io.Serializable;

/**
 * @author dyw
 * @date 2022-07-03  22:21
 */
public class HandleCallbackParam implements Serializable {
    private static final long serivalVersionUID = 42L;
    private long logId;
    private long logDateTim;
    private long handleCode;
    private String handleMsg;

    public HandleCallbackParam(long logId, long logDateTim, long handleCode, String handleMsg) {
        this.logId = logId;
        this.logDateTim = logDateTim;
        this.handleCode = handleCode;
        this.handleMsg = handleMsg;
    }

    public HandleCallbackParam() {
    }

    public long getLogId() {
        return logId;
    }

    public void setLogId(long logId) {
        this.logId = logId;
    }

    public long getLogDateTim() {
        return logDateTim;
    }

    public void setLogDateTim(long logDateTim) {
        this.logDateTim = logDateTim;
    }

    public long getHandleCode() {
        return handleCode;
    }

    public void setHandleCode(long handleCode) {
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
                "logId=" + logId +
                ", logDateTim=" + logDateTim +
                ", handleCode=" + handleCode +
                ", handleMsg='" + handleMsg + '\'' +
                '}';
    }
}
