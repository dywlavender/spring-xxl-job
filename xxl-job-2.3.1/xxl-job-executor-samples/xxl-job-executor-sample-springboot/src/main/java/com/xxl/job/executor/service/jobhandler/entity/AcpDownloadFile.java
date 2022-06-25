package com.xxl.job.executor.service.jobhandler.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName AcpDownloadFile
 * @Description TODO
 * @Author dlavender
 * @Date 2022/6/14 18:23
 * @Version 1.0
 **/
@Data
public class AcpDownloadFile {
    private BigDecimal id;
    private String srcPath;
    private String destPath;
    private String downloadStatus;
    private String uploadStatus;
    private String fileType;
    private Date createTime;
    private Date updateTime;
    private String batchDate;
    private String errorMsg;
    private String consumerSeqNo;

}

enum FileType{
    ACCOUNTZIP("-1","账务文件压缩表",true);
    String code;
    String desc;
    boolean isMessage;
    FileType(String code, String desc, boolean isMessage){
        this.code = code;
        this.desc = desc;
        this.isMessage = isMessage;
    }
}
