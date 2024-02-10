package com.mlinyun.cloudstorage.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 文件实体类
 */
@Data
@Table(name = "file")
@Entity
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint(20) comment '文件ID'")
    private Long fileId;

    @Column(columnDefinition = "varchar(500) comment '时间戳名称'")
    private String timeStampName;

    @Column(columnDefinition = "varchar(500) comment '文件URL'")
    private String fileUrl;

    @Column(columnDefinition = "bigint(10) comment '文件大小'")
    private Long fileSize;

}
