package com.xxl.job.executor.service.jobhandler.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @ClassName OutFolderTraverseEsbRequst
 * @Description TODO
 * @Author dlavender
 * @Date 2022/6/15 14:28
 * @Version 1.0
 **/
@Data
public class OutFolderTraverseEsbRequst {
    private String consumerId;
    private String zoneCd;
    private String tranDate;
    private String txnSeqNo;
    private String cnlInd2;
    private String filePath3;
    private List<Map<String,Object>> array;
}
