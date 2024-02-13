package com.mlinyun.cloudstorage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 前台下载文件接口请求参数载体
 */
@Data
@Schema(name = "下载文件DTO", requiredMode = Schema.RequiredMode.REQUIRED)
public class DownloadFileDTO {

    /**
     * 用户文件ID
     */
    @Schema(description = "用户文件ID")
    private Long userFileId;

}
