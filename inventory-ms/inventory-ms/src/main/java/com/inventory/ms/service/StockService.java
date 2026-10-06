package com.inventory.ms.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.inventory.ms.Exception.StockNotAvailableException;
import com.inventory.ms.dto.CustomerOrder;
import com.inventory.ms.dto.DeliveryEvent;
import com.inventory.ms.dto.PaymentEvent;
import com.inventory.ms.dto.WareHouse;
import com.inventory.ms.repo.StockRepo;

import tools.jackson.databind.ObjectMapper;

@Service
public class StockService {

	private KafkaTemplate<String, DeliveryEvent>kafkaTemplate;
	private KafkaTemplate<String, PaymentEvent>kafkaTemplatee;
	private StockRepo repo;
	


	public StockService(KafkaTemplate<String, DeliveryEvent> kafkaTemplate,
			KafkaTemplate<String, PaymentEvent> kafkaTemplatee, StockRepo repo) {
		super();
		this.kafkaTemplate = kafkaTemplate;
		this.kafkaTemplatee = kafkaTemplatee;
		this.repo = repo;
	}


	@KafkaListener(topics = "new-payments",groupId="payments-group")
	public void updateStock(String paymentEvent) {
		System.out.println("inside update inventory for Orders"+paymentEvent);
		
		DeliveryEvent deliveryEvent = new DeliveryEvent();
		
		PaymentEvent value = new ObjectMapper().readValue(paymentEvent, PaymentEvent.class);
		CustomerOrder customerOrder = value.getCustomerOrder();
		
		Iterable<WareHouse> inventories = repo.findByItem(customerOrder.getItem());
		boolean hasNext = inventories.iterator().hasNext();
		
		if(!hasNext) {
			System.out.println("Stock is not available");
			throw new StockNotAvailableException("Stock not availbe");
		}
		
		
		try {
			inventories.forEach(s->{s.setQuantity(s.getQuantity()-customerOrder.getQuantity());
			repo.save(s);
			
				});
			deliveryEvent.setOrder(customerOrder);
			deliveryEvent.setType("STOCK_UPDATED");
			kafkaTemplate.send("NEW-STOCK",deliveryEvent);
			
			
		}catch(Exception exception) {
			PaymentEvent event = new PaymentEvent();
			event.setCustomerOrder(customerOrder);
			event.setType("PAYMENT_REVERSED");
			kafkaTemplate.send("Reverse-payment",deliveryEvent);
		}
		
	}
	

	@KafkaListener(topics = "reversed-stock", groupId = "stock-group")
	public void reverseStock(String event) {
		System.out.println("Inside reverse stock for order "+event);
		
		try {
			DeliveryEvent deliveryEvent = new ObjectMapper().readValue(event, DeliveryEvent.class);

			Iterable<WareHouse> inv = this.repo.findByItem(deliveryEvent.getOrder().getItem());

			inv.forEach(i -> {
				i.setQuantity(i.getQuantity() + deliveryEvent.getOrder().getQuantity());
				repo.save(i);
			});

			PaymentEvent paymentEvent = new PaymentEvent();
			paymentEvent.setCustomerOrder(deliveryEvent.getOrder());
			paymentEvent.setType("PAYMENT_REVERSED");
			kafkaTemplatee.send("reversed-payments", paymentEvent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
}
