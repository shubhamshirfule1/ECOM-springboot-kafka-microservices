package com.order.ms.controller;

import com.order.ms.Enum.OrderEnum;
import com.order.ms.dto.CustomerOrder;
import com.order.ms.dto.OrderEntity;
import com.order.ms.dto.OrderEvent;
import com.order.ms.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class OrderController {

	private OrderService orderService;
	
	public OrderController(OrderService orderService) {
		super();
		this.orderService = orderService;
	}



	@PostMapping("/orders")
	public void createOrder(@RequestBody CustomerOrder order) {
		
		 orderService.CreateOrder(order);
		
		
	}
}
