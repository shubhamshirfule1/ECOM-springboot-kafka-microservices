package com.inventory.ms.dto;

public class DeliveryEvent {

	private CustomerOrder order;
	
	private String type;

	
	public CustomerOrder getOrder() {
		return order;
	}

	public void setOrder(CustomerOrder order) {
		this.order = order;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
	
}
