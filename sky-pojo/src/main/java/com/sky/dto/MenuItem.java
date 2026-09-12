package com.sky.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Joey
 * @Description:
 * @date:2024/5/21 17:23
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuItem {
    private Integer itemId;
    private String index;//路径
    private String title;//标题
}
