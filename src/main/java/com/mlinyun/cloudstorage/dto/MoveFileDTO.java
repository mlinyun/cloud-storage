package com.mlinyun.cloudstorage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 前台移动文件接口请求参数载体
 */
@Data
@Schema(description = "移动文件DTO", requiredMode = Schema.RequiredMode.REQUIRED)
public class MoveFileDTO {

    /**
     * 文件路径
     */
    @Schema(description = "文件路径")
    private String filePath;

    /**
     * 文件名
     */
    @Schema(description = "文件名")
    private String fileName;

    /**
     * 旧文件名
     */
    @Schema(description = "旧文件名")
    private String oldFilePath;

    /**
     * 扩展名
     */
    @Schema(description = "扩展名")
    private String extendName;

}
