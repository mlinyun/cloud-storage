package com.mlinyun.cloudstorage.operation.delete.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 删除文件实体类
 */
@Data
@Schema(description = "删除文件实体类")
public class DeleteFile {

    /**
     * 文件URL
     */
    @Schema(description = "文件URL")
    private String fileUrl;

    /**
     * 时间戳名称
     */
    @Schema(description = "时间戳名称")
    private String timeStampName;

}
