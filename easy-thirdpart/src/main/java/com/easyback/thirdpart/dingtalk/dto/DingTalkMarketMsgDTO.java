package com.easyback.thirdpart.dingtalk.dto;

import com.alibaba.fastjson.JSONObject;
import com.easyback.thirdpart.dingtalk.DDMsgTypeEnum;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.*;

/**
 * Created by huangqilong on 2019/10/28
 */
@Data
public class DingTalkMarketMsgDTO implements Serializable {


    /**
     * 标题
     */
    @NotBlank(message = "title不能为空")
    private String title;

    /**
     * marketDown格式消息
     */
    @NotBlank(message = "内容不能为空")
    private String text;

    /**
     * 字典key
     */
    private String dictKey;


    private String orderNumber;

    /**
     * 是否@所有人
     */
    private Boolean atAll;

    /**
     * 被@人的手机号(在text内容里要有@手机号)
     */
    private List<String> atMobiles;


    public String toJsonString() {
        JSONObject result = new JSONObject();
        result.put("msgtype", DDMsgTypeEnum.MARKDOWN.getKey());
        Map<String, Object> markdown = new HashMap<>(16);
        markdown.put("title", this.getTitle());
        markdown.put("text", this.getText());
        result.put("markdown", markdown);
        JSONObject at = new JSONObject();
        if (this.getAtAll() != null && this.getAtAll()) {
            at.put("isAtAll", true);
        } else {
            if (!CollectionUtils.isEmpty(this.getAtMobiles())) {
                at.put("atMobiles", this.getAtMobiles());
            }
        }
        result.put("at", at);
        return result.toJSONString();
    }

}
