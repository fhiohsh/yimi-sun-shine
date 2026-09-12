package com.sky.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.controller.response.OrderResponse;
import com.sky.dto.OrderAddressDTO;
import com.sky.dto.OrderDTO;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.utils.RedisUtil;
import com.sky.vo.GoodsVo;
import com.sky.vo.OrderDetailGoodsVO;
import com.sky.vo.OrderDetailVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: Joey
 * @Description:
 * @date:2024/9/17 0:14
 */
@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private UserCouponMapper userCouponMapper;

    @Autowired
    private RedisUtil redisUtil;

    public PageResult getAllOrder(Integer orderStatus, Integer current, Integer size){
        if (current == null) {
            throw new OrderBusinessException(MessageConstant.UNKNOWN_ERROR);
        }
        PageHelper.startPage(current, size);
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        if (orderStatus != null) {
            queryWrapper.eq("order_status", orderStatus);
        }
        queryWrapper.orderByDesc("update_time");
        List<Orders> orders = orderMapper.selectList(queryWrapper);
        // 获取分页信息
        PageInfo<Orders> pageInfo = new PageInfo<>(orders);
        return new PageResult(pageInfo.getTotal(), pageInfo.getList());
    }

    @Transactional
    public List<OrderResponse> getOrder(Integer orderStatus){
        QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
        Integer userId = BaseContext.getCurrentId().intValue();
        if (userId == null) {
           throw new OrderBusinessException(MessageConstant.USER_ID_ORDER_FAILED);
        }
        queryWrapper.eq("user_id", userId);
        if (orderStatus != null) {
            queryWrapper.eq("order_status", orderStatus);
        }
        List<Orders> orders = orderMapper.selectList(queryWrapper);
        Date now = new Date();
        for (Orders order : orders) {
            if (order.getOrderStatus() == Orders.UNPAID
                    && order.getOrderExpireTime()!= null
                    && order.getOrderExpireTime().before(now)) {
                order.setOrderStatus(Orders.CANCELED);
                order.setCancelReason("超时未支付");
                orderMapper.updateById(order);
            }
        }
        queryWrapper.orderByDesc("create_time");
        List<Orders> ordersList = orderMapper.selectList(queryWrapper);

        List<OrderResponse> responseList = new ArrayList<>();
        ordersList.forEach(order -> {
            OrderResponse response = new OrderResponse();
            BeanUtils.copyProperties(order, response);
            OrderDetails details = orderDetailMapper.selectList(new LambdaQueryWrapper<OrderDetails>()
                    .eq(OrderDetails::getOrderId, order.getId())).get(0);
            Goods goods = goodsMapper.selectById(details.getProductId());
            response.setImageOne(goods.getImage());
            responseList.add(response);
        });
        return responseList;
    }



    public OrderDetailVO getOrderDetail(Integer orderId){
        QueryWrapper<Orders> orderQueryWrapper = new QueryWrapper<>();
        orderQueryWrapper.eq("id", orderId);
//        orderQueryWrapper.eq("user_id", BaseContext.getCurrentId());
        Orders orders = orderMapper.selectOne(orderQueryWrapper);
        if (orders == null) {
            throw new OrderBusinessException(MessageConstant.USER_ID_ORDER_FAILED);
        }
        OrderDetailVO detailVO = new OrderDetailVO();
        AddressBook addressBook = addressBookMapper.selectById(orders.getAddressId());
        if (addressBook == null) {
            throw new OrderBusinessException(MessageConstant.USER_ID_ADDRESS_FAILED);
        }
        detailVO.setAddressBook(addressBook);
        detailVO.setOrders(orders);
        List<OrderDetailGoodsVO> goodsVOS = new ArrayList<>();
        QueryWrapper<OrderDetails> orderDetailsQueryWrapper = new QueryWrapper<>();
        orderDetailsQueryWrapper.eq("order_id", orderId);
        List<OrderDetails> details = orderDetailMapper.selectList(orderDetailsQueryWrapper);
        if (details.size() > 0) {
            for (OrderDetails detail : details) {
                Goods goods = goodsMapper.queryGoods(detail.getProductId());
                OrderDetailGoodsVO goodsVO = new OrderDetailGoodsVO();
                goodsVO.setProductId(detail.getProductId().intValue());
                goodsVO.setProductName(detail.getProductName());
                goodsVO.setAttrInfo(detail.getAttrInfo());
                goodsVO.setNum(detail.getQuantity());
                goodsVO.setPrice(detail.getTotalPrice());
                goodsVO.setImage(goods.getImage());
                goodsVOS.add(goodsVO);
            }
        }
        detailVO.setOrderDetailGoodsVOList(goodsVOS);
        return detailVO;
    }


    @Transactional
    public Orders addOrder(OrderDTO orderDTO){
        AddressBook addressBook = addressBookMapper.selectById(orderDTO.getAddressId());
        if(addressBook == null){
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        Long currentId = BaseContext.getCurrentId();
        Integer userId = Math.toIntExact(currentId);
        Orders order = new Orders();
        BeanUtils.copyProperties(orderDTO, order);//origin  target
        order.setAddressInfo(orderDTO.getAddressInfo());
        order.setUserPhone(orderDTO.getUserPhone());
        order.setUserId(userId);
        order.setOrderNumber(generateOrderNumber());
        order.setOrderStatus(Orders.UNPAID);
        order.setDeliveryStatus(Orders.DELIVERY_NOT_SHIPPED);
        setOrderExpireTimeWithFiveMinutesAdded(order);
        int insert = orderMapper.insert(order);
        if (insert < 0) {
            throw new OrderBusinessException(MessageConstant.ADD_ORDER_FAILED);
        }
        // 将订单id存入Redis，key 为固定的订单哈希标识，field 为订单 ID
//        redisUtil.hset("order_cache", order.getId().toString(), order.getId());
        List<OrderDetails> orderDetails = new ArrayList<>();
        orderDTO.getGoodsList().forEach(item -> {
            OrderDetails details = new OrderDetails();
            details.setOrderId(order.getId());
            details.setProductId(item.getId());
            details.setProductName(item.getName());
            details.setQuantity(item.getNum());
            details.setUnitPrice(item.getPrice());//单价
            details.setTotalPrice(item.getTotalPrice());
            details.setAttrInfo(item.getAttrInfo());
            orderDetails.add(details);
        });
        if(!orderDetails.isEmpty()){
            orderDetails.forEach(od->{
                orderDetailMapper.insert(od);
            });
        }
        return order;
    }


    @Transactional
    public void updateOrderStatus(OrderAddressDTO orderAddressDTO){
        Orders order = orderMapper.selectById(orderAddressDTO.getOrderId());
        if (order!= null) {
            // 检查传入的订单状态是否合法
            boolean isValidStatus = false;
            for (int statusValue : new int[]{
                    Orders.UNPAID,
                    Orders.PAID,
                    Orders.TO_BE_SHIPPED,
                    Orders.SHIPPED,
                    Orders.TO_BE_EVALUATED,
                    Orders.REFUND_IN_PROGRESS,
                    Orders.REFUNDED,
                    Orders.COMPLETED,
                    Orders.CANCELED}) {
                if (statusValue == orderAddressDTO.getOrderStatus()) {
                    isValidStatus = true;
                    break;
                }
            }
            if (isValidStatus) {
                //取消订单  cancelReason
                if(orderAddressDTO.getOrderStatus() == Orders.CANCELED){
                    order.setCancelReason(orderAddressDTO.getCancelReason()==null
                            ? "系统取消" : orderAddressDTO.getCancelReason());
                    sendMsg(orderAddressDTO, "订单取消");
                }

                //订单发货
                if(orderAddressDTO.getOrderStatus() == Orders.SHIPPED){
                    order.setDeliveryStatus(Orders.DELIVERY_SHIPPED);
                    order.setDeliveryAddress(Orders.DELIVERY_ADDRESS);
                    //发送通知
                    sendMsg(orderAddressDTO, "已发货");
                }
                //收货
                if(orderAddressDTO.getOrderStatus() == Orders.TO_BE_EVALUATED){
                    order.setReceiveDeliveryTime(new Date());
                    order.setDeliveryStatus(Orders.DELIVERY_RECEIVED);
                    sendMsg(orderAddressDTO, "订单收货");
                }

                //退款退货状态
                if(orderAddressDTO.getOrderStatus() == Orders.REFUND_IN_PROGRESS){
                    sendMsg(orderAddressDTO, "订单退货中");
                }

                //确认退款退货
                if(orderAddressDTO.getOrderStatus() == Orders.REFUNDED){
                    sendMsg(orderAddressDTO, "订单已退货");
                }

                //已完成 评价后为完成
                if(orderAddressDTO.getOrderStatus() == Orders.COMPLETED){

                    //todo 评价表更新 comment_type -> 1
                    sendMsg(orderAddressDTO, "订单评价完成");
                }

                order.setOrderStatus(orderAddressDTO.getOrderStatus());
                orderMapper.updateById(order);
            } else {
                throw new OrderBusinessException("非法的订单状态值");
            }
        } else {
            throw new OrderBusinessException("找不到对应的订单");
        }
    }

    private void sendMsg(OrderAddressDTO orderAddressDTO, String msg){
        Notification notification = new Notification();
        notification.setOrderId(orderAddressDTO.getOrderId());
        notification.setUserId(orderAddressDTO.getUserId());
        notification.setTitle("订单通知");
        notification.setMessageContent("您购买的订单"+orderAddressDTO.getOrderNumber() + msg);
        notification.setRead(false);
        notification.setType("订单通知");
        notificationMapper.insert(notification);
    }

    @Transactional
    public void updateOrderAddress(OrderAddressDTO orderAddressDTO){
        Orders orders = orderMapper.selectById(orderAddressDTO.getOrderId());
        if (orders == null || orderAddressDTO.getAddressInfo() == null) {
            throw new OrderBusinessException("找不到对应的订单");
        }
        orders.setAddressInfo(orderAddressDTO.getAddressInfo());
        orderMapper.updateById(orders);
    }
//    public void checkOrderStatus() {
//        // 获取所有存储在 Redis 中的订单 id
//        Set<String> keys = (Set<String>) redisUtil.get("*");
//        for (String key : keys) {
//            Long orderId = (Long) redisUtil.get(key);
//            Orders order = orderMapper.selectById(orderId);
//            if (order != null) {
//                if (Orders.PAID.equals(order.getOrderStatus())) {
//                    // 已支付，删除缓存
//                    redisUtil.del(key);
//                } else if (Orders.UNPAID.equals(order.getOrderStatus())) {
//                    // 未支付，修改订单状态
//                    order.setOrderStatus(Orders.CANCELED);
//                    orderMapper.updateById(order);
//                }
//            }
//        }
//    }

    @Transactional
    public Orders payOrder(OrderDTO orderDTO){
//        setShipTime(order);
        Orders orders = orderMapper.selectById(orderDTO.getOrderId());
        setShipTime(orders);
        orders.setOrderStatus(Orders.TO_BE_SHIPPED);
        orders.setDeliveryStatus(Orders.DELIVERY_NOT_SHIPPED);
        if(orderDTO.getPaymentMethod() == 1){
            orders.setPaymentMethod("微信支付");
        }else{
            orders.setPaymentMethod("其他支付");
        }
        //添加优惠券核销
        if(orderDTO.getCouponId() != null && orderDTO.getCouponId() != 0){
            redeemCoupon(orderDTO.getCouponId(), orders.getId());
        }
        orderMapper.updateById(orders);
        //商品数量-1
        QueryWrapper<OrderDetails> orderDetailQueryWrapper = new QueryWrapper<>();
        orderDetailQueryWrapper.eq("order_id", orderDTO.getOrderId());
        List<OrderDetails> detailsList = orderDetailMapper.selectList(orderDetailQueryWrapper);
        List<Integer> ids = new ArrayList<>();
        for (OrderDetails od : detailsList) {
            ids.add(od.getProductId().intValue());
        }
        if(ids!=null){
            List<Goods> goods = goodsMapper.selectBatchIds(ids);
            goods.forEach(good ->{
                Integer num = good.getNum() - 1;
                good.setNum(num);
                goodsMapper.updateById(good);
            });
        }
        return orderMapper.selectById(orderDTO.getOrderId());
    }

    private void redeemCoupon(Integer couponId, Integer orderId) {
        // 更新优惠券状态为已使用等逻辑
        CouponUserReal real = userCouponMapper.selectById(couponId);
        if (real != null && real.getStatus() == 0) {
            real.setStatus(CouponUserReal.VERIFY_USED); // 假设 1 代表已使用状态
            real.setUseTime(new Date());
            real.setOrderId(orderId);
            userCouponMapper.updateById(real);
        }else{
            throw new OrderBusinessException(MessageConstant.COUPON_STS_FAILED);
        }
    }


    /**
     * 设置发货时间 now + 1天
     * @param order
     */
    private void setShipTime(Orders order) {
        Date currentTime = new Date();
        // 将当前时间加上一天（以毫秒为单位）
        long oneDayInMillis = 24 * 60 * 60 * 1000;
        Date shipTime = new Date(currentTime.getTime() + oneDayInMillis);
        order.setDeliveryTime(shipTime);
    }

    public void setOrderExpireTimeWithFiveMinutesAdded(Orders order) {
        Date now = new Date();
        long fiveMinutesInMillis = 15 * 60 * 1000;
        order.setOrderExpireTime(new Date(now.getTime() + fiveMinutesInMillis));
    }

    private String generateOrderNumber(){
        UUID uuid = UUID.randomUUID();
        String uuidStr = uuid.toString().replaceAll("-", "").substring(0, 8);
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        String dateStr = sdf.format(new java.util.Date());
        return dateStr + uuidStr;
    }
}
