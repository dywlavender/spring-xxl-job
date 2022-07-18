package com.xxl.job.admin.core.complete;

import com.xxl.job.admin.core.thread.JobTriggerPoolHelper;
import com.xxl.job.admin.core.util.I18nUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

/**
 * @ClassName XxlJobCompleter
 * @Description TODO
 * @Author dlavender
 * @Date 2022/7/18 22:19
 * @Version 1.0
 **/
public class XxlJobCompleter {
    private static Logger logger = LoggerFactory.getLogger(XxlJobCompleter.class);

    public static int updateHandleInfoAndFinish(XxlJobLog xxlJobLog){
        finishJob(xxlJobLog);

        if (xxlJobLog.getHandleMsg().length()>15000){
            xxlJobLog.setHandleMsg(xxlJobLog.getHandleMsg().subString(0,15000));
        }
        return XxlJobAdminConfig.getAdminConfig().getXxlJobLogDao().updateHandleInfo(xxlJobLog);
    }

    private static void finishJob(XxlJobLog xxlJobLog){
        String triggerChildMsg = null;
        if (XxlJobContext.HANDLE_CODE_SUCCESS == xxlJobLog.getHandleCode()){
            triggerChildMsg = "<br><br><span style=\"color:#00c0ef;\">>>>"+I18nUtil.getString("jobconf_trigger_chaild_run")+"<<<< </span><br>";

            String[] childJobIds = xxlJobInfo.getChildJobId().split(",");
            for (int i = 0; i < childJobIds.length; i++) {
                int childJobId = (childJobIds[i]!=null && childJobIds[i].trim().length()>0&& isNumeric(childJobIds[i]))?Integer.valueOf(childJobIds[i]):-1;
            }

            if (childJobId>0){
                JobTriggerPoolHelper.trigger(childJobIds,TriggerTypeEnum.PARENT,-1,null,null,null);
                ReturnT<String> triggerChildResult = ResturnT.SUCCESS;

                triggerChildMsg += MessageFormat.format(I18nUtil.getString("jobconf_callback_child_msg1"),
                        (i+1),
                        childJobIds.length,
                        childJobIds[i],
                        (triggerChildResult.getCode()==ReturnT.SUCCESS_CODE?I18nUtil.getString("system_success"):I18nUtil.getString("system_fail")),
                        triggerChildResult.getMsg());
            } else {
                triggerChildMsg += MessageFormat.format(I18nUtil.getString("jobconf_callback_child_msg2"),
                        (i+1),
                        childJobIds.length,
                        childJobIds[i]);
            }
        }
        if (triggerChildMsg != null){
            xxlJobLog.setHandleMsg(xxlJobLog.getHandleMsg()+triggerChildMsg);
        }
    }

    private static boolean isNumeric(String str){
        try{
            int result = Integer.valueOf(str);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }
}

