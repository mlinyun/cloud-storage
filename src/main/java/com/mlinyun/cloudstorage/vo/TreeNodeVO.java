package com.mlinyun.cloudstorage.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用来展示目录结构
 */
@Data
@Schema(name = "树节点VO", requiredMode = Schema.RequiredMode.REQUIRED)
public class TreeNodeVO {

    /**
     * 节点ID
     */
    @Schema(description = "节点ID")
    private Long id;

    /**
     * 节点名
     */
    @Schema(description = "节点名")
    private String label;

    /**
     * 深度
     */
    @Schema(description = "深度")
    private Long depth;

    /**
     * 是否被关闭
     */
    @Schema(description = "是否被关闭")
    private String state = "closed";

    /**
     * 属性集合
     */
    @Schema(description = "属性集合")
    private Map<String, String> attributes = new HashMap<>();

    /**
     * 子节点列表
     */
    @Schema(description = "子节点列表")
    private List<TreeNodeVO> children = new ArrayList<>();

}
