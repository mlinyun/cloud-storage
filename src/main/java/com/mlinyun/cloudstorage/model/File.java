package com.mlinyun.cloudstorage.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.*;
import lombok.Data;

/**
 * 文件实体类
 */
@Data
@TableName("file")
@Table(name = "file")
@Entity
public class File {

    @Id
    @TableId(type = IdType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint(20) comment '文件ID'")
    private Long fileId;

    @Column(columnDefinition = "varchar(500) comment '时间戳名称'")
    private String timeStampName;

    @Column(columnDefinition = "varchar(500) comment '文件URL'")
    private String fileUrl;

    @Column(columnDefinition = "bigint(10) comment '文件大小'")
    private Long fileSize;

    @Column(columnDefinition = "int(1) comment '存储类型 0-本地存储, 1-阿里云存储, 2-FastDFS存储'")
    private Integer storageType;

    @Column(columnDefinition = "varchar(32) comment 'md5唯一标识'")
    private String identifier;

    @Column(columnDefinition = "int(1) comment '引用数量'")
    private Integer pointCount;

}
