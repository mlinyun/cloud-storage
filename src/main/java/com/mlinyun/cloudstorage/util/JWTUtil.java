package com.mlinyun.cloudstorage.util;

import com.mlinyun.cloudstorage.config.jwt.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
        // 本地密钥解码
        byte[] encodedKey = Base64.decodeBase64(jwtProperties.getSecret());
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
        String TOKEN_TYP = jwtProperties.getHeader().getTyp();
        // 签名算法
        String ALGORITHM = jwtProperties.getHeader().getAlg();

        // 签发者
        String JWT_ISS = jwtProperties.getPayload().getRegisterClaims().getIss();
        // 接收者
        String JWT_AUD = jwtProperties.getPayload().getRegisterClaims().getAud();

        // 设置过期时间
        ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
        ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("js");
        int expireTime = 0;
        try {
            expireTime = (int) scriptEngine.eval(jwtProperties.getPayload().getRegisterClaims().getExp());
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
        Claims claims = (Claims) Jwts.builder().claims()
                // 签发者
                .issuer(JWT_ISS)
                // 过期日期
                .expiration(new Date(System.currentTimeMillis() + expireTime))
                // 主题
                .subject(subject)
                // 接收者
                .audience().add(JWT_AUD);
        JwtBuilder builder = Jwts.builder()
                // 设置头部信息 header
                .header().add("typ", TOKEN_TYP).add("alg", ALGORITHM).and()
                // 设置自定义负载信息 payload
                .claims(claims)
                // iat(issueAt): jwt 签发时间
                .issuedAt(nowDate)
                // 签名
                .signWith(SignatureAlgorithm.valueOf(ALGORITHM), key);
        return builder.compact();
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
        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt).getBody();
        return claims;
    }
}
