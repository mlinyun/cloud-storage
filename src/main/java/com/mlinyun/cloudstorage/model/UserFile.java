package com.mlinyun.cloudstorage.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.*;
import lombok.Data;

/**
 * 用户文件实体类
 */
@Data
@TableName("user_file")
@Table(name = "user_file", uniqueConstraints = {
        @UniqueConstraint(name = "file_index", columnNames = {"fileName", "filePath", "extendName"})})
@Entity
public class UserFile {

    @Id
    @TableId(type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint(20) comment '用户文件ID'")
    private Long userFileId;

    @Column(columnDefinition = "bigint(20) comment '用户ID'")
    private Long userId;

    @Column(columnDefinition = "bigint(20) comment '文件ID'")
    private Long fileId;

    @Column(columnDefinition = "varchar(100) comment '文件名'")
    private String fileName;

    @Column(columnDefinition = "varchar(500) comment '文件路径'")
    private String filePath;

    @Column(columnDefinition = "varchar(100) comment '扩展名'")
    private String extendName;

    @Column(columnDefinition = "int(1) comment '是否是目录 0-否, 1-是'")
    private Integer isDir;

    @Column(columnDefinition = "varchar(25) comment '上传时间'")
    private String uploadTime;

    @TableLogic
    @Column(columnDefinition = "int(11) comment '删除标志 0-未删除 1-已删除'")
    private Integer deleteFlag;

    @Column(columnDefinition = "varchar(25) comment '删除时间'")
    private String deleteTime;

    @Column(columnDefinition = "varchar(50) comment '删除批次号'")
    private String deleteBatchNum;

}
