package com.mlinyun.cloudstorage.exception;

import com.mlinyun.cloudstorage.common.ResultCodeEnum;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    /**
     * 结果
     */
    private final boolean success;

    /**
     * 异常状态码
     */
    private final int code;

    public BusinessException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.success = resultCodeEnum.getSuccess();
        this.code = resultCodeEnum.getCode();
    }

    public BusinessException(ResultCodeEnum resultCodeEnum, String message) {
        super(message);
        this.success = resultCodeEnum.getSuccess();
        this.code = resultCodeEnum.getCode();
    }
}
