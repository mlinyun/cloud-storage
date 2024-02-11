package com.mlinyun.cloudstorage.config.jwt;

import lombok.Data;

/**
 * JWT 的第一部分：Header
 * 标头通常由两部分组成：token 的类型（typ）和正在使用的签名算法（alg），如 HMAC SHA256 或 RSA。
 * 然后编码为 Base64Url，以形成 JWT 的第一部分
 */
@Data
public class JwtHeader {

    /**
     * 签名算法
     */
    private String alg;

    /**
     * token 类型
     */
    private String typ;
}
