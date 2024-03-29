package com.mlinyun.cloudstorage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mlinyun.cloudstorage.common.RestResult;
import com.mlinyun.cloudstorage.common.ResultCodeEnum;
import com.mlinyun.cloudstorage.dto.LoginDTO;
import com.mlinyun.cloudstorage.dto.RegisterDTO;
import com.mlinyun.cloudstorage.exception.BusinessException;
import com.mlinyun.cloudstorage.model.User;
import com.mlinyun.cloudstorage.service.UserService;
import com.mlinyun.cloudstorage.util.JWTUtil;
import com.mlinyun.cloudstorage.vo.LoginVO;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 用户接口
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Tag(name = "用户接口", description = "该接口为用户接口，主要做用户登录，注册和校验token")
public class UserController {

    /**
     * 注入用户服务
     */
    @Resource
    UserService userService;

    /**
     * 注入 JWT 工具类
     */
    @Resource
    JWTUtil jwtUtil;

    /**
     * 用户注册接口
     *
     * @param registerDTO 前台注册接口请求参数
     * @return 注册结果
     */
    @ResponseBody
    @PostMapping(value = "/register")
    @Operation(summary = "用户注册", description = "注册账号", tags = {"用户接口"})
    public RestResult<String> userRegister(@RequestBody RegisterDTO registerDTO) {
        // 非空判断
        if (registerDTO == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_NULL);
        }
        RestResult<String> result = null;
        // 获取用户注册信息
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setTelephone(registerDTO.getTelephone());
        user.setPassword(registerDTO.getPassword());
        // 调用用户注册服务
        result = userService.registerUser(user);
        return result;
    }

    /**
     * 用户登录接口
     *
     * @param loginDTO 前台登录接口请求参数
     * @return 登录结果
     */
    @ResponseBody
    @PostMapping(value = "/login")
    @Operation(summary = "用户登录", description = "用户登录认证后才能进入系统", tags = {"用户接口"})
    public RestResult<LoginVO> userLogin(@RequestBody LoginDTO loginDTO) {
        // 非空判断
        if (loginDTO == null) {
            throw new BusinessException(ResultCodeEnum.PARAM_NULL);
        }
        RestResult<LoginVO> restResult = new RestResult<>();
        LoginVO loginVO = new LoginVO();
        // 设置用户登录信息
        User user = new User();
        user.setTelephone(loginDTO.getTelephone());
        user.setPassword(loginDTO.getPassword());
        // 调用用户登录服务
        RestResult<User> loginResult = userService.login(user);
        // 设置响应参数 - 用户名
        loginVO.setUsername(loginResult.getData().getUsername());
        // 设置响应参数 - token
        String jwt = "";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            jwt = jwtUtil.createJWT(objectMapper.writeValueAsString(loginResult.getData()));
        } catch (Exception e) {
            throw new BusinessException(ResultCodeEnum.UNKNOWN_ERROR, "用户登录失败！");
        }
        loginVO.setToken(jwt);
        // 返回登录成功
        return RestResult.success().data(loginVO);
    }

    /**
     * token 校验接口
     *
     * @param token 前台请求头携带的 token
     * @return 校验结果
     */
    @ResponseBody
    @GetMapping(value = "/checkUserLoginInfo")
    @Operation(summary = "检查用户登录信息", description = "验证token的有效性", tags = {"用户接口"})
    public RestResult<User> checkToken(@RequestHeader("token") String token) {
        RestResult<User> restResult = new RestResult<>();
        User tokenUserInfo = null;
        try {
            Claims claims = jwtUtil.parseJWT(token);
            String subject = claims.getSubject();
            ObjectMapper objectMapper = new ObjectMapper();
            tokenUserInfo = objectMapper.readValue(subject, User.class);
        } catch (Exception e) {
            log.error("解码异常" + e.getMessage());
            throw new BusinessException(ResultCodeEnum.UNKNOWN_ERROR, "解码异常！");
        }
        if (tokenUserInfo != null) {
            return RestResult.success().data(tokenUserInfo);
        } else {
            throw new BusinessException(ResultCodeEnum.PARAM_NULL, "token 为空");
        }
    }
}
