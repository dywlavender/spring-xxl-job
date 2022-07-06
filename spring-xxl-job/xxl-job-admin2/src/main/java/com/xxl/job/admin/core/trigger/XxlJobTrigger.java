package com.xxl.job.admin.core.trigger;

import com.xxl.job.admin.core.conf.XxlJobAdminConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @ClassName XxlJobTrigger
 * @Description TODO
 * @Author dlavender
 * @Date 2022/7/6 10:10
 * @Version 1.0
 **/
public class XxlJobTrigger {
    private static Logger logger = LoggerFactory.getLogger(XxlJobTrigger.class);

    public static void trigger(int jobId,
                               TriggerTypeEnum triggerType,
                               int failRetryCount,
                               String executorShardingParam,
                               String executorParam,
                               String addressList){
        XxlJobInfo jobInfo = XxlJobAdminConfig.getAdminConfig().getXxlJobInfoDao().loadById(jobId);
        if (jobInfo == null){
            return;
        }
        if (executorParam != null){
            jobInfo.setExecutorParam(executorParam);
        }
        int finalFailRetryCount = failRetryCount>=0?failRetryCount:jobInfo.getExecutroFailRetryCount();
        XxlJobGroup group = XxlJobAdminConfig.getAdminConfig().getXxlJobGroupDao().load(jobInfo.getJobGroup());
        if (addressList != null && addressList.trim().length()>0){
            group.setAddressType(1);
            group.setAddressList(addressList.trim());
        }

        //分片参数
        //路由参数
        if (ExecutorRouteStrategyEnum.SHARDING_BROADCAST == ExecutorRouteStrategyEnum.match(jobInfo.getExecutorRouteStrategy(),null)
                && group.getRegistryList() != null && !group.getRegistryList().isEmpty()
                && shardingParam==null){
            for (int i = 0; i < group.getRegistryList().szie; i++) {
                processTrigger(group,jobInfo,finalFailRetryCount,triggerType,i,group.getRegistryList().szie());
            }else{
                if (shardingParam == null){
                    shardingPAram == new int[]{0,1};
                }
                processTrigger(group,jobInfo,failRetryCount,triggerType,shardingParam[0],shardingParam[1]);
            }

        }

    }
    private static boolean isNumeric(String str){
        try{
            int result = Integer.valueOf(str);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }

    private static void processTrigger(XxlJobGroup group,XxlJobInfo jobInfo,int finalFailRetryCount,TriggerTypeEnum triggerType,int index,int tatal){
        //param

    }

}
