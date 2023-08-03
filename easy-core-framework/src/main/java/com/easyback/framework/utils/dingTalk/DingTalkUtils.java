package com.easyback.framework.utils.dingTalk;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.Random;

/**
 * 加解密工具类
 */
@Slf4j
public class DingTalkUtils {
    /**
     *
     * @return
     */
    public static String getRandomStr(int count) {
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < count; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }


    /*
     * int转byte数组,高位在前
     */
    public static byte[] int2Bytes(int count) {
        byte[] byteArr = new byte[4];
        byteArr[3] = (byte) (count & 0xFF);
        byteArr[2] = (byte) (count >> 8 & 0xFF);
        byteArr[1] = (byte) (count >> 16 & 0xFF);
        byteArr[0] = (byte) (count >> 24 & 0xFF);
        return byteArr;
    }

    /**
     * 高位在前bytes数组转int
     * @param byteArr
     * @return
     */
    public static int bytes2int(byte[] byteArr) {
        int count = 0;
        for (int i = 0; i < 4; i++) {
            count <<= 8;
            count |= byteArr[i] & 0xff;
        }
        return count;
    }

    public static JSONObject sendText(String sendMsgUrl, String params){
        // 发送text请求

        //log.info("钉钉发送消息请求参数: {}",params);

        try {
            HttpClient httpclient = HttpClients.createDefault();

            HttpPost httppost = new HttpPost(sendMsgUrl);

            httppost.addHeader("Content-Type", "application/json; charset=UTF-8");

            StringEntity se = new StringEntity(params, "UTF-8");

            httppost.setEntity(se);

            HttpResponse response = httpclient.execute(httppost);

            String result = EntityUtils.toString(response.getEntity(), "UTF-8");

            log.debug("钉钉发送消息返回参数: {}",result);
            return JSONUtil.parseObj(result);
        } catch (Exception e) {
            log.error("连接【" + sendMsgUrl + "】失败\r\n" + e.getMessage());
            return null;
        }
    }
}
