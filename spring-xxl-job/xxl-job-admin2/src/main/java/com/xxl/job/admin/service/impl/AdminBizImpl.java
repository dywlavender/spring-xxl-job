package com.xxl.job.admin.service.impl;

import com.baidu.xxl.job.core.core.biz.AdminBiz;
import com.baidu.xxl.job.core.core.biz.model.HandleCallbackParam;

import java.util.List;

/**
 * @ClassName AdminBizImpl
 * @Description TODO
 * @Author dlavender
 * @Date 2022/7/18 13:55
 * @Version 1.0
 **/
public class AdminBizImpl implements AdminBiz {
    @Override
    public ReturnT<String> callback(List<HandleCallbackParam> callbackParamList) {
        return JobCompleteHelper.getInstance().callback(callbackParamList);
    }

    @Override
    public ReturnT<String> registry(Registry registry) {
        return JobRegistryHelper.getInstance().registry(registry);
    }

    @Override
    public ReturnT<String> registryRemove(Registry registry) {
        return JobRegistryHelper.getInstance().registryRemove(registry);
    }
}
