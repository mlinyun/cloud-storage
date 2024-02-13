package com.mlinyun.cloudstorage.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.*;
import lombok.Data;

/**
 * 用户存储信息实体类
 */
@Data
@Table(name = "storage")
@TableName("storage")
@Entity
public class Storage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @TableId(type = IdType.AUTO)
    @Column(columnDefinition = "bigint(20) comment '存储信息ID'")
    private Long storageId;

    @Column(columnDefinition = "bigint(20) comment '用户ID'")
    private Long userId;

    @Column(columnDefinition = "bigint(20) comment '存储信息大小'")
    private Long storageSize;

}
