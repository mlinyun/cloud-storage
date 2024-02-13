package com.mlinyun.cloudstorage.operation.download.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 本地下载文件实体类
 */
@Data
@Schema(description = "本地下载文件实体类")
public class DownloadFile {

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
