package com.xxl.job.executor.service.jobhandler.common;

import com.sun.corba.se.spi.orbutil.fsm.FSM;
import com.xxl.job.executor.service.jobhandler.dao.AcpDownloadFileDao;
import com.xxl.job.executor.service.jobhandler.entity.AcpDownloadFile;
import com.xxl.job.executor.service.jobhandler.entity.OutFolderTraverseEsbRequst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName CommonFileDownload
 * @Description TODO
 * @Author dlavender
 * @Date 2022/6/15 14:37
 * @Version 1.0
 **/
@Component
public class CommonFileDownload {
    @Autowired
    private HttpTransport httpTransport;
    @Autowired
    private AcpDownloadFileDao acpDownloadFileDao;

    public List<String> queryCommonFileV2(OutFolderTraverseEsbRequst requst){
        //组报文
        Map reqBody = BeanUtils.bean2Map(requst);
        //三要素
        reqBody.put("channelCode","BLM");
        reqBody.put("productCode","00023");
        reqBody.put("transCode","outFolderTraverse");

        FsgContext fsgContext = new FsgContext(null,reqBody);
        fsgContext.setData("reqBody",reqBody);
        //发送请求
        Map resBody = httpTransport.submit(fsgContext);
        List headList = (List) resBody.get("headList");
        Map returnMap = (Map) headList.get(0);
        String returnCode = (String) returnMap.get("returnCode");
        ArrayList fileList = new ArrayList();
        if("000000".equals(returnCode)){
            List<Map<String ,Object>> tempList = (List<Map<String, Object>>) resBody.get("rlist");
            if(tempList != null) {
                for (Map file :
                        tempList) {
                    String fileName = (String) file.get("remoteFilePath");
                    fileList.add(fileName.substring(fileName.lastIndexOf("/") + 1));
                }
            }
        }
        return fileList;
    }

    public boolean commonDownloadV3(OutFolderTraverseEsbRequst outFolderTraverseEsbRequst){
        Map reqBody = BeanUtils.bean2Map(outFolderTraverseEsbRequst);
        reqBody.put("channelCode","BLM");
        reqBody.put("productCode","00023");
        reqBody.put("transCode","outFileDownloadBank");
        FsgContext fsgContext = new FsgContext(null,reqBody);
        fsgContext.setData("reqBody",reqBody);
        AcpDownloadFile acpDownloadFile = new AcpDownloadFile();
        acpDownloadFile.setConsumerSeqNo(outFolderTraverseEsbRequst.getTxnSeqNo());
        boolean result = true;
        try{
            for(int i = 0;i<3;i++){
                Map resBody = httpTransport.submit(fsgContext);
                List headList = (List) resBody.get("headList");
                Map returnMap = (Map) headList.get(0);
                String returnCode = (String) returnMap.get("returnCode");
                if("000000".equals(returnCode)){
                    acpDownloadFile.setDownloadStatus("0");
                    acpDownloadFileDao.updateByConsumerSeqNo(acpDownloadFile);
                    result = true;
                    break;
                }else {
                    acpDownloadFile.setDownloadStatus("30");
                    acpDownloadFileDao.updateByConsumerSeqNo(acpDownloadFile);
                    result = false;
                }
            }
            return result;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
