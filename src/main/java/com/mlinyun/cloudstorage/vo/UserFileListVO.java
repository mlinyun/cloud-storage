package com.mlinyun.cloudstorage.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 前台文件夹列表查询接口响应参数：用来作为文件列表查询接口给前台返回的信息载体
 */
@Data
@Schema(description = "用户文件夹列表VO")
public class UserFileListVO {

    /**
     * 文件ID
     */
    @Schema(description = "文件ID")
    private Long fileId;

    /**
     * 时间戳名称
     */
    @Schema(description = "时间戳名称")
    private String timeStampName;

    /**
     * 文件URL
     */
    @Schema(description = "文件URL")
    private String fileUrl;

    /**
     * 文件大小
     */
    @Schema(description = "文件大小")
    private Long fileSize;

    /**
     * 是否是oss存储
     */
    @Schema(description = "是否是oss存储")
    private Integer isOSS;

    /**
     * 引用数量
     */
    @Schema(description = "引用数量")
    private Integer pointCount;

    /**
     * md5
     */
    @Schema(description = "md5")
    private String identifier;

    /**
     * 用户文件ID
     */
    @Schema(description = "用户文件ID")
    private Long userFileId;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private Long userId;

    /**
     * 文件名
     */
    @Schema(description = "文件名")
    private String fileName;

    /**
     * 文件路径
     */
    @Schema(description = "文件路径")
    private String filePath;

    /**
     * 扩展名
     */
    @Schema(description = "扩展名")
    private String extendName;

    /**
     * 是否是目录
     */
    @Schema(description = "是否是目录")
    private Integer isDir;

    /**
     * 上传时间
     */
    @Schema(description = "上传时间")
    private String uploadTime;
}
