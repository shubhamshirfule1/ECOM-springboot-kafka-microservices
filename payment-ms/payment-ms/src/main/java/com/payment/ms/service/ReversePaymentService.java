package com.payment.ms.service;

import java.util.Optional;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.payment.ms.dto.CustomerOrder;
import com.payment.ms.dto.OrderEvent;
import com.payment.ms.dto.PaymentEvent;
import com.payment.ms.entity.PaymentEntity;
import com.payment.ms.repo.PaymentRepo;

import tools.jackson.databind.ObjectMapper;

@Component
public class ReversePaymentService {

	private PaymentRepo paymentRepo;
	
	private KafkaTemplate<String, OrderEvent>kafkaTemplate;
	

	@KafkaListener(topics = "reversed-payments",groupId="payments-group")
	public void reversePayment(String event) {
		System.out.println("Inside reverse payment for order"+event);
		
		try {
			PaymentEvent event2 = new ObjectMapper().readValue(event, PaymentEvent.class);
			CustomerOrder customerOrder = event2.getCustomerOrder();
			
			Optional<PaymentEntity> PaymentEntity = this.paymentRepo.findById(customerOrder.getOrderId());
			PaymentEntity.stream().forEach(p->{
				p.setStatus("FAiled");
				paymentRepo.save(p);
			});
			
			OrderEvent event3 = new OrderEvent();
			event3.setCustomerOrder(customerOrder);
			event3.setType("ORDER_REVERSED");
			kafkaTemplate.send("reversed-orders",event3);
			
		}catch(Exception e) {
			System.out.println("Exception Occured while reverseing payment");
			e.printStackTrace();
		}
	}
}
