package com.inventory.ms.Exception;

public class StockNotAvailableException extends RuntimeException{

	
	public StockNotAvailableException(String message) {
		super(message);
	}
}
