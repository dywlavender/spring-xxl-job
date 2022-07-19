package com.xxl.job.admin.core.conf;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @ClassName XxlJobAdminConfig
 * @Description TODO
 * @Author dlavender
 * @Date 2022/7/19 15:08
 * @Version 1.0
 **/
@Component
public class XxlJobAdminConfig implements InitializingBean, DisposableBean {



    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
