package com.xxl.job.executor.service.jobhandler;

import com.alibaba.fastjson.JSON;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.executor.service.jobhandler.common.CommonFileDownload;
import com.xxl.job.executor.service.jobhandler.common.WydBizFileService;
import com.xxl.job.executor.service.jobhandler.dao.AcpDownloadFileDao;
import com.xxl.job.executor.service.jobhandler.entity.*;
import com.xxl.job.executor.service.jobhandler.util.DateUtil;
import com.xxl.job.executor.service.jobhandler.util.Util;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.*;

/**
 * @ClassName WYDBizFileDownloadServiceImpl
 * @Description TODO
 * @Author dlavender
 * @Date 2022/6/14 17:17
 * @Version 1.0
 **/
public class WYDBizFileDownloadServiceImpl{

    @Autowired
    private CommonFileDownload commonFileDownload;
    @Autowired
    private TaskService taskService;
    @Autowired
    private WydBizFileService wydBizFileService;
    @Autowired
    private AcpDownloadFileDao acpDownloadFileDao;
    @Autowired
    private CommInf commInf;

    public ReturnT<String> excute(String param){
        WYDBizFileDownTaskParam wydBizFileDownTaskParam = JSON.parseObject(param, WYDBizFileDownTaskParam.class);
        if (wydBizFileDownTaskParam.isDeleteFlag()){
            taskService.deleteInitForDownLoadFile(wydBizFileDownTaskParam.getBatchDate());
        }
        //初始化任务表
        CommonTask commonTask = taskService.initCommonTask(wydBizFileDownTaskParam.getBatchDate(), "0");
        //如果正在下载或下载完成则退出
        if (!"0".equals(commonTask.getStatus())){
            return new ReturnT<>();
        }
        //检查OK文件和zip文件是否准备好
        List<String> checkFileList = queryCheckFile(wydBizFileDownTaskParam);
        if(!wydBizFileService.checkIsExistFile(wydBizFileDownTaskParam.getBatchDate(), checkFileList)){
            return new ReturnT<>();
        }
        String yesterday = DateUtil.yesterday(wydBizFileDownTaskParam
                .getBatchDate());
        String zipFileNameString = String.format("EXT-COOPEZIP_%s.zip",yesterday);
        String zipFileName = checkFileList.stream().filter(x->zipFileNameString.equals(x))
                .findFirst().orElse(null);
        if(zipFileName == null){
            taskService.updateTaskStatus(commonTask.getId(),"30","未找到微众压缩文件",null);
            return new ReturnT<>(500,null);
        }
        //zip文件记录到数据库中
        saveFilePath(zipFileName,wydBizFileDownTaskParam.getBatchDate());

        //查询apcdownloadfile表中当日的数据，下载状态不为1的
        Map<String,Object> queryCondition = new HashMap<>();
        queryCondition.put("fileTypeList", Collections.singleton("wyd_biz_file"));
        queryCondition.put("batchDate",wydBizFileDownTaskParam.getBatchDate());
        List<AcpDownloadFile> downloadFileList = acpDownloadFileDao.selectUndoneWsdFile(queryCondition);
        if(downloadFileList.size()>0){
            AcpDownloadFile acpDownloadFile = downloadFileList.get(0);
            //组装请求
            OutFolderTraverseEsbRequst outFolderTraverseEsbRequst = new OutFolderTraverseEsbRequst();
            CommInf commInf = new CommInf();
            commInf.setFilePath(acpDownloadFile.getSrcPath());
            List<Map<String,Object>> array = new ArrayList<>();
            array.add(BeanUtil.bean2Map(commInf));
            outFolderTraverseEsbRequst.setArray(array);
            outFolderTraverseEsbRequst.setTranDate(new Date().toString());
            outFolderTraverseEsbRequst.setZoneCd("INTERNET");
            outFolderTraverseEsbRequst.setTxnSeqNo(acpDownloadFile.getConsumerSeqNo());
            outFolderTraverseEsbRequst.setCnlInd2("WYD");
            if(!commonFileDownload.commonDownloadV3(outFolderTraverseEsbRequst)){
                taskService.updateTaskStatus(commonTask.getId(),"30","失败",null);
                return new ReturnT<>(500,null);
            }

        }
        taskService.updateTaskStatus(commonTask.getId(),"10","",
                downloadFileList.isEmpty() ? null : downloadFileList.get(0).getId().toString());
        return new ReturnT<>();
    }

    /**
     * 将 zip文件清单 落库
     * @param zipFileName
     * @param batchDate
     */
    private void saveFilePath(String zipFileName, String batchDate) {
        String yesterday = DateUtil.yesterday(batchDate);
        String srcPath = "wydDataFileSrcPath" + yesterday + File.separator + "EAST"
                + File.separator + zipFileName;
        String destPath = "wydBizFileDestPath" + File.separator + zipFileName;
        AcpDownloadFile acpDownloadFile = new AcpDownloadFile();
        acpDownloadFile.setBatchDate(batchDate);
        acpDownloadFile.setFileType("wyd_biz_file");
        acpDownloadFile.setSrcPath(srcPath);
        acpDownloadFile.setDestPath(destPath);
        acpDownloadFile.setDownloadStatus("0");
        acpDownloadFile.setCreateTime(new Date());
        acpDownloadFile.setConsumerSeqNo(Util.getSerialNumber());
        acpDownloadFileDao.insert(acpDownloadFile);
    }

    /**
     * 查询ok文件是否上传
     * @param wydBizFileDownTaskParam
     * @return
     */
    public List<String> queryCheckFile(WYDBizFileDownTaskParam wydBizFileDownTaskParam){
        // batchDate 前一天
        String yesterday = wydBizFileDownTaskParam.getBatchDate();
        String srcPath = "wydDataFileSrcPath" + yesterday +  "/wyd_biz_file";
        OutFolderTraverseEsbRequst outFolderTraverseEsbRequst = new OutFolderTraverseEsbRequst();
        outFolderTraverseEsbRequst.setZoneCd("INTERNET");
        outFolderTraverseEsbRequst.setTranDate(wydBizFileDownTaskParam.getBatchDate());
        outFolderTraverseEsbRequst.setTxnSeqNo(Util.getSerialNumber());
        outFolderTraverseEsbRequst.setCnlInd2("WYD");
        outFolderTraverseEsbRequst.setFilePath3(srcPath);
        return commonFileDownload.queryCommonFileV2(outFolderTraverseEsbRequst);
    }
}
