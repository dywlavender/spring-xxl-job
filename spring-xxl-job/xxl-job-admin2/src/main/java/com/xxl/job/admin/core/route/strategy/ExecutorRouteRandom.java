package com.xxl.job.admin.core.route.strategy;

import com.xxl.job.admin.core.route.ExecutorRouter;

import java.util.List;
import java.util.Random;

/**
 * @ClassName ExecutorRouteRandom
 * @Description TODO
 * @Author dlavender
 * @Date 2022/7/19 22:15
 * @Version 1.0
 **/
public class ExecutorRouteRandom extends ExecutorRouter {
    private static Random localRandom = new Random();
    @Override
    public ReturnT<String> route(TriggerParam triggerParam, List<String> addressList) {
        String address = addressList.get(localRandom.nextInt(addressList.size()));
        return ReturnT<String>(address);
    }
}
