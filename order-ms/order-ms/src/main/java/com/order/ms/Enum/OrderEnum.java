package com.order.ms.Enum;

public enum OrderEnum {

	
	CREATED("Order Created"),
	COMPLETED("Order Completed"), 
	Failed("Order Cancelled");
	
	private final String descirption;

	private OrderEnum(String descirption) {
		this.descirption = descirption;
	}
	
	
	
}
