package com.xxl.job.core.executor;

import com.xxl.job.core.biz.AdminBiz;
import com.xxl.job.core.biz.client.AdminBizClient;
import com.xxl.job.core.biz.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.XxlJob;
import io.netty.util.NetUtil;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.logging.MemoryHandler;

/**
 * @author dyw
 * @date 2022-07-03  21:54
 */
public class XxlJobExector {
    private static final Logger logger = LoggerFactory.getLogger(XxlJobExector.class);

    private String adminAddresses;
    private String accessToken;
    private String appname;
    private String address;
    private String ip;
    private int port;
    private String logPAth;
    private int logRetentionDays;

    public void setAdminAddresses(String adminAddresses) {
        this.adminAddresses = adminAddresses;
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

    public void setLogPAth(String logPAth) {
        this.logPAth = logPAth;
    }

    public void setLogRetentionDays(int logRetentionDays) {
        this.logRetentionDays = logRetentionDays;
    }

    public void start() throws Exception{
        initAdminBizList(adminAddresses,accessToken);
        TriggerCallbackThread.getInstance().start();
        initEmbedServer(address,ip,port,appname,accessToken);

    }
    public void destory(){

    }

    private static List<AdminBiz> adminBizList;
    private void initAdminBizList(String adminAddresses,String accessToken){
        if(adminAddresses != null && adminAddresses.trim().length()>0){
            for(String address: adminAddresses.trim().split(",")){
                if(address != null && address.trim().length()>0){
                    AdminBiz adminBiz = new AdminBizClient(address.trim(),accessToken);
                    if(adminBizList == null){
                        adminBizList = new ArrayList<AdminBiz>();
                    }
                    adminBizList.add(adminBiz);
                }
            }
        }
    }

    private static ConcurrentHashMap<String,IJobHandler> jobHandlerRepository = new ConcurrentHashMap<String, IJobHandler>();
    public static IJobHandler registJobHandler(String name, IJobHandler jobHandler){
        return jobHandlerRepository.put(name,jobHandler);
    }
    protected void registJobHandler(XxlJob xxlJob, Object bean, Method executorMethod){
        if(xxlJob == null){
            return;
        }
        String name = xxlJob.value();
        Class<?> clazz = bean.getClass();
        String methodName = executorMethod.getName();
        executorMethod.setAccessible(true);
        Method initMethod = null;
        Method destoryMethod = null;
        if (xxlJob.init().trim().length()>0){
            try {
                initMethod = clazz.getDeclaredMethod(xxlJob.init());
                initMethod.setAccessible(true);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }

        }
        if (xxlJob.destory().trim().length()>0){
            try {
                destoryMethod = clazz.getDeclaredMethod(xxlJob.destory());
                destoryMethod.setAccessible(true);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }

        }
        registJobHandler(name,new MethodJobHandler(bean,executorMethod,initMethod,destoryMethod));
    }

    private EmbedServer embedServer = null;
    private void initEmbedServer(String address,String ip,int port,String appname,String accessToken){
        port = port>0 ? port:NetUtil.findAvailablePort(9999);
        ip = (ip != null && ip.trim().length()>0) ? ip : IpUtil.getIp();

        if(address == null || address.trim().length()>0){
            String ip_port_address = IpUtil.getIpPort(ip,port);
            address = "http://{ip_port}/".replace("{ip_port}",ip_port_address);
        }
        if(accessToken == null || accessToken.trim().length()==0){
            logger.warning("., accessToken is empty");
        }
        embedServer = new EmbedServer();
        embedServer.start(address,port,appname,accessToken);
    }
}
