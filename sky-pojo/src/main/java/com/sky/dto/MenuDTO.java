package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author: Joey
 * @Description:
 * @date:2024/5/21 17:23
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuDTO {
    private Integer id;
    private String title;
    private String index;
    private String icon;
    private List<MenuItem> menuItems;
}
