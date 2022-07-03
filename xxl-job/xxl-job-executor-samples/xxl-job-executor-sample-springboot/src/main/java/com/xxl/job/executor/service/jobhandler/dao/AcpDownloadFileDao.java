package com.xxl.job.executor.service.jobhandler.dao;

import com.xxl.job.executor.service.jobhandler.entity.AcpDownloadFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @ClassName AcpDownloadFileDao
 * @Description TODO
 * @Author dlavender
 * @Date 2022/6/14 18:21
 * @Version 1.0
 **/
public interface AcpDownloadFileDao {
    int deleteByPrimaryKey(BigDecimal id);
    int insert(AcpDownloadFile record);
    int deleteAccountFileByBatchDate(Map map);

    List<AcpDownloadFile> selectUndoneWsdFile(Map<String, Object> queryCondition);

    void updateByConsumerSeqNo(AcpDownloadFile acpDownloadFile);
}
