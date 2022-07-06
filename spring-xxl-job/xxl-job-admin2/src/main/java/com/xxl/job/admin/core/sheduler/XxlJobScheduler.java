package com.xxl.job.admin.core.sheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



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

}
