package com.mlinyun.cloudstorage.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 前台文上传文件接口响应参数载体
 */
@Data
@Schema(name = "上传文件VO", requiredMode = Schema.RequiredMode.REQUIRED)
public class UploadFileVO {

    /**
     * 时间戳
     */
    @Schema(description = "时间戳", example = "123123123123")
    private String timeStampName;

    /**
     * 跳过上传
     */
    @Schema(description = "跳过上传", example = "true")
    private boolean skipUpload;

    /**
     * 是否需要合并分片
     */
    @Schema(description = "是否需要合并分片", example = "true")
    private boolean needMerge;

    /**
     * 已经上传的分片
     */
    @Schema(description = "已经上传的分片", example = "[1,2,3]")
    private List<Integer> uploaded;

}
