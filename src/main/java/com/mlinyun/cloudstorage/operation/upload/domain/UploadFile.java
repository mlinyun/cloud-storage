package com.mlinyun.cloudstorage.operation.upload.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 本地上传文件实体类
 */
@Data
@Schema(description = "本地上传文件实体类")
public class UploadFile {

    /**
     * 文件名
     */
    @Schema(description = "文件名")
    private String fileName;

    /**
     * 文件类型
     */
    @Schema(description = "文件类型")
    private String fileType;

    /**
     * 文件大小
     */
    @Schema(description = "文件大小")
    private long fileSize;

    /**
     * 时间戳名称
     */
    @Schema(description = "时间戳名称")
    private String timeStampName;

    /**
     * 上传结果 1-上传成功 0-未完成
     */
    @Schema(description = "上传结果")
    private int success;

    /**
     * 消息
     */
    @Schema(description = "消息")
    private String message;

    /**
     * 文件URL
     */
    @Schema(description = "文件URL")
    private String url;

    /* ---------- 切片上传相关参数 ---------- */
    /**
     * 切片任务ID
     */
    @Schema(description = "切片任务ID")
    private String taskId;

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
     * md5码
     */
    @Schema(description = "md5码")
    private String identifier;

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

}
