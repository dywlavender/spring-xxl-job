package com.xxl.job.executor.service.jobhandler.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @ClassName DateUtil
 * @Description TODO
 * @Author dlavender
 * @Date 2022/6/15 15:10
 * @Version 1.0
 **/
public class DateUtil {
    public static String yesterday(String today){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        try {
            Date date = format.parse(today);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE,-1);
            return format.format(calendar.getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
