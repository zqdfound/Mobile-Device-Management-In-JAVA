package com.easyback.framework.utils;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;

/**
 * @author zhuangqingdian
 * @date 2019-04-02 13:30
 */
public class PwdUtil {

    /**
     *
     * @param pwd 数据库密码
     * @param voPwd 输入明文密码
     * @param random 数据库random
     * @return
     */
    public static Boolean verify(String pwd,String voPwd,String random) {
        String testStr = random;

        byte[] key = voPwd.getBytes();
        HMac mac = new HMac(HmacAlgorithm.HmacMD5, key);

        String macHex1 = mac.digestHex(testStr);
        if(StrUtil.equals(macHex1,pwd)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 加密
     * @param pwd 明文密码
     * @param random 随机数
     * @return
     */
    public static String encryption(String pwd,String random) {

        byte[] key = pwd.getBytes();
        HMac mac = new HMac(HmacAlgorithm.HmacMD5, key);

        return mac.digestHex(random);
    }

    /**
     * 获取UUID随机数
     */
    public static String getRandom() {
        return UUID.randomUUID(true).toString();
    }

    public static void main(String[] args) {
        String testStr = "xSSCm9nRZToiFeFosaYllwrHdlDlJQH90cMtHYMyZtU=";

        byte[] key = "mdmadmin0801".getBytes();
        HMac mac = new HMac(HmacAlgorithm.HmacMD5, key);

        String macHex1 = mac.digestHex(testStr);
        System.out.println(macHex1);


        Boolean b = verify("70361130ebfb20cf7f0adf1770acb21f","123456","xSSCm9nRZToiFeFosaYllwrHdlDlJQH90cMtHYMyZtU=");
        System.out.println(b);
    }

}
