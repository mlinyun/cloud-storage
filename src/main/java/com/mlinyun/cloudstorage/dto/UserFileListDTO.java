package com.mlinyun.cloudstorage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 前台文件夹列表查询接口请求参数：用来作为文件列表查询接口接收前台请求信息的载体
 */
@Data
@Schema(description = "文件列表DTO", requiredMode = Schema.RequiredMode.REQUIRED)
public class UserFileListDTO {

    /**
     * 文件路径
     */
    @Schema(description = "文件路径")
    private String filePath;

    /**
     * 当前页码
     */
    @Schema(description = "当前页码")
    private Long currentPage;

    /**
     * 一页显示数量
     */
    @Schema(description = "一页显示数量")
    private Long pageCount;
}
