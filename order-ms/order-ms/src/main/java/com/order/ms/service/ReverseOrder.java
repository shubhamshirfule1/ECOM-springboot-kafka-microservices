package com.order.ms.service;

import java.util.Optional;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.order.ms.Enum.OrderEnum;
import com.order.ms.dto.OrderEntity;
import com.order.ms.dto.OrderEvent;
import com.order.ms.repo.OrderRepo;

import tools.jackson.databind.ObjectMapper;

@Component
public class ReverseOrder {


	private final  OrderRepo orderRepo;

	public ReverseOrder(OrderRepo orderRepo) {
		super();
		this.orderRepo = orderRepo;
	}

	@KafkaListener(topics="reverse_order",groupId = "orders-group")
	public void reverseOrderConsumer( String event) {
		
		try {
			OrderEvent e = new ObjectMapper().readValue(event, OrderEvent.class);
			
			Optional<OrderEntity> OrderEntity = orderRepo.findById(e.getCustomerOrder().getOrderId());
			OrderEntity.ifPresent(a->{a.setStatus(OrderEnum.Failed);
			orderRepo.save(a);});
		
		
		}catch(Exception e) {
			System.out.println("Exception Occured while reverting order details");
		}
	}
}
