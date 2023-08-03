package com.easyback.manager.utils;

import cn.hutool.core.codec.Base64;
import com.dd.plist.*;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 返回值解析工具
 * @Author: zhuangqingdian
 * @Date:2023/7/3
 */
@Slf4j
public class RespParser {

    /**
     * 定义pList字符串解析正则式
     **/
    public static final String DATA = "\\<data>(.*?)\\</data>";
    public static final String STRING = "\\<string>(.*?)\\</string>";
    public static final String KEY = "\\<key>(.*?)\\</key>";
    public static final String DICT = "\\<dict>(.*?)\\</dict>";
    public static final String ARRAY = "\\<array>(.*?)\\</array>";

    /**
     * 将返回的xml转化为map
     *
     * @param pList
     * @return
     */
    public static Map<String, String> parseXML2Map(String pList) {
        Map<String, String> provisioningProfileList = new HashMap<String, String>();
        pList = replaceBlank(pList);
        pList = pList.replace("<array/>", "<array></array>").
                replaceAll("<true/>", "<string>true</string>").
                replaceAll("<false/>", "<string>false</string>").
                replaceAll("<array>", "").replaceAll("</array>", "").
                replaceAll("<dict>", "").replaceAll("</dict>", "").
                replaceAll("</data><data>", "").
                replaceAll("<data>", "<string>")
                .replaceAll("</data>", "</string>")
                .replaceAll("<real>", "<string>").replaceAll("</real>", "</string>")
                .replaceAll("<key>QueryResponses</key>", "")//获取设备信息特殊处理
                .replaceAll("\\*", "");
        String strBlank = replaceBlank(pList);
        /**获取key、string列表数据**/
        List<String> topkeyList = getList(KEY, strBlank);
        List<String> topstringList = getList(STRING, strBlank);
        for (int i = 0; i < topstringList.size(); i++) {
//            log.info(topkeyList.get(i) + " === " + topstringList.get(i));
            provisioningProfileList.put(topkeyList.get(i), topstringList.get(i));
        }
        return provisioningProfileList;
    }

    /**
     * 获取字符串列表数据
     *
     * @param pattern
     * @param pList
     * @return
     */
    public static List<String> getList(String pattern, String pList) {
        /**获取data列表数据**/
        List<String> dataList = new ArrayList<String>();
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(pList);
        while (m.find()) {
            dataList.add(m.group(1));
        }
        return dataList;
    }

    /**
     * java去除字符串中的空格、回车、换行符、制表符
     *
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        String strBlank = "";
        if (str != null) {
            str = str.replace("<false/>", "<string>false</string>");
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            strBlank = m.replaceAll("");
        }
        return strBlank;
    }


    /**
     * 解析加密的payload
     *
     * @param payloadEncode
     * @return
     */
    public static Map<String, String> getRespMapByPayload(String payloadEncode) {
        //payload解码
        String payloadDecode = Base64.decodeStr(payloadEncode);
//        log.info("payload==========");
//        log.info(payloadDecode);
        Map<String, String> result = RespParser.parseXML2Map(payloadDecode);
        return result;
    }

    //解析获取安装app列表返回值
    public static List<Map<String, String>> getInstallAppMap(String payloadEncode) {
        List<Map<String, String>> list = new ArrayList<>();
        //payload解码
        try {
            String payloadDecode = Base64.decodeStr(payloadEncode);
            NSDictionary rootDict = (NSDictionary) PropertyListParser.parse(payloadDecode.getBytes(StandardCharsets.UTF_8));
            NSObject[] parameters = ((NSArray) rootDict.objectForKey("InstalledApplicationList")).getArray();
            for (NSObject param : parameters) {
                if (param.getClass().equals(NSDictionary.class)) {
                    NSDictionary num = (NSDictionary) param;
                    Map<String, String> map = new HashMap<>();
                    map.put("appBundleId", num.get("Identifier").toString());
                    map.put("appName", num.get("Name").toString());
                    list.add(map);
                }
            }
        } catch (Exception e) {
            log.error("解析Plist失败：{}", e.getMessage());
        }
        return list;
    }



}
