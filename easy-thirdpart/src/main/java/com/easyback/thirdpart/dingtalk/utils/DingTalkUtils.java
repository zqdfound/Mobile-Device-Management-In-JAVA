package com.easyback.thirdpart.dingtalk.utils;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.easyback.thirdpart.dingtalk.DingTalkApiConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by huangqilong on 2019/10/28
 */
@Component
@Slf4j
public class DingTalkUtils {

    private final String ACCESS_TOKEN_URL = "https://oapi.dingtalk.com/gettoken";

    @Autowired
    private DingTalkApiConfig dingTalkApiConfig;




    /**
     * 没有参数名，只有参数值的httpClent请求(POST)
     *
     * @param urlStr 请求地址
     * @param params 参数
     * @return 结果
     */

    public static JSONObject postHttpClentsJsonNoEntity(String urlStr, String params) {

        log.info("钉钉发送消息请求参数: {}",params);

        try {
            HttpClient httpclient = HttpClients.createDefault();

            HttpPost httppost = new HttpPost(urlStr);

            httppost.addHeader("Content-Type", "application/json; charset=UTF-8");

            StringEntity se = new StringEntity(params, "UTF-8");

            httppost.setEntity(se);

            HttpResponse response = httpclient.execute(httppost);

            String result = EntityUtils.toString(response.getEntity(), "UTF-8");

            log.info("钉钉发送消息返回参数: {}",result);
            return JSONUtil.parseObj(result);
        } catch (Exception e) {
            log.error("连接【" + urlStr + "】失败\r\n" + e.getMessage());
            return null;
        }

    }

    /**
     * 获取AccesstToken
     *
     * @return
     * @throws Exception
     */
    public JSONObject getDingTalkAccessToken() throws Exception {

        Map map = MapUtil.newHashMap();
        map.put("appsecret", dingTalkApiConfig.getSecretKey());
        map.put("appkey", dingTalkApiConfig.getAppkey());

        // 拼接url
        String url = getAppendUrl(ACCESS_TOKEN_URL, map);

        String data = sendGet(url);

        return JSONUtil.parseObj(data);
    }


    public static String getAppendUrl(String url, Map<String, String> map) {
        if (map != null && !map.isEmpty()) {
            StringBuffer buffer = new StringBuffer();
            Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> entry = iterator.next();
                if (StrUtil.isEmpty(buffer.toString())) {
                    buffer.append("?");
                } else {
                    buffer.append("&");
                }
                buffer.append(entry.getKey()).append("=").append(entry.getValue());
            }
            url += buffer.toString();
        }
        return url;
    }

    public static String sendGet(String url) {
        String result = "";
        try {
            URL U = new URL(url);
            URLConnection connection = U.openConnection();
            connection.connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            in.close();
        } catch (Exception e) {
            log.warn("钉钉获取access_token失败" + e.getMessage());
        }
        return result;
    }


}
