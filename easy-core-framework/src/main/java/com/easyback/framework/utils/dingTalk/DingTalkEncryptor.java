package com.easyback.framework.utils.dingTalk;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Security;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * 钉钉开放平台加解密方法
 * 在ORACLE官方网站下载JCE无限制权限策略文件
 *     JDK6的下载地址：http://www.oracle.com/technetwork/java/javase/downloads/jce-6-download-429243.html
 *     JDK7的下载地址： http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-432124.html
 */
public class DingTalkEncryptor {

    private static final Charset CHARSET = Charset.forName("utf-8");
    private static final Base64 base64  = new Base64();
    private byte[]         aesKey;
    private String         token;
    private String         corpId;
    /**ask getPaddingBytes key固定长度**/
    private static final Integer AES_ENCODE_KEY_LENGTH = 43;
    /**加密随机字符串字节长度**/
    private static final Integer RANDOM_LENGTH = 16;

    /**
     * 构造函数
     * @param token             钉钉开放平台上，开发者设置的token
     * @param encodingAesKey  钉钉开放台上，开发者设置的EncodingAESKey
     * @param corpId           ISV进行配置的时候应该传对应套件的SUITE_KEY，普通企业是Corpid
     * @throws DingTalkEncryptException 执行失败，请查看该异常的错误码和具体的错误信息
     */
    public DingTalkEncryptor(String token, String encodingAesKey, String corpId) throws DingTalkEncryptException{
        if (null != encodingAesKey && encodingAesKey.length() == AES_ENCODE_KEY_LENGTH) {
            this.token = token;
            this.corpId = corpId;
            this.aesKey = Base64.decodeBase64(encodingAesKey + "=");
        } else {
            throw new DingTalkEncryptException(DingTalkEncryptException.AES_KEY_ILLEGAL);
        }
    }

    /**
     * 将和钉钉开放平台同步的消息体加密,返回加密Map
     * @param plaintext     传递的消息体明文
     * @param timeStamp      时间戳
     * @param nonce           随机字符串
     * @return
     * @throws DingTalkEncryptException
     */
    public Map<String,String> getEncryptedMap(String plaintext, Long timeStamp, String nonce) throws DingTalkEncryptException {
        if(null==plaintext){
            throw new DingTalkEncryptException(DingTalkEncryptException.ENCRYPTION_PLAINTEXT_ILLEGAL);
        }
        if(null==timeStamp){
            throw new DingTalkEncryptException(DingTalkEncryptException.ENCRYPTION_TIMESTAMP_ILLEGAL);
        }
        if(null==nonce){
            throw new DingTalkEncryptException(DingTalkEncryptException.ENCRYPTION_NONCE_ILLEGAL);
        }
        // 加密
        String encrypt = this.encrypt(DingTalkUtils.getRandomStr(RANDOM_LENGTH), plaintext);
        String signature = this.getSignature(this.token, String.valueOf(timeStamp), nonce, encrypt);
        Map<String, String> resultMap = new HashMap();
        resultMap.put("msg_signature", signature);
        resultMap.put("encrypt", encrypt);
        resultMap.put("timeStamp", String.valueOf(timeStamp));
        resultMap.put("nonce", nonce);
        return resultMap;
    }

    /**
     * 密文解密
     * @param msgSignature     签名串
     * @param timeStamp        时间戳
     * @param nonce             随机串
     * @param encryptMsg       密文
     * @return                  解密后的原文
     * @throws DingTalkEncryptException
     */
    public String getDecryptMsg(String msgSignature, String timeStamp, String nonce, String encryptMsg)throws DingTalkEncryptException {
        //校验签名
        String signature = this.getSignature(this.token, timeStamp, nonce, encryptMsg);
        if (!signature.equals(msgSignature)) {
            throw new DingTalkEncryptException(DingTalkEncryptException.COMPUTE_SIGNATURE_ERROR);
        } else {
            String result = this.decrypt(encryptMsg);
            return result;
        }
    }


    /*
     * 对明文加密.
     * @param text 需要加密的明文
     * @return 加密后base64编码的字符串
     */
    private String encrypt(String random, String plaintext) throws DingTalkEncryptException {
        try {
            byte[] randomBytes = random.getBytes(CHARSET);
            byte[] plainTextBytes = plaintext.getBytes(CHARSET);
            byte[] lengthByte = DingTalkUtils.int2Bytes(plainTextBytes.length);
            byte[] corpidBytes = this.corpId.getBytes(CHARSET);
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            byteStream.write(randomBytes);
            byteStream.write(lengthByte);
            byteStream.write(plainTextBytes);
            byteStream.write(corpidBytes);
            byte[] padBytes = DingTalkPKCS7Padding.getPaddingBytes(byteStream.size());
            byteStream.write(padBytes);
            byte[] unencrypted = byteStream.toByteArray();
            byteStream.close();
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(this.aesKey, "AES");
            IvParameterSpec iv = new IvParameterSpec(this.aesKey, 0, 16);
            cipher.init(1, keySpec, iv);
            byte[] encrypted = cipher.doFinal(unencrypted);
            String result = base64.encodeToString(encrypted);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DingTalkEncryptException(DingTalkEncryptException.COMPUTE_ENCRYPT_TEXT_ERROR);
        }
    }

    /*
     * 对密文进行解密.
     * @param text 需要解密的密文
     * @return 解密得到的明文
     */
    private String decrypt(String text) throws DingTalkEncryptException {
        byte[] originalArr;
        byte[] networkOrder;
        try {
            // 设置解密模式为AES的CBC模式
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(this.aesKey, "AES");
            IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(this.aesKey, 0, 16));
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
            // 使用BASE64对密文进行解码
            networkOrder = Base64.decodeBase64(text);
            // 解密
            originalArr = cipher.doFinal(networkOrder);
        } catch (Exception e) {
            throw new DingTalkEncryptException(DingTalkEncryptException.COMPUTE_DECRYPT_TEXT_ERROR);
        }

        String plainText;
        String fromCorpid;
        try {
            // 去除补位字符
            byte[] bytes = DingTalkPKCS7Padding.removePaddingBytes(originalArr);
            // 分离16位随机字符串,网络字节序和corpId
            networkOrder = Arrays.copyOfRange(bytes, 16, 20);
            int plainTextLegth = DingTalkUtils.bytes2int(networkOrder);
            plainText = new String(Arrays.copyOfRange(bytes, 20, 20 + plainTextLegth), CHARSET);
            fromCorpid = new String(Arrays.copyOfRange(bytes, 20 + plainTextLegth, bytes.length), CHARSET);
        } catch (Exception e) {
            throw new DingTalkEncryptException(DingTalkEncryptException.COMPUTE_DECRYPT_TEXT_LENGTH_ERROR);
        }

        // corpid不相同的情况
        if (!fromCorpid.equals(this.corpId)) {
            throw new DingTalkEncryptException(DingTalkEncryptException.COMPUTE_DECRYPT_TEXT_CORPID_ERROR);
        } else {
            return plainText;
        }
    }

    /**
     * 数字签名
     * @param token         isv token
     * @param timestamp     时间戳
     * @param nonce          随机串
     * @param encrypt       加密文本
     * @return
     * @throws DingTalkEncryptException
     */
    public String getSignature(String token, String timestamp, String nonce, String encrypt) throws DingTalkEncryptException {
        try {
            String[] array = new String[]{token, timestamp, nonce, encrypt};
            Arrays.sort(array);
            StringBuffer sb = new StringBuffer();

            for(int i = 0; i < 4; ++i) {
                sb.append(array[i]);
            }

            String str = sb.toString();
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(str.getBytes());
            byte[] digest = md.digest();
            StringBuffer hexstr = new StringBuffer();
            String shaHex = "";

            for(int i = 0; i < digest.length; ++i) {
                shaHex = Integer.toHexString(digest[i] & 255);
                if (shaHex.length() < 2) {
                    hexstr.append(0);
                }

                hexstr.append(shaHex);
            }

            return hexstr.toString();
        } catch (Exception var13) {
            throw new DingTalkEncryptException(DingTalkEncryptException.COMPUTE_SIGNATURE_ERROR);
        }
    }

    private static void RemoveCryptographyRestrictions() throws Exception {
        Class<?> jceSecurity = getClazz("javax.crypto.JceSecurity");
        Class<?> cryptoPermissions = getClazz("javax.crypto.CryptoPermissions");
        Class<?> cryptoAllPermission = getClazz("javax.crypto.CryptoAllPermission");
        if (jceSecurity != null) {
            setFinalStaticValue(jceSecurity, "isRestricted", false);
            PermissionCollection defaultPolicy = (PermissionCollection)getFieldValue(jceSecurity, "defaultPolicy", (Object)null, PermissionCollection.class);
            if (cryptoPermissions != null) {
                Map<?, ?> map = (Map)getFieldValue(cryptoPermissions, "perms", defaultPolicy, Map.class);
                map.clear();
            }

            if (cryptoAllPermission != null) {
                Permission permission = getFieldValue(cryptoAllPermission, "INSTANCE", (Object)null, Permission.class);
                defaultPolicy.add(permission);
            }
        }

    }

    private static Class<?> getClazz(String className) {
        Class clazz = null;

        try {
            clazz = Class.forName(className);
        } catch (Exception var3) {
        }

        return clazz;
    }

    private static void setFinalStaticValue(Class<?> srcClazz, String fieldName, Object newValue) throws Exception {
        Field field = srcClazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & -17);
        field.set((Object)null, newValue);
    }

    private static <T> T getFieldValue(Class<?> srcClazz, String fieldName, Object owner, Class<T> dstClazz) throws Exception {
        Field field = srcClazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return dstClazz.cast(field.get(owner));
    }

    static {
        try {
            Security.setProperty("crypto.policy", "limited");
            RemoveCryptographyRestrictions();
        } catch (Exception var1) {
        }

    }
}
