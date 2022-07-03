package com.xxl.job.executor.service.jobhandler;

import com.xxl.job.executor.service.jobhandler.dao.AcpDownloadFileDao;
import com.xxl.job.executor.service.jobhandler.dao.CommonTaskDao;
import com.xxl.job.executor.service.jobhandler.dao.CommonTaskDetailDao;
import com.xxl.job.executor.service.jobhandler.entity.CommonTask;
import com.xxl.job.executor.service.jobhandler.util.Util;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName TaskService
 * @Description TODO
 * @Author dlavender
 * @Date 2022/6/14 18:56
 * @Version 1.0
 **/
public class TaskService {
    @Autowired
    private AcpDownloadFileDao acpDownloadFileDao;
    @Autowired
    private CommonTaskDetailDao commonTaskDetailDao;
    @Autowired
    private CommonTaskDao commonTaskDao;
    @Autowired
    private TransactionTemplate transactionTemplate;

    public void updateTaskStatus(String taskId,String status,String errorMsg,String targetId){
        CommonTask commonTask = new CommonTask();
        commonTask.setStatus(status);
        commonTask.setId(taskId);
        commonTask.setErrorMsg(errorMsg);
        commonTask.setTargetId(targetId);
        commonTask.setUpdateTime(new Date());
        commonTaskDao.updataById(commonTask);
    }
    public CommonTask queryTask(String batchDate, String taskType){
        Map<String,Object> taskParams = new HashMap<>();
        taskParams.put("batchDate",batchDate);
        taskParams.put("taskType",taskType);
        CommonTask commonTask = commonTaskDao.findByBatchDateAndType(taskParams);
        return commonTask;
    }
    public CommonTask initCommonTask(String batchDate, String targetType){
        CommonTask commonTask = queryTask(batchDate,targetType);
        if(commonTask == null){
            commonTask = new CommonTask();
            commonTask.setId(Util.getSerialNumber());
            commonTask.setBatchDate(batchDate);
            commonTask.setCreateTime(new Date());
            commonTask.setUpdateTime(new Date());
            commonTask.setTargetType(targetType);
            commonTask.setStatus("0");
            commonTaskDao.create(commonTask);
        }
        return commonTask;
    }
    public void deleteInitForDownLoadFile(String batchDate){
        transactionTemplate.execute( e -> {
            Map<String,Object> detailParam = new HashMap<>();
            detailParam.put("batchDate",batchDate);
            detailParam.put("detailTypeGroup",0);
            commonTaskDetailDao.deleteByBatchDateAndType(detailParam);

            Map<String,Object> taskParams = new HashMap<>();
            taskParams.put("batchDate",batchDate);
            taskParams.put("targetType",0);
            commonTaskDao.deleteByBatchDateAndType(detailParam);

            Map<String,Object> downFile = new HashMap<>();
            downFile.put("batchDate",batchDate);
            downFile.put("fileTypeList",WYDBizFileEnum.getToDataFileTypeList());
            acpDownloadFileDao.deleteAccountFileByBatchDate(downFile);

        });
    }
}
