package com.baidu.xxl.job.core.core.biz;

import com.baidu.xxl.job.core.core.biz.model.HandleCallbackParam;

import java.util.List;

/**
 * @ClassName AdminBiz
 * @Description TODO
 * @Author dlavender
 * @Date 2022/7/18 13:03
 * @Version 1.0
 **/
public interface AdminBiz {
    public ReturnT<String> callback(List<HandleCallbackParam> callbackParamList);

    public ReturnT<String> registry(Registry registry);

    public ReturnT<String> registryRemove(Registry registry);
}
