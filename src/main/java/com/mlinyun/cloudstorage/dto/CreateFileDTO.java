package com.mlinyun.cloudstorage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 前台文件创建接口请求参数：用来存放文件创建请求参数
 */
@Data
@Schema(description = "创建文件DTO", requiredMode = Schema.RequiredMode.REQUIRED)
public class CreateFileDTO {

    /**
     * 文件名
     */
    @Schema(description = "文件名")
    private String fileName;

    /**
     * 文件路径
     */
    @Schema(description = "文件路径")
    private String filePath;
}
