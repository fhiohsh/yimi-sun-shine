package com.sky.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sky.context.BaseContext;
import com.sky.entity.Goods;
import com.sky.entity.ShopCart;
import com.sky.exception.ShoppingCartBusinessException;
import com.sky.mapper.GoodsMapper;
import com.sky.mapper.ShopCartMapper;
import com.sky.vo.ShopCartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Joey
 * @Description:
 * @date:2024/9/21 16:37
 */

@Service
public class ShopCartService extends ServiceImpl<ShopCartMapper, ShopCart> {

    @Autowired
    private GoodsMapper goodsMapper;

    public List<ShopCartVO> getCartListWithProductInfo() {
        List<ShopCart> cartList = getCartList();
        List<ShopCartVO> resultList = new ArrayList<>();
        for (ShopCart cart : cartList) {
            Integer productId = cart.getProductId();
            Goods goods = goodsMapper.queryGoods(productId.longValue());
            if (goods!= null) {
                ShopCartVO vo = new ShopCartVO();
                vo.setId(cart.getId());
                vo.setQuantity(cart.getQuantity());
                vo.setProductId(cart.getProductId());
                vo.setProductName(goods.getName());
                vo.setAttrInfo(cart.getAttrInfo());
                vo.setProductImage(goods.getImage());
                vo.setProductPrice(goods.getPrice());
                resultList.add(vo);
            }
        }
        return resultList;
    }

    public List<ShopCart> getCartList() {
        return list(new QueryWrapper<ShopCart>().eq("user_id", BaseContext.getCurrentId()));
    }

    @Transactional
    public boolean addToCart(ShopCart shopCart) {
        Integer userId = BaseContext.getCurrentId().intValue();
        shopCart.setUserId(userId);
        QueryWrapper<ShopCart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .eq("product_id", shopCart.getProductId())
                .eq("attr_info", shopCart.getAttrInfo());
        ShopCart existingCartItem = getOne(queryWrapper);
        if (existingCartItem != null) {

            int currentQuantity = existingCartItem.getQuantity();
            int newQuantity = currentQuantity + shopCart.getQuantity();
            if (newQuantity <= 10) {
                existingCartItem.setQuantity(newQuantity);
                return updateById(existingCartItem);
            } else {
                // 数量超过 10，返回异常或进行其他处理
                throw new ShoppingCartBusinessException("当前商品选择添加数量达到上限");
            }
        } else {
            return save(shopCart);
        }
    }

    @Transactional
    public boolean deleteFromCart(Integer id) {
        return removeById(id);
    }

    @Transactional
    public boolean updateQuantity(Integer id, Integer quantity) {
        ShopCart shopCart = new ShopCart();
        shopCart.setQuantity(quantity);
        return update(shopCart, new QueryWrapper<ShopCart>().eq("id", id));
    }
}
