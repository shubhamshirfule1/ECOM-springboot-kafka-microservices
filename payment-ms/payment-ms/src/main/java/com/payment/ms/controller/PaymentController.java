package com.payment.ms.controller;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RestController;

import com.payment.ms.dto.CustomerOrder;
import com.payment.ms.dto.OrderEvent;
import com.payment.ms.dto.PaymentEvent;
import com.payment.ms.entity.PaymentEntity;
import com.payment.ms.repo.PaymentRepo;

import tools.jackson.databind.ObjectMapper;

@RestController
public class PaymentController {
	
	private PaymentRepo paymentRepo;
	
	
	KafkaTemplate<String, PaymentEvent> kafkaTemplate;
	KafkaTemplate<String, OrderEvent> kafkaTemplatefororder;


	public PaymentController(PaymentRepo paymentRepo, KafkaTemplate<String, PaymentEvent> kafkaTemplate,
			KafkaTemplate<String, OrderEvent> kafkaTemplatefororder) {
		super();
		this.paymentRepo = paymentRepo;
		this.kafkaTemplate = kafkaTemplate;
		this.kafkaTemplatefororder = kafkaTemplatefororder;
	}

	@KafkaListener(topics = "new-orders",groupId = "orders-group")
	public void processPayment(String event) {
		System.out.println("process payment event"+event);
		
		OrderEvent value = new ObjectMapper().readValue(event, OrderEvent.class);
		CustomerOrder customerOrder = value.getCustomerOrder();
		
		PaymentEntity entity = new PaymentEntity();
		entity.setAmount(customerOrder.getAmount());
		entity.setPaymentMethod(customerOrder.getPaymentMethod());
		entity.setOrderId(customerOrder.getOrderId());
		entity.setStatus("Success");
		
		try {
			paymentRepo.save(entity);
			PaymentEvent paymentevent1 = new PaymentEvent();
			paymentevent1.setCustomerOrder(customerOrder);
			paymentevent1.setType("PAYMENT_CRETAED");
			
			kafkaTemplate.send("new-payments",paymentevent1);
		}catch(Exception e) {
			System.out.println("=========== Payment ERROR ===========");
			e.printStackTrace();
			System.out.println("===================================");
			System.out.println("Exception Occured while payment process");
			entity.setOrderId(customerOrder.getOrderId());
			entity.setStatus("Failed");
			paymentRepo.save(entity);
			OrderEvent event2 = new OrderEvent();
			event2.setCustomerOrder(customerOrder);
			event2.setType("ORDER_REVERSED");
			kafkaTemplatefororder.send("reversed-orders",event2);
			
		}
	}

}
