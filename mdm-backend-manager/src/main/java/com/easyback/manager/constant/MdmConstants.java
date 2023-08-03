package com.easyback.manager.constant;

/**
 * @Author: zhuangqingdian
 * @Date:2023/7/3
 */
public class MdmConstants {

    //返回的Topic类型
    // Topic                             | Payload                                  |
    //|-----------------------------------|------------------------------------------|
    //| [mdm.Authenticate](#authenticate) | [checkin_event](#checkin-events)         |
    //| [mdm.TokenUpdate](#token-update)  | [checkin_event](#checkin-events)         |
    //| [mdm.CheckOut](#checkout)         | [checkin_event](#checkin-events)         |
    //| [mdm.Connect](#connect)           | [acknowledge_event](#acknowledge-events) |
    public static final String RESP_AUTH = "mdm.Authenticate";
    public static final String RESP_TOKEN_UPDATE = "mdm.TokenUpdate";
    public static final String RESP_CONNECT = "mdm.Connect";
    public static final String RESP_CHECKOUT = "mdm.CheckOut";

    /**MDM请求服务器端状态**/
    public static final String Idle = "Idle";
    public static final String Acknowledged = "Acknowledged";
    public static final String CommandFormatError = "CommandFormatError";
    public static final String Error = "Error";
    public static final String NotNow = "NotNow";
    public static final String QueryResponses = "QueryResponses";
    public static final String InstalledApplicationList = "InstalledApplicationList";
}
