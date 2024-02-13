package com.mlinyun.cloudstorage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 前台重命名文件接口参数载体
 */
@Data
@Schema(description = "重命名文件DTO", requiredMode = Schema.RequiredMode.REQUIRED)
public class RenameFileDTO {

    /**
     * 用户文件ID
     */
    @Schema(description = "用户文件ID")
    private Long userFileId;

    /**
     * 文件名
     */
    @Schema(description = "文件名")
    private String fileName;
}
