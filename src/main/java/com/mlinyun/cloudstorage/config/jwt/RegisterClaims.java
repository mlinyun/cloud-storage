package com.mlinyun.cloudstorage.config.jwt;

import lombok.Data;

/**
 * Claims
 * 关于实体（通常为用户）和其他数据的语句
 */
@Data
public class RegisterClaims {

    /**
     * jwt 签发者
     */
    private String iss;

    /**
     * jwt 过期时间
     */
    private String exp;

    /**
     * 主题
     */
    private String sub;

    /**
     * jwt 接收者
     */
    private String aud;
}
