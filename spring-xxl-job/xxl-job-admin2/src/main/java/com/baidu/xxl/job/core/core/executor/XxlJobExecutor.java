package com.baidu.xxl.job.core.core.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dyw
 * @date 2022-07-23  17:53
 */
public class XxlJobExecutor {
    private static final Logger logger = LoggerFactory.getLogger(XxlJobExecutor.class);

    private String adminAddress;
    private String accessToken;
    private String appname;
    private String address;
    private String ip;
    private int port;
    private String logPath;
    private int logRetentionDays;

    public void setAdminAddress(String adminAddress) {
        this.adminAddress = adminAddress;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public void setLogRetentionDays(int logRetentionDays) {
        this.logRetentionDays = logRetentionDays;
    }

    public  void start() throws Exception{
        //init logpath
        XxlJobFileAppender.initLogPath(logPath);

        //init invoker,admin-client
        initAdminBizList(adminAddress,accessToken);

        //init
        JobLogFileCleanThread.getInstance().start();

        //init
        TriggerCallbackThread.getInstance().start();

        initEmbedServer(address,ip,port,appname,accessToken);
    }


}
