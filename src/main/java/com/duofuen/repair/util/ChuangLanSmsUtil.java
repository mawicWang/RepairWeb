package com.duofuen.repair.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import java.io.PrintWriter;

/**
 * @author tianyh
 * @Description: HTTP 请求
 */
public class ChuangLanSmsUtil {

    private static final Logger LOGGER = LogManager.getLogger();


    // 请登录zz.253.com 获取创蓝API账号(非登录账号,示例:N1234567)
    private static final String account = "buzz168";
    private static final String account_test = "N9718791";
    // 请登录zz.253.com 获取创蓝API密码(非登录密码)
    private static final String password = "Buzztime666";
    private static final String password_test = "Gzcl888888";
    //短信发送的URL 请登录zz.253.com 获取完整的URL接口信息
    private static final String url = "http://sapi.253.com/msg/HttpBatchSendSM";
    private static final String smsSingleRequestServerUrl_test = "http://smssh1.253.com/msg/send/json";
    //状态报告
    private static final String report = "true";

    public static boolean sendMsg(String phoneNum, String message) {
        // 设置您要发送的内容：其中“【】”中括号为运营商签名符号，多签名内容前置添加提交
        String msg = Const.MSG_PREFIX + message;

//        SmsSendRequest smsSingleRequest = new SmsSendRequest(account, password, msg, phoneNum, report);
//        String requestJson = JSON.toJSONString(smsSingleRequest);
//        LOGGER.info("before request string is: " + requestJson);
//
//        String response = sendSmsByPost(smsSingleRequestServerUrl, requestJson);
//        LOGGER.info("response after request result is :" + response);
//
//        SmsSendResponse smsSingleResponse = JSON.parseObject(response, SmsSendResponse.class);
//        LOGGER.info("response  toString is :" + smsSingleResponse);
        try {
            LOGGER.info("短信内容：{}", msg);
            String result = batchSend(url, account, password, phoneNum, msg, false, null);
            LOGGER.info("短信返回结果：{}", result);

            Pattern pattern = Pattern.compile("(\\d{14}),(\\d+)([\\s\\S]?)");
            Matcher m = pattern.matcher(result);
            if (m.find() && m.group(2).equals("0")) {
                LOGGER.info("Message send success!");
                return true;
            } else {
                LOGGER.info("Message send fail!");
            }
        } catch (Exception e) {
            LOGGER.error(e);
            return false;
        }
//        if (smsSingleResponse.getCode().equals("0")) {
//            LOGGER.info("短信发送成功");
//            return true;
//        }
        return false;
    }


    private static String batchSend(String url, String account, String pswd, String mobile, String msg,
                                    boolean needstatus, String extno) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet();

        try {
            URI uri = new URIBuilder(url)
                    .setParameter("account", account)
                    .setParameter("pswd", pswd)
                    .setParameter("mobile", mobile)
                    .setParameter("needstatus", String.valueOf(needstatus))
                    .setParameter("msg", msg)
                    .setParameter("extno", extno).build();
            httpGet.setURI(uri);
            CloseableHttpResponse response = httpclient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
//                InputStream in = method.getResponseBodyAsStream();
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                byte[] buffer = new byte[1024];
//                int len = 0;
//                while ((len = in.read(buffer)) != -1) {
//                    baos.write(buffer, 0, len);
//                }
//                return URLDecoder.decode(baos.toString(), "UTF-8");
            } else {
                throw new Exception("HTTP ERROR Status: " + response.getStatusLine().getStatusCode() + ":" + response.getStatusLine().getReasonPhrase());
            }
        } finally {
            httpclient.close();
        }
    }

    /**
     * test only
     **/
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
