package com.sky.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author: Joey
 * @Description: 商品列表
 * @date:2024/5/22 8:53
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Goods implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long typeId;
    private String name;
    private BigDecimal price;
    private String image;
    private Integer num;//库存
    private String description;
    private String details;
    private Integer status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}
