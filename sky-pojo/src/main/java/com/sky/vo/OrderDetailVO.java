package com.sky.vo;

import com.sky.entity.AddressBook;
import com.sky.entity.Orders;
import lombok.Data;

import java.util.List;

/**
 * @author: Joey
 * @Description:
 * @date:2024/9/17 1:36
 */

@Data
public class OrderDetailVO {
    private Orders orders;
    private AddressBook addressBook;
    private List<OrderDetailGoodsVO> orderDetailGoodsVOList;
}
