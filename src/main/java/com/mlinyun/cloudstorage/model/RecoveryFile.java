package com.mlinyun.cloudstorage.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.*;
import lombok.Data;

/**
 * 恢复文件实体类
 * 每次删除文件操作都会记录到该表，用于后续文件恢复使用
 */
@Data
@Table(name = "recovery_file")
@TableName("recovery_file")
@Entity
public class RecoveryFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    @Column(columnDefinition = "bigint(20) comment '可恢复文件ID'")
    private Long recoveryFileId;

    @Column(columnDefinition = "bigint(20) comment '用户文件ID'")
    private Long userFileId;

    @Column(columnDefinition = "varchar(25) comment '删除时间'")
    private String deleteTime;

    @Column(columnDefinition = "varchar(50) comment '删除批次号'")
    private String deleteBatchNum;

}
