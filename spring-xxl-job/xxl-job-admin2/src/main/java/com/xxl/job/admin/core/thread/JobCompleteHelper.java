package com.xxl.job.admin.core.thread;

import com.baidu.xxl.job.core.core.biz.model.HandleCallbackParam;
import com.xxl.job.admin.core.conf.XxlJobAdminConfig;
import com.xxl.job.admin.core.model.XxlJobLog;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * @ClassName JobCompleteHelper
 * @Description TODO
 * @Author dlavender
 * @Date 2022/7/18 14:02
 * @Version 1.0
 **/
public class JobCompleteHelper {
    private static Logger logger = LoggerFactory.getLogger(JobCompleteHelper.class);

    private static JobCompleteHelper instance = new JobCompleteHelper();

    public static JobCompleteHelper getInstance(){return instance;}

    private ThreadPoolExecutor callbackThreadPool = null;
    private Thread monitorThread;

    private volatile boolean toStop = false;

    public void start(){
        callbackThreadPool = new ThreadPoolExecutor(2, 20, 30L, TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(3000),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "xxl-job, admin JobLosedMonitorHelper-callbackThreadPool-" + r.hashCode());
                    }
                },
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        r.run();
                        logger.warn(">>>>> xxl-job,callback too fast, match threadpool rejected handler(run now");
                    }
                });
        monitorThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.MILLISECONDS.sleep(50);
                } catch (InterruptedException e) {
                    if (!toStop){
                        logger.error(e.getMessage(),e);
                    }
                }

                while(!toStop){
                    try{
                        Date losedTime = DateUtil.addMinutes(new Date(),-10);
                        List<long> losedJobIds = XxlJobAdminConfig.getAdminConfig().getXxlJobLogDao().findLostJobIds(losedTime);

                        if (losedJobIds!=null && losedJobIds.size()>0){
                            for (Long logId:losedJobIds){
                                XxlJobLog jobLog = new XxlJobLog();
                                jobLog.setId(logId);
                                jobLog.setHandleTIme(new Date());
                                jobLog.setHandleCode(ReturnT.FAIL_CODE);
                                jobLog.setHandleMsg(I18nUtil.getString("joblog_lost_fail"));
                                XxlJobCompleter.updateHandleInfoAndFinish(jobLog);
                            }
                        }
                    }catch (Exception e){
                        if (!toStop){
                            logger.error(">>>> xxl-job, job fail monitor thread error:{}",e);
                        }
                    }

                    try {
                        TimeUnit.SECONDS.sleep(60);
                    } catch (InterruptedException e) {
                        if (!toStop){
                            logger.error(e.getMessage(),e);
                        }
                    }
                }
                logger.info(">>> xxl-job,JobLosedMonitorHelper stop");
            }
        });
        monitorThread.setDaemon(true);
        monitorThread.setName("xxl-job, admin JobLosedMonitorHelper");
        monitorThread.start();
    }

    public void toStop(){
        toStop = true;
        callbackThreadPool.shutdownNow();
        monitorThread.interrupt();
        try {
            monitorThread.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage(),e);
        }
    }

    public ReturnT<String> callback(List<HandleCallbackParam> callbackParamLit){
        callbackThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                for (HandleCallbackParam handleCallbackParam:callbackParamLit){
                    ReturnT<String> callbackResult = callback(handleCallbackParam);
                    logger.debug(">>>> JobApiController.callback {} ,handleCallbackParam={}",
                            (callbackResult.getCode()==ReturnT.SUCCESS_CODE?"success":"false"),handleCallbackParam);
                }
            }
        });
        return ReturnT.SUCCESS;
    }

    private ReturnT<String> callback(HandleCallbackParam handleCallbackParam){
        XxlJobLog log = XxlJobAdminConfig.getAdminConfig().getXxlJobLogDao().load(handleCallbackParam.getLogID());
        if (log==null){
            return new ReturnT<String>(ReturnT.FAIL_CODE,"log item not found");
        }
        if (log.getHandleCode()>0){
            return new ReturnT<String>(ReturnT.FAIL_CODE,"log repeate callback");
        }

        StringBuffer handleMsg = new StringBuffer();
        if (log.getHandleCode()>0){
            handleMsg.append(log.getHandleMsg()).append("<br>");

        }
        if (handleCallbackParam.getHandleMsg() != null) {
            handleMsg.append(handleCallbackParam.getHandleMsg());
        }

        log.setHandleTime(new Date());
        log.setHandleCode(handleCallbackParam.getHandleCode());
        log.setHandleMsg(handleMsg.toString());
        XxlJobCompleter.updateHandleInfoAndFinish(log);
        return ReturnT.SUCCESS;
    }

}
