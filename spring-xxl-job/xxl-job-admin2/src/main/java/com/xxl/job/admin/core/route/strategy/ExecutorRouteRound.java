package com.xxl.job.admin.core.route.strategy;

import com.xxl.job.admin.core.route.ExecutorRouter;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName ExecutorRouteRound
 * @Description TODO
 * @Author dlavender
 * @Date 2022/7/20 22:21
 * @Version 1.0
 **/
public class ExecutorRouteRound extends ExecutorRouter {
    private static ConcurrentMap<Integer, AtomicInteger> routeCountEachJob = new ConcurrentHashMap<>();
    private static long CACHE_CALID_TIME = 0;
    private static int count(int jobId){
        if (System.currentTimeMillis()>CACHE_CALID_TIME){
            routeCountEachJob.clear();
            CACHE_CALID_TIME = System.currentTimeMillis()+10000*60*60*24;
        }

        AtomicInteger count = routeCountEachJob.get(jobId);
        if (count ==null || count.get()>100000){
            count = new AtomicInteger(new Random().nextInt(100));
        }else {
            count.addAndGet(1);
        }
        routeCountEachJob.put(jobId,count);
        return count.get();
    }
    @Override
    public ReturnT<String> route(TriggerParam triggerParam, List<String> addressList) {
        String address = addressList.get(count(triggerParam.getId())% addressList.size());
        return new ReturnT<String>(address);
    }
}
