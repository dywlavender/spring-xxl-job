package com.xxl.job.executor.service.jobhandler;

import lombok.Data;
import lombok.Getter;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName WYDBizFileEnum
 * @Description TODO
 * @Author dlavender
 * @Date 2022/6/14 19:21
 * @Version 1.0
 **/
@Getter
public enum WYDBizFileEnum {
    WYD_ZIP_FILE_OK("微业贷ok文件","EAST","wyd_zip_file_ok","");
    private String desc;
    private String fileFolder;
    private String fileType;
    private String tableName;
    WYDBizFileEnum(String desc,String fileFolder,String fileType,String tableName){
        this.desc = desc;
        this.fileFolder = fileFolder;
        this.fileType = fileType;
        this.tableName = tableName;
    }
    public static List<String> getToDataFileTypeList(){
        return Arrays.stream(WYDBizFileEnum.values())
                .filter(x -> !WYD_ZIP_FILE_OK.equals(x))
                .map(WYDBizFileEnum::getFileType)
                .collect(Collectors.toList());
    }
}
