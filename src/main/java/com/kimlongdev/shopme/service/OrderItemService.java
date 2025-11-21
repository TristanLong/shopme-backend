package com.kimlongdev.shopme.service;

import com.kimlongdev.shopme.modal.OrderItem;

public interface OrderItemService {
    OrderItem getOrderItemById(Long id) throws Exception;

}
