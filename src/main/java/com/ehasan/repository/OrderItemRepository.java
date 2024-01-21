package com.ehasan.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ehasan.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
