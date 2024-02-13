package com.mlinyun.cloudstorage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 前台批量删除文件接口参数载体
 */
@Data
@Schema(description = "批量删除文件DTO", requiredMode = Schema.RequiredMode.REQUIRED)
public class BatchDeleteFileDTO {

    /**
     * 文件集合
     */
    @Schema(description="文件集合")
    private String files;

}
