package com.xxl.job.admin.core.biz.impl;

import com.xxl.job.admin.core.biz.ExecutorBiz;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dyw
 * @date 2022-07-06  22:31
 */
public class ExecutorBizImpl implements ExecutorBiz {
    private static Logger logger = LoggerFactory.getLogger(ExecutorBizImpl.class);

    @Override
    public ReturnT<String> beat() {
        return null;
    }

    @Override
    public ReturnT<String> run(TriggerParam triggerParam) {
        return null;
    }
}
