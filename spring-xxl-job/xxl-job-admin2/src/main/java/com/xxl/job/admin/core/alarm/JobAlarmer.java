package com.xxl.job.admin.core.alarm;

import com.xxl.job.admin.core.model.XxlJobInfo;
import com.xxl.job.admin.core.model.XxlJobLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName JobAlarmer
 * @Description TODO
 * @Author dlavender
 * @Date 2022/7/19 13:30
 * @Version 1.0
 **/
public class JobAlarmer implements ApplicationContextAware, InitializingBean {
    private static Logger logger = LoggerFactory.getLogger(JobAlarmer.class);

    private ApplicationContext applicationContext;
    private List<JobAlarm> jobAlarmList;

    //将beanFactory中所有JobAlarm类都放在jobAlarmList中
    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, JobAlarm> serviceBeanMap = applicationContext.getBeansOfType(JobAlarm.class);
        if (serviceBeanMap.size() > 0) {
            jobAlarmList = new ArrayList<>(serviceBeanMap.values());
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public boolean alarm(XxlJobInfo info, XxlJobLog jobLog){
        boolean result = false;
        if (jobAlarmList != null && jobAlarmList.size() > 0) {
            result = true;
            for (JobAlarm alarm:jobAlarmList){
                boolean resultItem = false;
                try{
                    resultItem = alarm.doAlarm(info,jobLog);
                }catch (Exception e){
                    logger.error(e.getMessage(),e);
                }
                if (!resultItem){
                    result = false;
                }
            }
        }
        return result;
    }
}
