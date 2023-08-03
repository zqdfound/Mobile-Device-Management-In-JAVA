package com.easyback.framework.utils.dingTalk;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * Created by huangqilong on 2019/10/28
 */
@Data
public class DingTalkTextMsgDTO implements Serializable {

    @NotBlank(message = "消息内容不能为空")
    private String content;

    private Boolean atAll;

    private List<String> atMobiles;


    public String toJsonString() {
        JSONObject result = new JSONObject();
        JSONObject content = new JSONObject();
        JSONObject at = new JSONObject();
        if (this.getAtAll() != null && this.getAtAll()) {
            at.put("isAtAll", true);
        } else {
            if (CollectionUtil.isNotEmpty(this.getAtMobiles())) {
                at.put("atMobiles", this.getAtMobiles());
            }
        }
        result.put("msgtype", DDMsgTypeEnum.TEXT.getKey());
        content.put("content",this.content);
        result.put("text", content);
        result.put("at", at);
        return result.toJSONString();
    }

}
