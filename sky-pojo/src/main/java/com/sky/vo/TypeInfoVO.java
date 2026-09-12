package com.sky.vo;

import com.sky.dto.TypeInfoDTO;
import lombok.Data;

import java.util.List;

/**
 * @author: Joey
 * @Description: 分类VO
 * @date:2024/5/29 20:52
 */
@Data
public class TypeInfoVO {
    private String label;
    private Long value;
    List<TypeInfoDTO> children;
    private boolean hasChildren;
}
