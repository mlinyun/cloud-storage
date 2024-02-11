package com.mlinyun.cloudstorage.config.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT 配置类
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt") // 该注解可读取配置文件中以 jwt 开头的配置
public class JwtProperties {

    /**
     * JWT 密钥
     */
    private String secret;

    /**
     * JWT 第一部分：标头
     */
    private JwtHeader header;

    /**
     * JWT 第二部分：有效负载
     */
    private JwtPayload payload;
}
