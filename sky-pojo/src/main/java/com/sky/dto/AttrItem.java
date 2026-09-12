package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author: Joey
 * @Description: 参数详情
 * @date:2024/5/28 18:24
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttrItem {
    private String name;
    private List value;
}
