package com.mlinyun.cloudstorage.util;

import com.mlinyun.cloudstorage.config.jwt.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Date;

/**
 * JWT 工具类
 */
@Slf4j
@Component
public class JWTUtil {

    /**
     * JWT 配置类
     */
    @Resource
    JwtProperties jwtProperties;

    /**
     * 由本地密钥 secret 生成加密 key
     *
     * @return 加密后的 key
     */
    private SecretKey generalKey() {
        // 从配置文件中获取本地密钥
        String secret = jwtProperties.getSecret();
        // 本地密钥解码
        byte[] encodedKey = Base64.decodeBase64(secret);
        // 根据给定的字节数组使用AES加密算法构造一个密钥
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    /**
     * 创建 JWT
     *
     * @param subject 主题
     * @return 创建完成的 token
     */
    public String createJWT(String subject) throws Exception {

        // token 类型
        final String TOKEN_TYP = jwtProperties.getHeader().getTyp();
        // 签名算法
        final String ALGORITHM = jwtProperties.getHeader().getAlg();
        // 签发者
        final String JWT_ISS = jwtProperties.getPayload().getRegisterClaims().getIss();
        // 接收者
        final String JWT_AUD = jwtProperties.getPayload().getRegisterClaims().getAud();
        // 失效时间
        final String obsoleteTime = jwtProperties.getPayload().getRegisterClaims().getExp();

        // 设置过期时间
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("javascript");
        int expireTime = 0;
        try {
            expireTime = (int) scriptEngine.eval(obsoleteTime);
        } catch (ScriptException scriptException) {
            log.error(scriptException.getMessage());
        }

        // 生成 JWT 的时间
        long nowTime = System.currentTimeMillis();
        Date nowDate = new Date(nowTime);
        // 生成签名的时候使用的密钥 secret，切记这个秘钥不能外露，是你服务端的私钥，
        // 在任何场景都不应该流露出去，一旦客户端得知这个 secret，那就意味着客户端是可以自我签发 jwt 的
        SecretKey key = generalKey();

        // 为 payload 添加各种标准声明和私有声明
        DefaultClaims defaultClaims = new DefaultClaims();
        defaultClaims.setIssuer(JWT_ISS);
        defaultClaims.setExpiration(new Date(System.currentTimeMillis() + expireTime));
        defaultClaims.setSubject(subject);
        defaultClaims.setAudience(JWT_AUD);

        JwtBuilder jwtBuilder = Jwts.builder()  // 表示 new 一个 JwtBuilder，设置 jwt 的 body
                .setClaims(defaultClaims)
                .setIssuedAt(nowDate)   // iat(issuedAt)：jwt的签发时间
                .signWith(SignatureAlgorithm.forName(ALGORITHM), key);  // 设置签名，使用的是签名算法和签名使用的密钥
        return jwtBuilder.compact();
    }

    /**
     * 解码 JWT
     *
     * @param jwt 要解密的 JWT
     * @return 解密后的 JWT
     * @throws Exception 异常
     */
    public Claims parseJWT(String jwt) throws Exception {
        SecretKey key = generalKey();   // 签名密钥，和生成的签名的密钥一模一样
        Claims claims = Jwts.parser()   // 得到DefaultJwtParser
                .setSigningKey(key)     // 设置签名的秘钥
                .parseClaimsJws(jwt).getBody(); // 设置需要解析的jwt
        return claims;
    }
}
