package com.jt.order.mapper;

import java.util.List;

import com.jt.common.mapper.SysMapper;
import com.jt.order.pojo.OrderItem;

public interface OrderItemMapper extends SysMapper<OrderItem>{

	List<OrderItem> findOrderItemByOrderId(String orderId);
	
}