package com.order.ms.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.order.ms.dto.OrderEvent;

@Service
public class OrderProducer {
	
	private KafkaTemplate<String , OrderEvent > orderKafkaTemplate;

	public OrderProducer(KafkaTemplate<String, OrderEvent> orderKafkaTemplate) {
		super();
		this.orderKafkaTemplate = orderKafkaTemplate;
	}
	
	public void sendMessageOrder(String message,OrderEvent event) {
		orderKafkaTemplate.send(message,event);
	}

}
