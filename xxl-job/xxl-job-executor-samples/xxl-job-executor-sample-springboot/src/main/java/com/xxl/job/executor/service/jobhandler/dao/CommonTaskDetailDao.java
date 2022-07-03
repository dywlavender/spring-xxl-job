package com.xxl.job.executor.service.jobhandler.dao;

import java.util.Map;

/**
 * @ClassName CommonTaskDetailDao
 * @Description TODO
 * @Author dlavender
 * @Date 2022/6/14 18:39
 * @Version 1.0
 **/
public interface CommonTaskDetailDao {
    void deleteByBatchDateAndType(Map<String,Object> params);
}
