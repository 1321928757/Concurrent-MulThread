package com.luckysj.demo.model.response;


public enum ResponseCodeEnum {
    /**
     * 请求成功
     */
    SUCCESS("0000", ErrorLevels.DEFAULT, ErrorTypes.SYSTEM, "请求成功"),
    /**
     * 登录相关异常
     */
    LOGIN_USER_INFO_CHECK("LOGIN-0001", ErrorLevels.INFO, ErrorTypes.BIZ, "用户信息错误"),
    /**
     * 权限相关异常
     */
    NO_PERMISSIONS("PERM-0001", ErrorLevels.INFO, ErrorTypes.BIZ, "用户无权限"),
    /**
     * 业务相关异常
     */
    BIZ_CHECK_FAIL("BIZ-0001", ErrorLevels.INFO, ErrorTypes.BIZ, "业务检查异常"),
    BIZ_STATUS_ILLEGAL("BIZ-0002", ErrorLevels.INFO, ErrorTypes.BIZ, "业务状态非法"),
    BIZ_QUERY_EMPTY("BIZ-0003", ErrorLevels.INFO, ErrorTypes.BIZ, "查询信息为空"),
    /**
     * 系统出错
     */
    SYSTEM_EXCEPTION("SYS-0001", ErrorLevels.ERROR, ErrorTypes.SYSTEM, "系统出错啦，请稍后重试"),
    ;

    /**
     * 枚举编码
     */
    private final String code;

    /**
     * 错误级别
     */
    private final String errorLevel;

    /**
     * 错误类型
     */
    private final String errorType;

    /**
     * 描述说明
     */
    private final String description;

    ResponseCodeEnum(String code, String errorLevel, String errorType, String description) {
        this.code = code;
        this.errorLevel = errorLevel;
        this.errorType = errorType;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getErrorLevel() {
        return errorLevel;
    }

    public String getErrorType() {
        return errorType;
    }

    public String getDescription() {
        return description;
    }


    public static ResponseCodeEnum getByCode(Integer code) {
        for (ResponseCodeEnum value : values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return SYSTEM_EXCEPTION;
    }

}

