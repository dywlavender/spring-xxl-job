package com.xxl.job.admin.core.route.strategy;

import com.xxl.job.admin.core.route.ExecutorRouter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @ClassName ExecutorRouteLRU
 * @Description TODO
 * @Author dlavender
 * @Date 2022/7/20 22:27
 * @Version 1.0
 **/
public class ExecutorRouteLRU extends ExecutorRouter {
    private static ConcurrentMap<Integer, LinkedHashMap<String,String>> jobLRUMap = new ConcurrentHashMap<>();
    private static long CACHE_VAILD_TIME = 0;
    public String route(int jobId,List<String> addressLsit){
        if (System.currentTimeMillis()>CACHE_VAILD_TIME){
            jobLRUMap.clear();
            CACHE_VAILD_TIME = System.currentTimeMillis() + 1000*60*60*24;
        }
        LinkedHashMap<String,String> lruItem = jobLRUMap.get(jobId);

        if (lruItem ==null){
            lruItem = new LinkedHashMap<>(16,0.75f,true);
            jobLRUMap.putIfAbsent(jobId,lruItem);
        }

        for (String address:addressLsit){
            if (!lruItem.containsKey(address)){
                lruItem.put(address,address);
            }
        }

        List<String> delKeys = new ArrayList<>();
        for (String existKey:lruItem.keySet()){
            if (!addressLsit.contains(existKey)){
                delKeys.add(existKey);
            }
        }

        if (delKeys.size()>0){
            for (String delKey:delKeys){
                lruItem.remove(delKey);
            }
        }

        String eldesKey = lruItem.entrySet().iterator().next().getKey();
        String eldestValue = lruItem.get(eldesKey);
        return eldestValue;
    }
    @Override
    public ReturnT<String> route(TriggerParam triggerParam, List<String> addressList) {
        String address = route(triggerParam.getJobId(),addressList);
        return new ReturnT<String>(address);
    }
}
