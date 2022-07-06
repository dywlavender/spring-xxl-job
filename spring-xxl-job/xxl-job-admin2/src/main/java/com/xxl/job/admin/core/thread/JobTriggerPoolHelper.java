package com.xxl.job.admin.core.thread;

import com.xxl.job.admin.core.trigger.XxlJobTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName JobTriggerPoolHelper
 * @Description TODO
 * @Author dlavender
 * @Date 2022/7/6 8:50
 * @Version 1.0
 **/
public class JobTriggerPoolHelper {
    private static final Logger logger = LoggerFactory.getLogger(JobTriggerPoolHelper.class);

    //快慢线程池
    private ThreadPoolExecutor fastTriggerPool = null;
    private ThreadPoolExecutor slowTriggerPool = null;
    private volatile long minTim = System.currentTimeMillis()/60000;
    private volatile ConcurrentMap<Integer, AtomicInteger> jobTimeoutCountMap = new ConcurrentHashMap<>();
    public void start(){
        fastTriggerPool = new ThreadPoolExecutor(10, 200, 60L, TimeUnit.SECONDS,
                        new LinkedBlockingDeque<Runnable>(1000),
                        new ThreadFactory() {
                            @Override
                            public Thread newThread(Runnable r) {
                                return new Thread(r, "xxl-job admin jobTriggerPoolHelper fastTriggerPool - " + r.hashCode());
                            }
                        });
        slowTriggerPool = new ThreadPoolExecutor(10, 100, 60L, TimeUnit.SECONDS,
                new LinkedBlockingDeque<Runnable>(2000),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r,"xxl-job admin jobTriggerPoolHelper slowTriggerPool - "+r.hashCode());
                    }
                });
    }

    //-------helper----
    private static JobTriggerPoolHelper helper = new JobTriggerPoolHelper();
    public void toStart(){helper.start();}

    public static void trigger(int jobId,TriggerTypeEnum triggerType,int failRetryCount, String executorShardingParam, String executorParam,String addressList){
        helper.addTrigger(jobId,triggerType,failRetryCount,executorShardingParam,executorParam,addressList);

    }

    public void addTrigger(final int jobId,
                           final TriggerTypeEnum triggerType,
                           final int failRetryCount,
                           final String executorShardingParam,
                           final String executorParam,
                           final String addressList){
        ThreadPoolExecutor triggerPool = fastTriggerPool;
        AtomicInteger jobTimeoutCount = jobTimeoutCountMap.get(jobId);
        if (jobTimeoutCount != null && jobTimeoutCount.get()>10){
            triggerPool = slowTriggerPool;
        }

        triggerPool.execute(new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                try {
                    XxlJobTrigger.trigger(jobId,triggerType,failRetryCount,executorShardingParam,
                            executorParam,addressList);
                }catch (Exception e){
                    logger.error(e.getMessage(),e);
                }finally {
                    //只统计同一分钟内的
                    long minIm_now = System.currentTimeMillis();
                    if(minTim != minIm_now){
                        minTim = minIm_now;
                        jobTimeoutCountMap.clear();
                    }
                    long cost = System.currentTimeMillis() - start;
                    if (cost > 500){
                        //如果存在 直接返回原有的value，不存在就初始化1，然后返回null
                        AtomicInteger timeoutCount = jobTimeoutCountMap.putIfAbsent(jobId,new AtomicInteger(1));
                        if (timeoutCount != null){
                            timeoutCount.incrementAndGet();
                        }
                    }
                }
            }
        });
    }
}
