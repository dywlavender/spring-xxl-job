package com.xxl.job.executor.service.jobhandler.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @ClassName Util
 * @Description 流水号：时间日期+uuid哈希值
 * @Author dlavender
 * @Date 2022/6/15 8:50
 * @Version 1.0
 **/
public class Util {
    public static void main(String[] args) {
        System.out.println(Util.getSerialNumber());
    }
    public static String getSerialNumber(){
        Integer uuidHashCode = UUID.randomUUID().hashCode();
        if (uuidHashCode < 0){
            uuidHashCode = uuidHashCode * (-1);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmssSSS");
        String date = dateFormat.format(new Date());
        return date + uuidHashCode;
    }
}
