package com.mlinyun.cloudstorage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 前台注册接口请求参数：用来存放用户注册接口请求参数
 */
@Data
@Schema(description="注册DTO")
public class RegisterDTO {
    /**
     * 用户名
     */
    @Schema(description="用户名")
    private String username;
    /**
     * 手机号
     */
    @Schema(description="手机号")
    private String telephone;

    /**
     * 密码
     */
    @Schema(description="密码")
    private String password;
}
