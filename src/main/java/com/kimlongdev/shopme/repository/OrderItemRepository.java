package com.kimlongdev.shopme.repository;

import com.kimlongdev.shopme.modal.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
