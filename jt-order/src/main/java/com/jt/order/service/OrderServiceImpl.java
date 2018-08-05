package com.jt.order.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.order.mapper.OrderItemMapper;
import com.jt.order.mapper.OrderMapper;
import com.jt.order.mapper.OrderShippingMapper;
import com.jt.order.pojo.Order;
import com.jt.order.pojo.OrderItem;
import com.jt.order.pojo.OrderShipping;


@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderShippingMapper orderShippingMapper;
	@Autowired
	private OrderItemMapper orderItemMapper;
	
	//保存订单信息时,需要关联入库 同时操作三张表
	@Override
	public String saveOrder(Order order) {
		
		String orderId = order.getUserId() + "" + System.currentTimeMillis();
		Date date = new Date();
		
		//1.实现订单表入库操作
		order.setOrderId(orderId); //保存主键
		order.setCreated(date);
		order.setUpdated(date);
		order.setStatus(1);	//代表未支付
		orderMapper.insert(order);
		System.out.println("order表入库成功!!!");
		
		//2.实现订单物流信息入库
		OrderShipping shipping = order.getOrderShipping();
		shipping.setOrderId(orderId);
		shipping.setCreated(date);
		shipping.setUpdated(date);
		orderShippingMapper.insert(shipping);
		System.out.println("订单物流信息入库成功!!");
		
		//3.实现订单商品入库
		List<OrderItem> orderItems = order.getOrderItems();
		
		for (OrderItem orderItem : orderItems) {
			orderItem.setOrderId(orderId);
			orderItem.setCreated(date);
			orderItem.setUpdated(date);
			orderItemMapper.insert(orderItem);
		}
		
		System.out.println("恭喜你代码写完了!!!");
		
		return orderId;
	}

	

	@Override
	public Order findOrderById(String orderId) {
		OrderShipping orderShipping=orderShippingMapper.findOrderShippingByOrderId(orderId);
		List<OrderItem> orderItems=orderItemMapper.findOrderItemByOrderId(orderId);
		Order order=orderMapper.selectByPrimaryKey(orderId);
		order.setOrderItems(orderItems);
		order.setOrderShipping(orderShipping);
		return order;
		//return orderMapper.findOrderById(orderId);
	}
}
