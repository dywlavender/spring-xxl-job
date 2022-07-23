package com.xxl.job.admin.core.route;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @ClassName ExecutorRouter
 * @Description TODO
 * @Author dlavender
 * @Date 2022/7/19 22:13
 * @Version 1.0
 **/
public abstract class ExecutorRouter {
    protected static Logger logger = LoggerFactory.getLogger(ExecutorRouter.class);
    public abstract ReturnT<String> route(TriggerParam triggerParam, List<String> addressList);
}
