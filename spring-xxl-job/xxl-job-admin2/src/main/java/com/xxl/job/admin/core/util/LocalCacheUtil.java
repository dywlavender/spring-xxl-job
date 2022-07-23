package com.xxl.job.admin.core.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @ClassName LocalCacheUtil
 * @Description TODO
 * @Author dlavender
 * @Date 2022/7/21 18:48
 * @Version 1.0
 **/
public class LocalCacheUtil {
    private static ConcurrentMap<String,LocalCacheData> cacheRepository = new ConcurrentHashMap<String,LocalCacheData>();
    private static class LocalCacheData{
        private String key;
        private Object val;
        private long timeooutTime;

        public LocalCacheData(){

        }

        public LocalCacheData(String key, Object val, long timeooutTime) {
            this.key = key;
            this.val = val;
            this.timeooutTime = timeooutTime;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public Object getVal() {
            return val;
        }

        public void setVal(Object val) {
            this.val = val;
        }

        public long getTimeooutTime() {
            return timeooutTime;
        }

        public void setTimeooutTime(long timeooutTime) {
            this.timeooutTime = timeooutTime;
        }
    }


    public static boolean set(String key,Object val,long cacheTime){
        cleanTimeoutCache();

        if (key==null || key.trim().length()==0){
            return false;
        }
        if (val == null){
            remove(key);
        }
        if (cacheTime<=0){
            remove(key);
        }
        long timeoutTime = System.currentTimeMillis()+cacheTime;
        LocalCacheData localCacheData = new LocalCacheData(key,val,timeoutTime);
        cacheRepository.put(localCacheData.key, localCacheData);
        return true;
    }

    public static boolean remove(String key){
        if (key==null||key.trim().length()==0){
            return false;
        }
        cacheRepository.remove(key);
        return true;
    }

    public static Object get(String key){
        if (key==null||key.trim().length()==0) {
            return null;
        }
        LocalCacheData localCacheData = cacheRepository.get(key);
        if (localCacheData!=null&& System.currentTimeMillis()<localCacheData.getTimeooutTime()){
            return localCacheData.getVal();
        }else{
            remove(key);
            return null;
        }

    }

    public static boolean cleanTimeoutCache(){
        if (!cacheRepository.keySet().isEmpty()){
            for (String key: cacheRepository.keySet()){
                LocalCacheData localCacheData = cacheRepository.get(key);
                if (localCacheData!=null&&System.currentTimeMillis()>=localCacheData.getTimeooutTime()){
                    cacheRepository.remove(key);
                }
            }
        }
        return true;
    }


}
