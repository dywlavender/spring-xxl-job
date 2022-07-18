package com.xxl.job.admin.controller;

import com.xxl.job.admin.controller.anotation.PermissionLimit;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.util.GsonTool;
import io.netty.util.internal.SuppressJava6Requirement;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @ClassName JobApiController
 * @Description TODO
 * @Author dlavender
 * @Date 2022/7/18 12:42
 * @Version 1.0
 **/
@Controller
@RequestMapping("/api")
public class JobApiController {
    @Resource
    private AdminBiz adminBiz;
    @RequestMapping("/{uri}")
    @ResponseBody
    @PermissionLimit(limit = false)
    public ReturnT<String> api(HttpServletRequest request, @PathVariable("uri") String uri, @RequestBody(required = false) String date){
        //valid
        if (!"POST".equalsIgnoreCase(request.getMethod())){
            return new ReturnT<String>(ReturnT.FAIL_CODE,"invalid request, HttpMethod not support");
        }

        if ("callback".equals(uri)) {
            List<HandleCallbackParam> callbackParamList = GsonTool.fromJson(data, List.class,HandleCallbackParam.class);
            return adminBiz.callback(callbackParamList);
        } else if ("registry".equals(uri)) {
            RegistryParam registryParam = GsonTool.fromJson(data,RegistryParam.class);
            return adminBiz.registry(registryParam);
        } else if ("registryRemove".equals(uri)) {
            RegistryParam registryParam = GsonTool.fromJson(data,RegistryParam.class);
            return adminBiz.registryRemove(registryParam);
        }else {
            return new ReturnT<String>(ReturnT.FAIL_CODE,"invalid request,uri-mapping(" + uri +") not found.");
        }
    }
}
