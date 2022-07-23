package com.xxl.job.executor.service.jobhandler.util;

import com.alibaba.fastjson.JSON;
import com.google.gson.annotations.JsonAdapter;
import com.itextpdf.text.pdf.PdfReader;
import sun.nio.cs.UTF_32LE;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName test01
 * @Description TODO
 * @Author dlavender
 * @Date 2022/7/19 17:30
 * @Version 1.0
 **/
public class test01 {
    public static void main(String[] args) throws IOException {

        // HashMap<String,Object> map = new HashMap<>();
        // map.put("a","b");
        // map.put("c","d");
        // String s = JSON.toJSONString(map);
        // System.out.println("map:"+s);
        //
        // String str = "{\"a\":\"b\"}";
        // System.out.println("str: "+str);
        // Object o = JSON.parseObject(str, Map.class);
        // System.out.println(o.toString());
        // String str = "123";
        // byte[] b = str.getBytes("UTF-8");
        // System.out.println(b);
        String str = " , ";
        String[] s = str.split(",");
        for (String ss:s){
            ss = ss.trim();

            System.out.println(ss.equals(""));
        }
    }
}
