package com.xxl.job.core.biz.handler;

/**
 * @author dyw
 * @date 2022-07-03  22:58
 */
public abstract class IJobHandler {
    public abstract void execute() throws Exception;
    public  void init() throws Exception{

    }
    public  void destory() throws Exception{

    }

}
