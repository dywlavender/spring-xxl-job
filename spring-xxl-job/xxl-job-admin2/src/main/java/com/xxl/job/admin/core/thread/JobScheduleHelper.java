package com.xxl.job.admin.core.thread;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName JobScheduleHelper
 * @Description TODO
 * @Author dlavender
 * @Date 2022/7/7 8:35
 * @Version 1.0
 **/
public class JobScheduleHelper {
    private static Logger logger = LoggerFactory.getLogger(JobScheduleHelper.class);
    private static JobScheduleHelper instance = new JobScheduleHelper();
    public static JobScheduleHelper getInstance(){return instance;}
    public static final long PRE_READ_MS = 500;
    private Thread scheduleThread;
    private Thread ringThread;
    private volatile boolean scheduleThreadToStop = false;
    private volatile boolean ringThreadToStopo = false;
    private volatile static Map<Integer,List<Integer>> ringData = new ConcurrentHashMap<>();
    public void start(){
        scheduleThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.MILLISECONDS.sleep(5000 - System.currentTimeMillis()%1000);
                } catch (InterruptedException e) {
                    if (!scheduleThreadToStop){
                        logger.error(e.getMessage(),e);
                    }
                }
                int preReadCount = (XxlJobAdminConfig.getAdminConfig().getTriggerPoolFastMax() +
                        XxlJobAdminConfig.getAdminConfig().getTriggerPoolSlowMax()) * 20;

                while(!scheduleThreadToStop){
                    long start = System.currentTimeMillis();
                    Connection conn = null;
                    Boolean connAutoCommit = null;
                    PreparedStatement preparedStatement = null;
                    boolean preReadSuc = true;
                    try{
                        conn = XxlJobAdminConfig.getAdminConfig().getDataSource().getConnection();
                        connAutoCommit = conn.getAutoCommit();
                        conn.setAutoCommit(false);

                        preparedStatement = conn.prepareStatement("select * from xxl_job_lock where lock_name = 'schedule_lock' for update");
                        preparedStatement.execute();

                        long nowTime = System.currentTimeMillis();
                        List<XxlJobInfo> scheduleList = XxlJobAdminConfig.getAdminConfig().getXxlJobInfDao().scheduleJobQuery(nowTime+PRE_READ_MS,preReadCount);
                        if (scheduleList != null && scheduleList.size()>0){
                            for (XxlJObInfo jobInfo:scheduleList){
                                if(nowTime > jobInfo.getTriggerNextTime()+PRE_READ_MS){
                                    logger.warn(jobInfo.getId());
                                    MisfireStrategyEnum misfireStrategyEnum = MisfireStrategyEnum.match(jobInfo.getMinfireStrategy(),MisfireStrategyEnum.DO_NOTHING);
                                    if (MisfireStrategyEnum.FIRE_ONCE_NOW == misfireStrategyEnum){
                                        JobTriggerPoolHelper.trigger(jobInfo.getId(),TriggerTypeEnum.MISFIRE,-1,null,null,null);
                                        logger.debug(jobInfo.getId());
                                    }

                                    refreshNextValidTime(jobInfo,new Date());
                                }else if(nowTime>jobInfo.getTriggerNextTime()){
                                    JobTriggerPoolHelper.trigger(jobInfo.getId(),TriggerTypeEnum.CRON,-1,null,null,null);
                                    logger.debug(jobInfo.getId());
                                    refreshNextValidTime(jobInfo,new Date());
                                }
                            }
                        }

                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        })
    }
}
