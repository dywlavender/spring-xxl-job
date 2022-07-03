package com.xxl.job.core.biz;

import com.xxl.job.core.biz.model.HandleCallbackParam;
import com.xxl.job.core.biz.model.RegistryParam;
import com.xxl.job.core.biz.model.ReturnT;

import java.util.List;

/**
 * @author dyw
 * @date 2022-07-03  22:09
 */
public interface AdminBiz {
    public ReturnT<String> callback(List<HandleCallbackParam> callbackParamList);
    public ReturnT<String> registry(RegistryParam registryParam);
    public ReturnT<String> registryRemove(RegistryParam registryParam);
}
