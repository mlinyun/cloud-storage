package com.mlinyun.cloudstorage.common;

import lombok.Getter;

/**
 * 结果枚举类
 */
@Getter // lombok 提供的注解，会自动为该类添加 getter 方法
public enum ResultCodeEnum {

    SUCCESS(true, 20000, "请求成功"),
    UNKNOWN_ERROR(false, 20001, "未知错误"),
    PARAM_ERROR(false, 20002, "参数错误"),
    PARAM_NULL(false, 20003, "参数为空"),
    NULL_POINT(false, 20004, "空指针异常"),
    INDEX_OUT_OF_BOUNDS(false, 20005, "下标越界异常");

    /**
     * 响应是否成功
     */
    private final Boolean success;

    /**
     * 响应状态码
     */
    private final Integer code;

    /**
     * 响应信息
     */
    private final String message;

    ResultCodeEnum(Boolean success, Integer code, String message) {
        this.success = success;
        this.code = code;
        this.message = message;
    }
}
