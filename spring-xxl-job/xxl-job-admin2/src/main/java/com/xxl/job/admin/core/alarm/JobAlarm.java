package com.xxl.job.admin.core.alarm;

/**
 * @ClassName JobAlarm
 * @Description TODO
 * @Author dlavender
 * @Date 2022/7/19 14:20
 * @Version 1.0
 **/
public interface JobAlarm {
    public boolean doAlarm(XxlJobInfo info,XxlJobLog jobLog);
}
