package com.xxl.job.executor.service.jobhandler;

/**
 * @ClassName TransactionTemplateAction
 * @Description TODO
 * @Author dlavender
 * @Date 2022/6/14 22:04
 * @Version 1.0
 **/
@FunctionalInterface
public interface TransactionTemplateAction {
    public abstract void run(String e);
}
