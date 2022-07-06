package com.xxl.job.admin.core.sheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * @ClassName XxlJobScheduler
 * @Description TODO
 * @Author dlavender
 * @Date 2022/7/6 8:42
 * @Version 1.0
 **/
public class XxlJobScheduler {
    private static final Logger logger = LoggerFactory.getLogger(XxlJobScheduler.class);
    public void init(){

        JobTriggerPoolHelper.toStart();

    }

    private static ConcurrentMap<String,ExecutorBiz> executorBizRepository = new ConcurrentHashMap<>();
    public static ExecutorBiz getExecutorBiz(String address){
        if (address==null || address.trim().length()==0){
            return null;
        }
        address = address.trim();
        ExecutorBiz executorBiz = executorBizRepository.get(address);
        if (executorBiz != null){
            return executorBiz;
        }
        executorBiz = new ExecutorBizClient(address,XxlJobAdminConfig.geteAdminConfig(),getAccessToken());
        executorBizRepository.put(address,executorBiz);
        return executorBiz;
    }
}
