package com.xxl.job.admin.core.thread;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
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
    private volatile boolean ringThreadToStop = false;
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

                                    if (jobInfo.getTriggerStatus()==1 && nowTime+PRE_REAN_MS>jobInfo.getTriggerNextTime()){
                                        int ringSecode = (int) ((jobInfo.getTriggerNextTime()/1000)%60);
                                        pushTimeRing(ringSecode,jobInfo.getId());
                                        refreshNextVailTime(jobInfo,new Date(jobInfo.getTriggerNextTime()));
                                    }
                                }else{
                                    int ringSecod = (int) ((jobInfo.getTriggerNextTime()/1000)%60);
                                    pushTimeRing(ringSecod,jobInfo.getId());
                                    refreshNextVaildTime(jobInfo,new Date(jobInfo.getTriggerNextTime()));
                                }
                            }

                            for (XxlJobInfo jobInfo:scheduleList){
                                XxlJobAdminConfig.getAdminConfig().getXxlJobInfoDao().scheduleUpdate(jobInfo);
                            }
                        }else {
                            preReadSuc = false;
                        }

                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }finally {
                        if (conn != null){
                            try {
                                conn.commit();
                            } catch (SQLException e) {
                                if (!scheduleThreadToStop){
                                    logger.error(e.getMessage(),e);
                                }
                            }
                            try {
                                conn.setAutoCommit(connAutoCommit);
                            } catch (SQLException e) {
                                if (!scheduleThreadToStop){
                                    logger.error(e.getMessage(),e);
                                }
                            }
                        }
                        if (preparedStatement != null){
                            try {
                                preparedStatement.close();
                            } catch (SQLException e) {
                                if (!scheduleThreadToStop){
                                    logger.error(e.getMessage(),e);
                                }
                            }
                        }
                    }
                    long cost = System.currentTimeMillis()-start;
                    if (cost<1000){
                        try {
                            TimeUnit.MILLISECONDS.sleep((preReadSuc?1000:PRE_READ_MS)-System.currentTimeMillis()%1000);
                        } catch (InterruptedException e) {
                            if (!scheduleThreadToStop){
                                logger.error(e.getMessage(),e);
                            }
                        }

                    }
                }
                logger.info("xxl-job scheduleThread stop");

            }
        });
        scheduleThread.setDaemon(true);
        scheduleThread.setName("xxl-job scheduleThread");
        scheduleThread.start();


        ringThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!ringThreadToStop){
                    try {
                        TimeUnit.MILLISECONDS.sleep(1000-System.currentTimeMillis()%1000);
                    } catch (InterruptedException e) {
                        if (!ringThreadToStop){
                            logger.error(e.getMessage(),e);
                        }
                    }
                    try{
                        List<Integer> ringItemData = new ArrayList<>();
                        int nowSecond = Calendar.getInstance().get(Calendar.SECOND);
                        for (int i = 0; i < 2; i++) {
                            List<Integer> tempData = ringData.remove((nowSecond+60-i)%60);
                            if (tempData != null){
                                ringItemData.addAll(tempData);
                            }
                        }
                        logger.debug("xxl-job time-ring beat: "+nowSecond+"="+Arrays.asList(ringItemData));
                        if (ringItemData.size()>0){
                            for (int jobId:ringItemData){
                                JobTriggerPoolHelper.trigger(jobId,TriggerTypeEnum.CORN,-1,null,null,null);

                            }
                            ringItemData.clear();
                        }
                    }catch (Exception e){
                        if (!ringThreadToStop){
                            logger.error("ringThread error:{}",e);
                        }
                    }

                }
                logger.info("ringThread stop");
            }
        });
        ringThread.setDaemon(true);
        ringThread.setName("ringThread");
        ringThread.start();
    }

    private void refreshNextTimeVaildTime(XxlJobInfo jobInfo,Date fromTime){
        Date nextVaildTime = generateNaxtVaildTime(jobInfo,fromTime);
        if (nextVaildTime!=null){
            jobInfo.setTriggerLastTime(jobInfo.getTriggerNextTime());
            jobInfo.setTriggerNextTime(nextVaildTime.getTime());
        }else {
            jobInfo.setTriggerStatus(0);
            jobInfo.setTriggerLastTime(0);
            jobInfo.setTriggerNextTime(0);
            logger.warn("refreshNextVaildTime fail for job: jobId={}, scheduleTyep={}," +
                    ", scheduleConf={}",jobInfo.getId(),jobInfo.getScheduleType(),jobInfo.getScheduleConf());
        }
    }

    private void pushTimeRing(int ringSecond, int jobId){
        List<Integer> ringItemData = ringData.get(ringSecond);
        if (ringItemData == null){
            ringItemData = new ArrayList<Integer>();
            ringData.put(ringSecond,ringItemData);
        }
        ringItemData.add(jobId);
        logger.debug("schedule push time-ring: {} = {}",ringSecond,Arrays.asList(ringItemData));
    }

    public static Date generateNaxtVaildTime(XxlJobInfo xxlJobInfo,Date fromTime){
        ScheduleTypeEnum scheduleTypeEnum = ScheduleTypeEnum.match(xxlJobInfo.getScheduleType(),null);
        if (ScheduleTypeEnum.CRON == scheduleTypeEnum){
            Date nextVaildTime = new CronExpression(xxlJobInfo.getScheduleConf()).getNextVaildTimeAfter(fromTime);
            return nextVaildTime;
        }else if(ScheduleTypeEnum.FIX_RATE == scheduleTypeEnum){
            return new Date(fromTime.getTime()+Integer.valueOf(xxlJobInfo.getScheduleConf())*1000);
        }
        return null;
    }
}
