package com.order.ms.dto;

import com.order.ms.Enum.OrderEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table
public class OrderEntity {

	@Id
	@GeneratedValue
	private long id;

	@Column
	private String item;
	
	@Column
	private int quantity;
	@Column
	private double amount;
	
	@Column(name = "status")
	@Enumerated(EnumType.STRING)
	private OrderEnum status;
	
	@Column
	private String address;
	
	@Column
	private String paymentMethod;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public OrderEnum getStatus() {
		return status;
	}

	public void setStatus(OrderEnum status) {
		this.status = status;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

}
