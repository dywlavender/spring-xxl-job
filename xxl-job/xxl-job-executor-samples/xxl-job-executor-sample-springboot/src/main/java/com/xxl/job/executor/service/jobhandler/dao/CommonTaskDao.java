package com.xxl.job.executor.service.jobhandler.dao;

import com.xxl.job.executor.service.jobhandler.entity.CommonTask;

import java.util.Map;

/**
 * @ClassName CommonTaskDao
 * @Description TODO
 * @Author dlavender
 * @Date 2022/6/14 18:41
 * @Version 1.0
 **/
public interface CommonTaskDao {
    void deleteByBatchDateAndType(Map<String,Object> params);
    CommonTask findByBatchDateAndType(Map<String,Object> params);

    void create(CommonTask commonTask);

    void updataById(CommonTask commonTask);
}
