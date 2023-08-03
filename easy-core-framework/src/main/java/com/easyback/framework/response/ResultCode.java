package com.easyback.framework.response;

/**
 * 响应码枚举，参考HTTP状态码的语义
 */
public enum ResultCode {

    SUCCESS(200,"操作成功"),//成功

    CODE_ERR(301,"验证码错误"),

    ARG_VALID_FAIL(302, "参数校验失败"),
    BIZ_ERR(303, "业务异常"),
    FAIL(400,"操作失败"),//失败
    RELOGIN(401,"请重新登录"),//重新登录,
    PERMISSION_DENY(402,"无访问权限"),//重新登录
    UNAUTHORIZED(403,"未认证（签名错误）"),//未认证（签名错误）
    NOT_FOUND(404,"请求路径不存在"),//接口不存在

    INTERNAL_SERVER_ERROR(500,"系统开小差"),//服务器内部错误

    FACE_REQUIRE(600,"需要进行人脸核验"),//需要进行人脸核验
    AUTH_REQUIRE(601,"需要进行实名认证"),//需要进行实名认证

    //sign error
    SIGN_NO_APPID(10001, "appId不能为空"),
    SIGN_NO_TIMESTAMP(10002, "timestamp不能为空"),
    SIGN_NO_SIGN(10003, "sign不能为空"),
    SIGN_NO_NONCE(10004, "nonce不能为空"),
    SIGN_TIMESTAMP_INVALID(10005, "timestamp无效"),
    SIGN_DUPLICATION(10006, "重复的请求"),
    SIGN_VERIFY_FAIL(10007, "sign签名校验失败"),

    ;
    public Integer code;

    public String message;

    ResultCode(Integer code,String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
