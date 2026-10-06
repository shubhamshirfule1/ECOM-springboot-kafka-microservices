package com.inventory.ms.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventory.ms.dto.WareHouse;

public interface StockRepo extends JpaRepository<WareHouse,Long>{

	public Iterable<WareHouse> findByItem(String item);
}
