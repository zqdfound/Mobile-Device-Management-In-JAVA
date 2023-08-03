package com.easyback.thirdpart.dingtalk;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.easyback.thirdpart.dingtalk.dto.DingTalkMarketMsgDTO;
import com.easyback.thirdpart.dingtalk.dto.DingTalkTextMsgDTO;
import com.easyback.thirdpart.dingtalk.utils.DingTalkUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * Created by huangqilong on 2019/10/28
 * 钉钉群机器人发送消息
 */
@Service
@Slf4j
public class DingTalkSendMessageService {

    @Resource
    private DingTalkUtils dingTalkUtils;

    /**
     * 获取accessToken
     *
     * @return
     * @throws Exception
     */
    public String getAccessToken() throws Exception {

        JSONObject result = dingTalkUtils.getDingTalkAccessToken();

        if (Objects.isNull(result)) {
            return "获取accessToken失败";
        }
        if (!StrUtil.equals("0", result.getStr("errcode"))) {
            return result.getStr("errmsg");
        }

        String accessToken = result.getStr("access_token");

        return accessToken;

    }


    /**
     * 发送text格式消息
     *
     * @param dto
     * @return
     * @throws Exception
     */
    public JSONObject sendTextMessage(String sendMsgUrl, DingTalkTextMsgDTO dto) {


        // 发送text请求
        return dingTalkUtils.postHttpClentsJsonNoEntity(sendMsgUrl, dto.toJsonString());

    }


    /**
     * 发送marketDown格式消息
     *
     * @param dto
     * @return
     * @throws Exception
     */
    public JSONObject sendMarkDownMessage(String sendMsgUrl, DingTalkMarketMsgDTO dto) {

        // 发送Market请求
        return dingTalkUtils.postHttpClentsJsonNoEntity(sendMsgUrl, dto.toJsonString());

    }


}
