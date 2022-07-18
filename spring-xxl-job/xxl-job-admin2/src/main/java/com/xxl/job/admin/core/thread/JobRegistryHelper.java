package com.xxl.job.admin.core.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.rmi.registry.Registry;
import java.util.*;
import java.util.concurrent.*;

/**
 * @ClassName JobRegistryHelper
 * @Description TODO
 * @Author dlavender
 * @Date 2022/7/18 16:21
 * @Version 1.0
 **/
public class JobRegistryHelper {
    private static Logger logger = LoggerFactory.getLogger(JobRegistryHelper.class);
    private static JobRegistryHelper instance = new JobRegistryHelper();
    public static JobRegistryHelper getInstance(){return instance;}

    private ThreadPoolExecutor registryOrRemoveThreadPool = null;
    private Thread registryMonitorThread;
    private volatile boolean toStop = false;

    public void start(){
        registryOrRemoveThreadPool = new ThreadPoolExecutor(2, 10, 30L,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(2000),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "xxl-job, admin JobRegistryMonitorHelper-registryOrRemoveThreadPool-" + r.hashCode());
                    }
                },
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        r.run();
                        logger.warn(">>>xxl-job,registry or remove too fast, match threadPool rejected handler(run now)");
                    }
                });
        registryMonitorThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!toStop){
                    try{
                        List<XxlJobGroup> groupList = XxlJobAdminConfig.getAdminConfig().getXxlJobGroupDao().findByAddressType(0);
                        if (groupList!=null && !groupList.isEmpty()){
                            List<Integer> ids = XxlJobAdminConfig.getAdminConfig().getXxlJobRegistryDao().findDead(RegistryConfig.DEAD_TIMEOUT,new Date());
                            if (ids != null && ids.size() > 0) {
                                XxlJobAdminConfig.getAdminConfig().getXxlJObRegistryDao().removeDead(ids);
                            }

                            HashMap<String,List<String>> appAddressMap = new HashMap<>();
                            List<XxlJobRegistry> list = XxlJobAdminConfig.getAdminConfig().getXxlJobRegistryDao().findAll(RegistryConfig.DEAD_TIMEOUT,new Date());
                            if(list!=null){
                                for (XxlJobRegistry item:list){
                                    if (RegistryConfig.RegistType.ExECUTOR.name().equals(item.getRegistryGroup())){
                                        String appname = item.getRegistryKey();
                                        List<String> registryList = appAddressMap.get(appname);
                                        if (registryList == null){
                                            registryList = new ArrayList<>();
                                        }
                                        if (!registryList.contains(item.getRegistryValue())){
                                            registryList.add(item.getRegistryValue());
                                        }
                                        appAddressMap.put(appname,registryList);
                                    }
                                }
                            }

                            for (XxlJobGroup group:groupList){
                                List<String> registryList = appAddressMap.get(group.getAppname());
                                String addressListStr = null;
                                if (registryList!=null && !registryList.isEmpty()){
                                    Collections.sort(registryList);
                                    StringBuilder addressListSB = new StringBuilder();
                                    for (String item:registryList){
                                        addressListSB.append(item).append(",");
                                    }
                                    addressListStr = addressListSB.toString();
                                    addressListStr = addressListStr.substring(0,addressListStr.length()-1);
                                }
                                group.setAddressList(addressListStr);
                                group.setUpdateTime(new Date());
                                XxlJobAdminConfig.getAdminConfig().getXxlJobGroupDao().update(group);
                            }
                        }
                    } catch (Exception e){
                        if (!toStop){
                            logger.error(">>>>xxl-job, job registry monitor thread error:{}",e);
                        }
                    }
                    try {
                        TimeUnit.SECONDS.sleep(REgistryConfig.BEAT_TIMEOUT);
                    } catch (InterruptedException e){
                        if (!toStop){
                            logger.error(">>> xxl-job, job reegistry monitor thread error:{}",e);
                        }
                    }
                }
                logger.info(">>> xxl-job,job registry monitor thread ");
            }
        });
        registryMonitorThread.setDaemon(true);
        registryMonitorThread.setName("xxl-job, admin JobRegistryMonitor");
        registryMonitorThread.start();
    }

    public void toStop(){
        toStop = true;
        registryOrRemoveThreadPool.shutdownNow();
        registryMonitorThread.interrupt();
        try {
            registryMonitorThread.join();
        } catch (InterruptedException e) {
            logger.error(e.getMessage(),e);
        }
    }

    public ReturnT<String> registry(RegistryParam registryParam){
        if (!StringUtils.hasText(registryParam.getRegistryGroup())||
                !StringUtils.hasText(registryParam.getRegistryKey())
                || !StringUtils.hasText(registryParam.getRegistryValue())){
            return new ReturnT<String>(ReturnT.FAIL_CODE,"Illegal Argument.");
        }

        registryOrRemoveThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                int ret = XxlJobAdminConfig.getAdminConfig().getXxlJobRegistryDao().registryUpdate(registryParam.getRegistryGroup(),registryParam.getRegistryKey(),registryParam.getRegistryValue(),new Date());
                if (ret<1){
                    XxlJobAdminConfig.getAdminConfig().getXxlJobRegistryDao().registrySave(registryParam.getRegistryGroup(),registryParam.getRegistryKey(),registryParam.getRegistryvalue(),new Date());
                    freshGroupRegistryInfo(registryParam);
                }
            }
        });
        return ReturnT.SUCCESS;
    }

    private void freshGroupRegistryInfo(RegistryParam registryParam){

    }
}
