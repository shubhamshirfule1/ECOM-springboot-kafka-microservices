package com.inventory.ms.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.ms.dto.Stock;
import com.inventory.ms.dto.WareHouse;
import com.inventory.ms.repo.StockRepo;

@RestController
@RequestMapping("/inventory")
public class inventoryController {

	private StockRepo repo;
	
	@PostMapping("/items/addStock")
	public void AddStock(@RequestBody Stock stock) {
		
		Iterable<WareHouse> byItem = repo.findByItem(stock.getItem());
		
		if(byItem.iterator().hasNext()) {
			byItem.forEach(i->{
				i.setQuantity(i.getQuantity()+stock.getQuantity());
				repo.save(i);
			});
		}else {
			WareHouse house = new WareHouse();
			house.setItem(stock.getItem());
			house.setQuantity(stock.getQuantity());
			repo.save(house);
		}
		
	}
}
