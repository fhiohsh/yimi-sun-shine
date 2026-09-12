package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author: Joey
 * @Description: 类型详情关联实体
 * @date:2024/5/22 19:23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoodsCategoryStyle implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long typeId;
    private String styleName;
    private Integer status;
    private String image;
}
