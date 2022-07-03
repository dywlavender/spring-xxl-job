package com.xxl.job.core.executor.impl;

import com.xxl.job.core.executor.XxlJobExector;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author dyw
 * @date 2022-07-03  21:50
 */
public class XxlJobSpringExecutor  extends XxlJobExector implements ApplicationContextAware, SmartInitializingSingleton {
    @Override
    public void afterSingletonsInstantiated() {
        initJobHandlerMethodRepository(applicationContext);
        try{
            super.start();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private void initJobHandlerMethodRepository(ApplicationContext applicationContext) {
        if (applicationContext == null){
            return;
        }
        String[] beanDefinitionNames = applicationContext.getBeanNamesForType(Object.class,false,true);
        for (String beanDefinitionName: beanDefinitionNames){
            Object bean = applicationContext.getBean(beanDefinitionName);
            Map<Method,XxlJob> annotateMethods = null;
            try{
                annotateMethods = MethodIntrospector.selectMethods(bean.getClass(),
                        new MethodIntrospector.MetadataLookup<XxlJob>(){
                            @Override
                            public XxlJob inspect(Method method) {
                                return AnnotatedElementUtils.findMergedAnnotation(method,XxlJob.class);
                            }
                        });
            }catch (Throwable throwable){

            }
            if(annotateMethods == null || annotateMethods.isEmpty()){
                continue;
            }
            for (Map.Entry<Method,XxlJob> methodXxlJobEntry: annotateMethods.entrySet()){
                Method executorMethod = methodXxlJobEntry.getKey();
                XxlJob xxlJob = methodXxlJobEntry.getValue();
                registJobHandler(xxlJob,bean,executorMethod);
            }

        }
    }


    private static ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        XxlJobSpringExecutor.applicationContext = applicationContext;
    }


}
