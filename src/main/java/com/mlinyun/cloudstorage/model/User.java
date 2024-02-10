package com.mlinyun.cloudstorage.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.persistence.*;
import lombok.Data;

/**
 * 用户实体类
 */
@Data //  lombok 提供的注解，会自动为该类添加 getter/setter 方法
@TableName("user")  // MyaBatis Plus 提供的注解，实体类添加，如果不添加，会按照默认规则进行表明的映射，比如 UserTable->user_table
@Table(name = "user") // 自定义表名
@Entity // 表明该类是一个实体类，添加了该注解后，才能被 jpa 扫描到
public class User {

    @Id // 表名该字段为主键字段，当声明了 @Entity 注解， @Id 就必须也得声明
    @TableId(type = IdType.AUTO)    // MyaBatis Plus 提供的注解，用来标注实体类主键
    // 设置主键生成方式，主要有四种类型，这里我们将 strategy 属性设置为 GenerationType.IDENTITY，表明主键由数据库生成，为自动增长型
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 自定义列名或者定义其他的数据类型
    @Column(columnDefinition = "bigint(20) comment '用户ID'")
    private Long userId;

    @Column(columnDefinition = "varchar(30) comment '用户名'")
    private String username;

    @Column(columnDefinition = "varchar(35) comment '密码'")
    private String password;

    @Column(columnDefinition = "varchar(15) comment '手机号码'")
    private String telephone;

    @Column(columnDefinition = "varchar(20) comment '盐值'")
    private String salt;

    @Column(columnDefinition = "varchar(30) comment '注册时间'")
    private String registerTime;

}
