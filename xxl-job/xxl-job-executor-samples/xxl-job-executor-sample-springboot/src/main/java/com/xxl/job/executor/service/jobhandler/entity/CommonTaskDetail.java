package com.xxl.job.executor.service.jobhandler.entity;

import lombok.Data;

/**
 * @ClassName CommonTaskDetail
 * @Description TODO
 * @Author dlavender
 * @Date 2022/6/14 18:53
 * @Version 1.0
 **/
@Data
public class CommonTaskDetail {
    private String id;
    private String taskId;
    private String detailType;
    private String status;
    private String batchDate;
    private Data createTime;
    private Data updateTime;
}
