package com.inventory.ms.dto;

import jakarta.persistence.Column;

public class Stock {

	private int Quantity;
	
	private String item;

	public int getQuantity() {
		return Quantity;
	}

	public void setQuantity(int quantity) {
		Quantity = quantity;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	
	
	
}
