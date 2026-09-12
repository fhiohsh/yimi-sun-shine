package com.sky.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: Joey
 * @Description: 后台菜单
 * @date:2024/5/21 17:21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Menu implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "动态权限")
    private Integer id;

    private String name;

    @ApiModelProperty(value = "主菜单子菜单")
    private Integer pid;

    @ApiModelProperty(value = "路由")
    private String path;

    @ApiModelProperty(value = "组件vue")
    private String component;

    @ApiModelProperty(value = "图标")
    private String icon;

    private Integer createId;

    private Date createTime;

    private Integer updateId;

    private Date updateTime;
}
