package com.xxl.job.admin.core.biz;

/**
 * @author dyw
 * @date 2022-07-06  22:30
 */
public interface ExecutorBiz {
    public ReturnT<String> beat();
    public ReturnT<String> run(TriggerParam triggerParam);

}
