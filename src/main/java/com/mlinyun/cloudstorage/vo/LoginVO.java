package com.mlinyun.cloudstorage.vo;

import lombok.Data;

/**
 * 前台登录接口响应参数
 */
@Data
public class LoginVO {

    /**
     * 用户名
     */
    private String username;
    /**
     * token
     */
    private String token;
}
