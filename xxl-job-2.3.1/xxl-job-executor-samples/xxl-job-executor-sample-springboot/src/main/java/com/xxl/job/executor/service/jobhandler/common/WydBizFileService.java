package com.xxl.job.executor.service.jobhandler.common;

import com.xxl.job.executor.service.jobhandler.util.DateUtil;
import org.springframework.stereotype.Component;

import javax.xml.crypto.Data;
import java.util.List;

/**
 * @ClassName WydBizFileService
 * @Description TODO
 * @Author dlavender
 * @Date 2022/6/15 15:03
 * @Version 1.0
 **/
@Component
public class WydBizFileService {

    public boolean checkIsExistFile(String batchDate, List<String> checkFileList){
        boolean exists = false;
        String yesterday = DateUtil.yesterday(batchDate);
        String okFileName = String.format("EXT-COOPZIP_%s.OK",yesterday);
        String okFile = checkFileList.stream()
                .filter(x -> okFileName.equals(x))
                .findFirst().orElse(null);
        if(okFile == null){
            exists = true;
        }
        return exists;
    }
}
