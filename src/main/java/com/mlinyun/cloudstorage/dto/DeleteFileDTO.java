package com.mlinyun.cloudstorage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 前台删除文件接口请求参数载体
 */
@Data
@Schema(description = "删除文件DTO", requiredMode = Schema.RequiredMode.REQUIRED)
public class DeleteFileDTO {

    /**
     * 用户文件ID
     */
    @Schema(description = "用户文件ID")
    private Long userFileId;

    /**
     * 文件路径
     */
    @Deprecated
    @Schema(description = "文件路径")
    private String filePath;

    /**
     * 文件名
     */
    @Deprecated
    @Schema(description = "文件名")
    private String fileName;

    /**
     * 是否是目录
     */
    @Deprecated
    @Schema(description = "是否是目录")
    private Integer isDir;

}
