package com.xxl.job.admin.core.biz.impl;


import com.xxl.job.admin.core.biz.ExecutorBiz;

/**
 * @author dyw
 * @date 2022-07-06  22:32
 */
public class ExecutorBizClient implements ExecutorBiz {
    private String addressUrl;
    private String accessToken;
    private int timeout = 3;
    public ExecutorBizClient(){}
    public ExecutorBizClient(String addressUrl,String accessToken){
        this.addressUrl = addressUrl;
        this.accessToken = accessToken;
        if (!this.addressUrl.endsWith("/")){
            this.addressUrl =this.addressUrl + "/";
        }
    }
    @Override
    public ReturnT<String> beat() {
        return null;
    }

    @Override
    public ReturnT<String> run(TriggerParam triggerParam) {
        return XxlJobRemotingUtil.postBody(addressUrl+"run",accessToken,timeout,triggerParam,String.class);
    }
}
