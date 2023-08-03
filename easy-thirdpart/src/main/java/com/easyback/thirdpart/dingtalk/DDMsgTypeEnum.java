package com.easyback.thirdpart.dingtalk;


import java.util.HashMap;
import java.util.Map;


/**
 * Created by huangqilong on 2019/10/28
 */
public enum DDMsgTypeEnum {
    /**
     * 钉钉消息类型
     */
    TEXT("text", "text"),
    LINK("link", "link"),
    MARKDOWN("markdown", "markdown");


    String key;

    String value;

    static final Map<String, DDMsgTypeEnum> map = new HashMap<>();

    static {
        DDMsgTypeEnum[] es = DDMsgTypeEnum.values();
        for (DDMsgTypeEnum e : es) {
            map.put(e.getKey(), e);
        }
    }

    DDMsgTypeEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static DDMsgTypeEnum parse(String key) {
        return map.get(key);
    }

}
