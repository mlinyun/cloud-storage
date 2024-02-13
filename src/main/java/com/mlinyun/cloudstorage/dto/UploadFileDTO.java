package com.mlinyun.cloudstorage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 前台上传文件接口请求参数载体
 */
@Data
@Schema(name = "上传文件DTO", requiredMode = Schema.RequiredMode.REQUIRED)
public class UploadFileDTO {

    /**
     * 文件路径
     */
    @Schema(description = "文件路径")
    private String filePath;

    /**
     * 上传时间
     */
    @Schema(description = "上传时间")
    private String uploadTime;

    /**
     * 扩展名
     */
    @Schema(description = "扩展名")
    private String extendName;

    /**
     * 文件名
     */
    @Schema(description = "文件名")
    private String filename;

    /**
     * 文件大小
     */
    @Schema(description = "文件大小")
    private Long fileSize;

    /**
     * 切片数量
     */
    @Schema(description = "切片数量")
    private int chunkNumber;

    /**
     * 切片大小
     */
    @Schema(description = "切片大小")
    private long chunkSize;

    /**
     * 所有切片
     */
    @Schema(description = "所有切片")
    private int totalChunks;

    /**
     * 总大小
     */
    @Schema(description = "总大小")
    private long totalSize;

    /**
     * 当前切片大小
     */
    @Schema(description = "当前切片大小")
    private long currentChunkSize;

    /**
     * md5码
     */
    @Schema(description = "md5码")
    private String identifier;

}
