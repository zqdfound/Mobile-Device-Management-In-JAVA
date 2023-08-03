package com.easyback.manager.utils;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.easyback.framework.exceptions.BizException;
import com.easyback.framework.utils.HttpUtil;
import com.easyback.manager.constant.MicroMdmApi;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  DEP（ABM） 相关接口
 * @Author: zhuangqingdian
 * @Date:2023/7/28
 */
@Slf4j
public class DEPUtil {
    //microMDM ServerURL
    private String serverUrl;
    //microMDM Authorization
    private String authorization;
    private String xServerProtocolVersion;


    public DEPUtil(String serverUrl,String authorization){
        this.serverUrl = serverUrl;
        this.authorization = authorization;
        this.xServerProtocolVersion = "3";
    }

    //获取鉴权token
    private static final String SESSION_URL = "https://mdmenrollment.apple.com/session";
    //下发激活锁
    private static final String ACTIVATION_LOCK_URL = "https://mdmenrollment.apple.com/device/activationlock";
    //移除ABM
    private static final String DISOWN_URL = "https://mdmenrollment.apple.com/devices/disown";
    //设备详情
    private static final String DEVICE_DETAIL_URL = "https://mdmenrollment.apple.com/devices";
    /**
     * 获取DEP Token
     */
    //{
    //    "dep_tokens": [
    //        {
    //            "consumer_key": "CK_f55c066fffbc344a1f8a639e82e01e08861fc3bc5a6e59eff9ef75b702294f5260a3b43db0a5910e4d295a244053782e",
    //            "consumer_secret": "CS_e50cd0b30e6c830c592d20b54cf67638c2d8bc1a",
    //            "access_token": "AT_O18328322013Obb742028cbee165192c3ca2528c6ace1d2ab3171O1689575801726",
    //            "access_secret": "AS_2546ad44e047d590070f834821284b42d63bc765",
    //            "access_token_expiry": "2024-07-16T06:36:41Z"
    //        }
    //    ],
    //    "public_key": "MIIDGDCCAgCgAwIBAgIQF/S9O2/HCqHLvxXbizUcNjANBgkqhkiG9w0BAQsFADAdMRswGQYDVQQDExJtaWNyb21kbS1kZXAtdG9rZW4wHhcNMjMwNzE3MDYyNDQ5WhcNMjQwNzE2MDYyNDQ5WjAdMRswGQYDVQQDExJtaWNyb21kbS1kZXAtdG9rZW4wggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQDgiuxTcmpBD1dstWzss2PIgq1kpjtvgeIdlpcPPh6wVhv/2V+QIkQ4l1Gh/L/2fxmJpIqrNGHhiXivTFxdpv+pK7wzUiWOIDF38yQR347M7SmwGYNuUglyeg9ZE8cOCBr2Kp4DeKNkBfY2RQbfdkb712pZOwYSYYh1vmUy6OJ3B9UQWXlSQZ0X91tG70c7i+ZxbyzCnrQY3hSOhm0GV5ae5AGdFCklaU+5ZnOJMNqDdEXbhjsJxZgJVZz0PasQAIZkxVcwU9NRiDRAfj7NZ3t0SFlIFUqHBE1yi6R5NkI3/OZ8AgnHJP05lceIFkkciHDlHADP77v6ZK0yO+lAHC1jAgMBAAGjVDBSMA4GA1UdDwEB/wQEAwIFoDATBgNVHSUEDDAKBggrBgEFBQcDATAMBgNVHRMBAf8EAjAAMB0GA1UdEQQWMBSCEm1pY3JvbWRtLWRlcC10b2tlbjANBgkqhkiG9w0BAQsFAAOCAQEAF+i2ea0W+KFIaFHdbNeK+VR4w16bOLqix3GqONCrkOtYg5l4yMT56FTY7mvhqFVnudK9OyRfvVs9LYB9SbPub5ym2/6svBSPh6ib7A3kXH/dNuLI5LnXvtO9RVK99prxk5WxUruVpulVBKAWZiT7Qd5tR70sR3QBVqsdB1mYaLgiu7GfE2omTi+i4N3D2leR5aiLyOIpHMRx50lGfxLFqzZybb59Anm4duFgBZdt2oR0LSw6C+m+3ckqK1XRgFQ2DRT1C2AerBLuMlJ6I5HOgGubGwZmJ4NSgi4uApP+gWGnSm4TdKpoFT2IVKMQFCzi4kDwC4i8gLG+WwU50Ta7hw=="
    //}
    private JSONObject getDepTokens() throws IOException {
        Map<String, String> header = new HashMap<>();
        header.put("Authorization",this.authorization);
        //JSONObject json = JSONUtil.parseObj(RequestUtil.doGET(this.serverUrl+ MicroMdmApi.DEP_TOKENS,map));
        JSONObject json = JSONUtil.parseObj(HttpUtil.doGet(this.serverUrl+ MicroMdmApi.DEP_TOKENS,header,null));
        JSONArray jsonArray = json.getJSONArray("dep_tokens");
        JSONObject depTokens = JSONUtil.parseObj(jsonArray.get(0));
        return depTokens;
    }

    /**
     * 获取sessionToken
     * @return
     */
    //{
    //    "auth_session_token" : "87a235815b8d6661ac73329f75815b8d6661ac73329f815"
    //}
    public String getSessionToken() throws IOException {
        JSONObject jsonObject = getDepTokens();
        Oauth1Util oauth1Util = new Oauth1Util(jsonObject.getStr("consumer_key"),
                jsonObject.getStr("consumer_secret"),
                jsonObject.getStr("access_token"),
                jsonObject.getStr("access_secret"));
        String result = oauth1Util.executeGetRequest(SESSION_URL);
        return JSONUtil.parseObj(result).getStr("auth_session_token");
    }


    /**
     * 下发激活锁
     * https://developer.apple.com/documentation/devicemanagement/activationlockrequest?language=objc
     * @param deviceSerial 设备序列号
     * @param escrowKey
     * @return
     */
    //{
    //    "serial_number": "F18XKBYUKXM5",
    //    "response_status": "SUCCESS"
    //}
    //SUCCESS: The device was successfully locked.
    //NOT_ACCESSIBLE: The device with this serial number is not accessible by this user.
    //ORG_NOT_SUPPORTED: The device with this serial number is not supported because it is not present in the new program.
    //DEVICE_NOT_SUPPORTED: The device type is not supported.
    //DEVICE_ALREADY_LOCKED: The device is already locked by someone.
    //FAILED: Activation lock of the device failed for unexpected reason. If retry fails, the client should contact Apple support.
    public Boolean activationLockDevice(String deviceSerial,String escrowKey) throws IOException {
        Map<String, String> header = new HashMap<>();
        header.put("X-ADM-Auth-Session",getSessionToken());
        header.put("X-Server-Protocol-Version",this.xServerProtocolVersion);

       JSONObject body = new JSONObject();
       body.set("device",deviceSerial);
       body.set("escrow_key",escrowKey);
       body.set("lost_message","do not go gentle into that good night");

        //JSONObject jsonObject = RequestUtil.doPost(ACTIVATION_LOCK_URL, header, body.toString());
        JSONObject jsonObject = JSONUtil.parseObj(HttpUtil.doPost(ACTIVATION_LOCK_URL, header, body.toString(),HttpUtil.JSON));
        if("SUCCESS".equals(jsonObject.getStr("response_status"))){
            return Boolean.TRUE;
        }
        if("NOT_ACCESSIBLE".equals(jsonObject.getStr("response_status"))){
          throw new BizException("设备未加入ABM");
        }
        log.error("下发激活锁失败：{}",jsonObject.toString());
        return Boolean.FALSE;
    }

    /**
     * 移除ABM
     * @param deviceSerial
     * @return
     */
    //{"devices":{"F18XKBYUKXM5":"SUCCESS"}}
    public boolean disownDevice(String deviceSerial) throws IOException {
        Map<String, String> header = new HashMap<>();
        header.put("X-ADM-Auth-Session",getSessionToken());
        header.put("X-Server-Protocol-Version",this.xServerProtocolVersion);

        JSONObject body = new JSONObject();
        body.set("devices", Arrays.asList(deviceSerial));

//        JSONObject jsonObject = RequestUtil.doPost(DISOWN_URL, header, body.toString());
        JSONObject jsonObject = JSONUtil.parseObj(HttpUtil.doPost(DISOWN_URL, header, body.toString(),HttpUtil.JSON));
        if(jsonObject.getStr("devices").contains("SUCCESS")){
            return Boolean.TRUE;
        }
        log.error("移除ABM失败：{}",jsonObject.toString());
        return Boolean.FALSE;
    }

    /**
     * 设备详情
     * @param devices
     * @return {"devices":{"F18XKBYUKXM5":{"serial_number":"F18XKBYUKXM5","description":"IPHONE XR BLACK 128GB A2108-CHN","model":"iPhone XR","os":"iOS","device_family":"iPhone","color":"BLACK","profile_status":"empty","device_assigned_by":"zqd@zejihuicomcn.appleid.com","device_assigned_date":"2023-07-27T07:10:03Z","response_status":"SUCCESS"}}}
     */
    public JSONObject deviceDetail(List<String> devices) throws IOException {
        Map<String, String> header = new HashMap<>();
        header.put("X-ADM-Auth-Session",getSessionToken());
        header.put("X-Server-Protocol-Version",this.xServerProtocolVersion);
        JSONObject json = new JSONObject();
        json.set("devices",devices);
        return JSONUtil.parseObj(HttpUtil.doPost(DEVICE_DETAIL_URL,header,json.toString(),HttpUtil.JSON));
    }


//    public static void main(String[] args) {
//        DEPUtil depUtil = new DEPUtil("https://mdm.zejihui.com.cn","Basic bWljcm9tZG06cGh0T2NlVFpFaQ==");
//        depUtil.activationLockDevice("F18XKBYUKXM5","6ab40d5eabe7218ec04182f461005600c7e3426bddd82cdb405bde9a1e0014b5");
//    }


}
