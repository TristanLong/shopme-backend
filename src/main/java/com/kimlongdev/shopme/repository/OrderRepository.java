package com.kimlongdev.shopme.repository;

import com.kimlongdev.shopme.modal.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {

    List<Order> findByUserId(Long userId);
    List<Order> findBySellerIdOrderByOrderedDateDesc(Long sellerId);
    List<Order> findBySellerIdAndOrderedDateBetween(Long sellerId, LocalDateTime startDate, LocalDateTime endDate);

}
