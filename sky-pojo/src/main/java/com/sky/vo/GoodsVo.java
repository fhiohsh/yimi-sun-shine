package com.sky.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sky.entity.GoodsAttributes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Joey
 * @Description: 返回Goods列表视图层
 * @date:2024/5/28 21:54
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsVo {
    private Long id;
    private Long categoryDetailId;
    private Long categoryId;
    private String categoryName;
//    private Long typeName;//类型名
    //款式id
    private String name;
    private String image;
    private BigDecimal price;
    private Integer num;//库存
    private Integer status;
    private String shortDesc;//简介
    //富文本内容
    private String description;
    //规格参数
    private List<GoodsAttributes> attrList = new ArrayList<>();;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
