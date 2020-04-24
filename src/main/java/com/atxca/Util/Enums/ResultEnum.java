package com.atxca.Util.Enums;

/**
 * Created by 廖师兄
 * 2017-01-21 14:23
 */
public enum ResultEnum {
    SUCCESS(0, "成功"),
    NULL_USER(100100, "用户不存在!"),
    USER_REPEAT(100101, "用户不存在!"),
    USER_NOT_NULL(100101, "用户以存在!"),
    USER_PRESENCE(100100, "用户手机号已经存在!"),
    STATUS_NOT_ZERO(100122, "场地不能被预约请刷新!"),
    SITE_OCCUPIED(100122, "不能预约,本场地改当前时间,已经被预约!!!"),
    SITE_OCCUPIED_TWO(100123, "不能预约,订单中存在其他用户预约"),
    WX_LOGIN_ERROR(11111,"微信登录异常")

    ;
    private Integer code;

    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
