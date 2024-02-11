package com.mlinyun.cloudstorage;

import com.mlinyun.cloudstorage.config.jwt.JwtProperties;
import com.mlinyun.cloudstorage.mapper.UserMapper;
import com.mlinyun.cloudstorage.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.Resource;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.Date;
import java.util.List;

@SpringBootTest
class CloudStorageApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
    }

    /**
     * MyBatis UserMapper 接口测试
     */
    @Test
    public void testUserMapper1() {
        User user = new User();
        user.setUsername("用户名1");
        user.setPassword("密码1");
        user.setTelephone("手机号1");
        userMapper.insertUser(user);
        System.out.println("数据库字段查询结果显示");
        List<User> list = userMapper.selectUser();
        list.forEach(System.out::println);
    }

    @Test
    public void testUserMapper2() {
        User user = new User();
        user.setUsername("用户名2");
        user.setPassword("密码2");
        user.setTelephone("手机号2");
        userMapper.insert(user);
        List<User> list = userMapper.selectList(null);
        System.out.println("数据库字段查询结果显示");
        list.forEach(System.out::println);
    }

}
