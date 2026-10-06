package com.order.ms.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.order.ms.dto.OrderEntity;

@Repository
public interface OrderRepo extends JpaRepository<OrderEntity, Long>{

}
