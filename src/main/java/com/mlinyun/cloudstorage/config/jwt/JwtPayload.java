package com.mlinyun.cloudstorage.config.jwt;

import lombok.Data;

/**
 * JWT 的第二部分：Payload
 * JWT 的第二部分是有效负载，其中包含 claims。claims 是关于实体（通常为用户）和其他数据的语句。
 * 然后对有效负载进行 Base64Url 编码，以形成 JSON Web 令牌的第二部分
 */
@Data
public class JwtPayload {
    private RegisterClaims registerClaims;
}
