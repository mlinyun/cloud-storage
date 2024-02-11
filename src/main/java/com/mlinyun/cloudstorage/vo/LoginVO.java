package com.mlinyun.cloudstorage.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 前台登录接口响应参数
 */
@Data
@Schema(description="登录VO")
public class LoginVO {

    /**
     * 用户名
     */
    @Schema(description="用户名")
    private String username;
    /**
     * token
     */
    @Schema(description="token")
    private String token;
}
