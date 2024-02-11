package com.mlinyun.cloudstorage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 前台登录接口请求参数
 */
@Data
@Schema(description = "登录DTO")
public class LoginDTO {

    /**
     * 手机号
     */
    @Schema(description = "手机号")
    private String telephone;

    /**
     * 密码
     */
    @Schema(description = "密码")
    private String password;
}
