package com.xxl.job.admin.core.util;

import com.xxl.job.core.util.GsonTool;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * @author dyw
 * @date 2022-07-06  22:45
 */
public class XxlJobRemotingUtil {

    public static ReturnT postBody(String url,String accessToken,int timeout,Object requestObj,Class returnTargClassOfT){
        HttpURLConnection connection = null;
        BufferedReader bufferedReader = null;
        try{
            URL realUrl = new URL(url);
            connection = (HttpURLConnection) realUrl.openConnection();
            boolean useHttps = url.startsWith("https");
            if (useHttps){
                HttpURLConnection https = (HttpURLConnection) connection;
                trustAllHosts(https);
            }

            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);
            connection.setReadTimeout(timeout*1000);
            connection.setConnectTimeout(3*1000);
            connection.setRequestProperty("connection","Keep-Alive");
            connection.setRequestProperty("Content-Type","application/json;charset=UTF-8");
            connection.setRequestProperty("Accept-Charset","application/json;charset=UTF-8");

            if(accessToken!=null && accessToken.trim().length()>0){
                connection.setRequestProperty(XXL_JOB_ACCESS_TOKEN,accessToken);
            }
            connection.connect();

            if ((requestObj!=null){
                String requstBody = GsonTool.toJson(requestObj);
                DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
                dataOutputStream.write(requstBody.getBytes("UTF-8"));
                dataOutputStream.flush();
                dataOutputStream.close();
            }

            int statusCode = connection.getResponseCode();
            if (statusCode != 200){

            }
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
            StringBuilder result = new StringBuilder();
            String line;
            while((line = bufferedReader.readLine())!=null){
                result.append(line);
            }
            String resultJson = result.toString();
            try {
                ReturnT returnT = GsonTool.fromJson(resultJson,ReturnT.class,returnTargClassOfT);
                return returnT;
            }

        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (Exception e2) {
                logger.error(e2.getMessage(), e2);
            }
        }
    }
}
