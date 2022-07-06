package com.xxl.job.admin.core.trigger;

import com.xxl.job.admin.core.conf.XxlJobAdminConfig;
import com.xxl.job.admin.core.route.ExecutorRouteStrategyEnum;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.biz.model.TriggerParam;
import com.xxl.job.core.enums.ExecutorBlockStrategyEnum;
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

    private static void processTrigger(XxlJobGroup group,XxlJobInfo jobInfo,int finalFailRetryCount,TriggerTypeEnum triggerType,int index,int total){
        //param
        ExecutorBlockStrategyEnum  blockStrategy = ExecutorBlockStrategyEnum.match(jobInfo.getExecutorBlockStrategy(),ExecutorBlockStrategyEnum.SERIAL_EXECUTOR);
        ExecutorRouteStrategyEnum executorRouteStrategyEnum = ExecutorRouteStrategyEnum.match(jobInfo.getExecutorRouteStrategy(),null);
        String shardingEnum = (ExecutorRouteStrategyEnum.SHARDING_BROADCAST==executorRouteStrategyEnum)?String.valueOf(index).concat("/").concat(String.valueOf(total)):null;

        //1、save log-id
        XxlJobLog jobLog = new XxlJobLog();

        //2、init trigger param
        TriggerParam triggerParam = new TriggerParam();

        //3、init address
        String address = null;
        ReturnT<String> routeAddressResult = null;
        if (group.getRegistryList() != null && !group.getRegistryList().isEmpty()){
            if(ExecutroRouteStrategyEnum.SHARDING_BROADCAST == executorRouteStrategyEnum){
                if(index < group.getRegistryList().size()){
                    address = group.getRegistryList().get(index);
                }else {
                    address = group.getRegistryList().get(0);
                }
            }else {

            }
        }else{

        }

        //4、trigger remote executor
        ReturnT<String> triggerResult = null;
        if (address != null){
            triggerResult = runExecutor(triggerParam, address);
        }else {
            triggerResult = new ReturnT<String>(ReturnT.FAIL_CODE,null);
        }

        //5、collection trigger info
        StringBuffer triggerMsgSb = new StringBuffer();

        //6、save log trigger-info

    }


    public static ReturnT<String> runExecutor(TriggerParam triggerParam,String address){
        ReturnT<String> runResult = null;
        try{
            ExecutorBiz executorBiz = XxlJobSheduler.getExecutorBiz(address);
            runResult = executorBiz.run(triggerParam);
        }catch (Exception e){
            runResult = new ReturnT<String>(ReturnT.FAIL_CODE,null);
        }

        StringBuffer runResultSB = new StringBuffer(I18nUtil.getString("jobconf_trigger_run")+":");
        runResultSB.append("<br>address: ").append(address);
        runResultSB.append("<br>code: ").append(runResult.getCode());
        runResultSB.append("<br>msg: ").append(runResult.getMsg());

        runResult.setMsg(runResultSB.toString());
        return runResult;
    }
}
