package com.mlinyun.cloudstorage.dto;

import lombok.Data;

/**
 * 前台注册接口请求参数
 */
@Data
public class RegisterDTO {
    /**
     * 用户名
     */
    private String username;
    /**
     * 手机号
     */
    private String telephone;

    /**
     * 密码
     */
    private String password;
}
