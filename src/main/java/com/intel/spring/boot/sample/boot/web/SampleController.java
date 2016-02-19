package com.intel.spring.boot.sample.boot.web;

import com.alibaba.fastjson.JSONObject;
import com.intel.spring.boot.sample.boot.utils.JWTUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by Ecic Chen on 2016/1/29.
 */
@Controller
public class SampleController {

    @RequestMapping("/")
    @ResponseBody
    public String index(){
        return "Hello World!";
    }

    @RequestMapping("/tuoling/test")
    @ResponseBody
    public String resultJson(@RequestHeader HttpHeaders headers){
        if(headers.containsKey("tk")){
            String token =  headers.get("tk").get(0);
            String url = "http://54.223.83.198/intelStore_pre " +
                    "" + token;

            String result = httpRequest(url);
            JSONObject resultObj = JSONObject.parseObject(result);

            if(null != resultObj){
                JSONObject dataObj = resultObj.getJSONObject("data");
                if(dataObj != null){
                    String tokenStr = dataObj.get("token").toString();
                    Map<String,Object> params = JWTUtils.verifierToken(tokenStr,"hbvxd3@@@@FjQqT04NU");
                    return new JSONObject(params).toString();
                }
            }
        }
        return "解析失败";
    }

    public  String httpRequest(String requestUrl) {
        StringBuffer buffer = new StringBuffer();
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();

            httpUrlConn.setDoOutput(false);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);

            httpUrlConn.setRequestMethod("GET");
            httpUrlConn.connect();

            // 将返回的输入流转换成字符串    很重要将字节流转化为字符流
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine())!= null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();

        } catch (Exception e) {
        }
        return buffer.toString();
    }
}
