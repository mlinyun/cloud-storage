package com.mlinyun.cloudstorage.dto;

import lombok.Data;

/**
 * 前台登录接口请求参数
 */
@Data
public class LoginDTO {

    /**
     * 手机号
     */
    private String telephone;

    /**
     * 密码
     */
    private String password;
}
