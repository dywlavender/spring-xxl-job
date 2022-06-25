package com.xxl.job.executor.service.jobhandler.entity;

import lombok.Data;

/**
 * @ClassName WYDBizFileDownTaskParam
 * @Description TODO
 * @Author dlavender
 * @Date 2022/6/15 9:12
 * @Version 1.0
 **/
@Data
public class WYDBizFileDownTaskParam {
    private String batchDate;
    private String waitTime;
    private String deadline;
    private int downHours;
    private boolean deleteFlag;
}
