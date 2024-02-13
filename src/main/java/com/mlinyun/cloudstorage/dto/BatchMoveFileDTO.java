package com.mlinyun.cloudstorage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 前台批量移动文件接口请求参数载体
 */
@Data
@Schema(description = "批量移动文件DTO", requiredMode = Schema.RequiredMode.REQUIRED)
public class BatchMoveFileDTO {

    /**
     * 文件集合
     */
    @Schema(description = "文件集合")
    private String files;

    /**
     * 文件路径
     */
    @Schema(description = "文件路径")
    private String filePath;

}
