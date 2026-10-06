package com.delivery.ms.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.delivery.ms.dto.Delivery;

public interface DeliveryRepository extends JpaRepository<Delivery, Long>{

}
