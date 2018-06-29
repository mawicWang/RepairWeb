package com.duofuen.repair.util;

import com.alibaba.fastjson.JSON;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
//import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author tianyh
 * @Description:HTTP 请求
 */
public class ChuangLanSmsUtil {

    private static final Logger LOGGER = LogManager.getLogger();


    // 请登录zz.253.com 获取创蓝API账号(非登录账号,示例:N1234567)
    private static final String account = "N9718791";
    // 请登录zz.253.com 获取创蓝API密码(非登录密码)
    private static final String password = "Gzcl888888";
    //短信发送的URL 请登录zz.253.com 获取完整的URL接口信息
    private static final String smsSingleRequestServerUrl = "http://smssh1.253.com/msg/send/json";
    //状态报告
    private static final String report = "true";

    public static boolean sendMsg(String phoneNum, String message) {
        // 设置您要发送的内容：其中“【】”中括号为运营商签名符号，多签名内容前置添加提交
        String msg = Const.MSG_PREFIX + message;

        SmsSendRequest smsSingleRequest = new SmsSendRequest(account, password, msg, phoneNum, report);
        String requestJson = JSON.toJSONString(smsSingleRequest);
        LOGGER.info("before request string is: " + requestJson);

        String response = sendSmsByPost(smsSingleRequestServerUrl, requestJson);
        LOGGER.info("response after request result is :" + response);

        SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
        LOGGER.info("response  toString is :" + smsSingleResponse);

        if (smsSingleResponse.getCode().equals("0")) {
            LOGGER.info("短信发送成功");
            return true;
        }
        return false;
    }


    private static String sendSmsByPost(String postUrl, String postContent) {
        URL url = null;
        try {
            url = new URL(postUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");// 提交模式
            httpURLConnection.setConnectTimeout(10000);//连接超时 单位毫秒
            httpURLConnection.setReadTimeout(10000);//读取超时 单位毫秒
            // 发送POST请求必须设置如下两行
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");

//			PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
//			printWriter.write(postContent);
//			printWriter.flush();

            httpURLConnection.connect();
            OutputStream os = httpURLConnection.getOutputStream();
            os.write(postContent.getBytes("UTF-8"));
            os.flush();

            StringBuilder sb = new StringBuilder();
            int httpRspCode = httpURLConnection.getResponseCode();
            if (httpRspCode == HttpURLConnection.HTTP_OK) {
                // 开始获取数据
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                return sb.toString();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
