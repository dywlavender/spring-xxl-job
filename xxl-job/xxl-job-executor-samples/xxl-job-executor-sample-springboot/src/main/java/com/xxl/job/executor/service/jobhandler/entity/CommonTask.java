package com.xxl.job.executor.service.jobhandler.entity;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName CommonTask
 * @Description 公共任务的实体类
 * @Author dlavender
 * @Date 2022/6/14 18:42
 * @Version 1.0
 **/
@Data
public class CommonTask {
    private String id;
    /**
     * 任务类型
     */
    private String targetType;
    /**
     * 任务类型对应的主键
     */
    private String targetId;
    /**
     * 下载状态
     */
    private String status;
    private String errorMsg;
    private String batchDate;
    private Date createTime;
    private Date updateTime;

}
