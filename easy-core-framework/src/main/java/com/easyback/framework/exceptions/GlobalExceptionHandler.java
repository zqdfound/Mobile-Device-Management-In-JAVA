package com.easyback.framework.exceptions;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.hutool.core.util.ObjectUtil;
import com.easyback.framework.response.Result;
import com.easyback.framework.response.ResultCode;
import com.easyback.framework.response.ResultGenerator;
import com.easyback.framework.utils.JkServletUtil;
import com.easyback.framework.utils.dingTalk.DingTalkTextMsgDTO;
import com.easyback.framework.utils.dingTalk.DingTalkUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.stream.Collectors;

/**
 * 全局异常统一处理
 *
 * @Author: zhuangqingdian
 * @Date:2023/3/25
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @Value("${spring.profiles.active}")
    private String env;
    @Resource
    private TaskExecutor taskExecutor;
    private String DING_NOTIFY_URL;
    private String DING_KEYWORD;

    @PostConstruct
    public void init() {
        if (ObjectUtil.equals(env, "test")) {
            DING_NOTIFY_URL = "";
            DING_KEYWORD = "";
        } else if (ObjectUtil.equals(env, "prod") || ObjectUtil.equals(env, "pro")) {
            //todo
            DING_NOTIFY_URL = "";
            DING_KEYWORD = "";
        }
    }

    String getMetaInfo() {
        HttpServletRequest request = JkServletUtil.getRequest();
        return "异常url : [" + request.getRequestURI() + "]";
    }


    /**
     * 包装类参数校验异常处理
     *
     * @param e 异常
     * @return 异常说明信息
     */
    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class, MissingServletRequestParameterException.class, MethodArgumentTypeMismatchException.class})
    public Result<?> handleParamsException(Exception e) {
        e.printStackTrace();
        String message = "";
        BindingResult bindResult = null;
        if (e instanceof MethodArgumentNotValidException) {
            bindResult = ((MethodArgumentNotValidException) e).getBindingResult();
        } else if (e instanceof BindException) {
            bindResult = ((BindException) e).getBindingResult();
        }
        if (bindResult == null) {
            message += e.getMessage();
        } else {
            message += bindResult.getFieldErrors().stream()
                    .filter(x -> x.getDefaultMessage() != null)
                    .map(x -> x.getDefaultMessage().contains(x.getField()) ? x.getDefaultMessage() : x.getField() + ": " + x.getDefaultMessage())
                    .collect(Collectors.joining(";"));
        }
        return ResultGenerator.genFailResult(ResultCode.ARG_VALID_FAIL, message);
    }

    /**
     * satoken账户校验未登录
     *
     * @param e
     * @return
     */
    @ExceptionHandler({NotLoginException.class})
    public Result<?> notLoginExceptionHandle(Exception e) {
        log.error("[账户校验异常]" + getMetaInfo() + ", " + e.getMessage(), e);
        return ResultGenerator.genFailResult(ResultCode.RELOGIN);
    }
    @ExceptionHandler({NotRoleException.class})
    public Result<?> notRoleExceptionHandle(Exception e) {
        log.error("[账户权限异常]" + getMetaInfo() + ", " + e.getMessage(), e);
        return ResultGenerator.genFailResult(ResultCode.PERMISSION_DENY);
    }
    @ExceptionHandler({JwtLoginException.class})
    public Result<?> jwtExceptionHandle(Exception e) {
        log.error("[JWT校验异常]" + getMetaInfo() + ", " + e.getMessage(), e);
        if (StringUtils.isNoneBlank(e.getMessage())) {
            return ResultGenerator.genFailResult(ResultCode.RELOGIN, e.getMessage());
        } else {
            return ResultGenerator.genFailResult(ResultCode.RELOGIN);
        }
    }

    /**
     * 业务异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(BizException.class)
    public Result<?> bizExceptionHandle(BizException e) {
        String info = getMetaInfo();
        log.error("[业务异常]" + info + ", " + e.getMessage());
        taskExecutor.execute(() -> {
            sendDingMsg("[业务异常]" + info + ", " + e.getMessage());
        });
        return ResultGenerator.genFailResult(ResultCode.BIZ_ERR, e.getMessage());
    }

    /**
     * 路径/请求方式错误
     *
     * @param e
     * @return
     */
    @ExceptionHandler({NoHandlerFoundException.class, HttpRequestMethodNotSupportedException.class})
    public Result<?> noHandlerExceptionHandle(NoHandlerFoundException e) {
        log.error("[路径异常]" + getMetaInfo() + ", " + e.getMessage(), e);
        return ResultGenerator.genFailResult(ResultCode.NOT_FOUND, "接口不存在");
    }

    /**
     * SaToken权限异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(NotPermissionException.class)
    public Result<?> permissionExceptionHandle(NotPermissionException e) {
        log.error("[权限异常]" + getMetaInfo() + ", " + e.getMessage(), e);
        return ResultGenerator.genFailResult(ResultCode.PERMISSION_DENY, "无访问权限");
    }

    /**
     * 其他未知异常， code=500, 返回服务器异常
     *
     * @param e 未知Exception
     */
    @ExceptionHandler(Exception.class)
    public Result<?> exceptionHandle(Exception e) {
        String info = getMetaInfo();
        log.error("[系统开小差]" + info + ", " + e.getMessage(), e);
        taskExecutor.execute(() -> {
            sendDingMsg("[系统开小差]" + info + ", " + e.getMessage());
        });
        return ResultGenerator.genFailResult(ResultCode.INTERNAL_SERVER_ERROR);
    }

    private void sendDingMsg(String msg) {
        if (StringUtils.isNotBlank(DING_NOTIFY_URL)) {
            DingTalkTextMsgDTO dto = new DingTalkTextMsgDTO();
            dto.setContent(DING_KEYWORD + ":" + msg);
            dto.setAtAll(Boolean.FALSE);
            DingTalkUtils.sendText(DING_NOTIFY_URL, dto.toJsonString());
        }
    }


}
