package com.order.ms.service;

import org.springframework.stereotype.Service;

import com.order.ms.Enum.OrderEnum;
import com.order.ms.dto.CustomerOrder;
import com.order.ms.dto.OrderEntity;
import com.order.ms.dto.OrderEvent;
import com.order.ms.repo.OrderRepo;

@Service
public class OrderService {

	
	private final OrderRepo orderRepo;
	
	private final OrderProducer orderProducer;
	
	
	
	public OrderService(OrderRepo orderRepo, OrderProducer orderProducer) {
		super();
		this.orderRepo = orderRepo;
		this.orderProducer = orderProducer;
	}



	public void CreateOrder(CustomerOrder order) {
		
		OrderEntity entity = new OrderEntity();
		entity.setAmount(order.getAmount());
		entity.setItem(order.getItem());
		entity.setQuantity(order.getQuantity());
		entity.setStatus(OrderEnum.CREATED);
		entity.setAddress(order.getAddress());
		entity.setPaymentMethod(order.getPaymentMethod());
		try {
		OrderEntity save = orderRepo.save(entity);
		
		order.setOrderId(save.getId());
		
		OrderEvent event = new OrderEvent();
		event.setCustomerOrder(order);
		event.setType("ORDER_CREATED");
		
		orderProducer.sendMessageOrder("new-orders",event);
		}catch(Exception e) {
			System.out.println("=========== ORDER ERROR ===========");
			e.printStackTrace();
			System.out.println("===================================");
			entity.setStatus(OrderEnum.Failed);
			orderRepo.save(entity);
		}
		
		
	}
}
