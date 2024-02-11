package com.mlinyun.cloudstorage.common;

import lombok.Data;

/**
 * 统一结果返回类
 */
@Data
public class RestResult<T> {

    /**
     * 结果
     */
    private Boolean success = true;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 信息
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    public RestResult() {

    }

    public RestResult(Boolean success, Integer code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 通用返回成功
     *
     * @return RestResult
     */
    public static <T> RestResult<T> success() {
        RestResult<T> restResult = new RestResult<>();
        restResult.setSuccess(ResultCodeEnum.SUCCESS.getSuccess());
        restResult.setCode(ResultCodeEnum.SUCCESS.getCode());
        restResult.setMessage(ResultCodeEnum.SUCCESS.getMessage());
        return restResult;
    }

    /**
     * 通用返回失败，未知错误
     *
     * @return RestResult
     */
    public static <T> RestResult<T> fail() {
        RestResult<T> restResult = new RestResult<>();
        restResult.setSuccess(ResultCodeEnum.UNKNOWN_ERROR.getSuccess());
        restResult.setCode(ResultCodeEnum.UNKNOWN_ERROR.getCode());
        restResult.setMessage(ResultCodeEnum.UNKNOWN_ERROR.getMessage());
        return restResult;
    }

    /**
     * 自定义返回数据
     *
     * @param param 自定义返回数据
     * @return RestResult
     */
    public RestResult data(T param) {
        this.setData(param);
        return this;
    }

    /**
     * 自定义状态信息
     *
     * @param message 自定义状态信息
     * @return RestResult
     */
    public RestResult<String> message(String message) {
        this.setMessage(message);
        return (RestResult<String>) this;
    }

    /**
     * 自定义状态码
     *
     * @param code 自定义状态码
     * @return RestResult
     */
    public RestResult<T> code(Integer code) {
        this.setCode(code);
        return this;
    }

    /**
     * 设置结果，形参未结果枚举类
     *
     * @param resultCodeEnum 结果枚举类
     * @return RestResult
     */
    public static <T> RestResult<T> setResult(ResultCodeEnum resultCodeEnum) {
        RestResult<T> restResult = new RestResult<>();
        restResult.setSuccess(resultCodeEnum.getSuccess());
        restResult.setCode(resultCodeEnum.getCode());
        restResult.setMessage(resultCodeEnum.getMessage());
        return restResult;
    }
}
