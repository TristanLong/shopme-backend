package com.kimlongdev.shopme.service;

import com.kimlongdev.shopme.domain.OrderStatus;
import com.kimlongdev.shopme.exception.OrderException;
import com.kimlongdev.shopme.modal.Address;
import com.kimlongdev.shopme.modal.Cart;
import com.kimlongdev.shopme.modal.Order;
import com.kimlongdev.shopme.modal.User;

import java.util.List;
import java.util.Set;

public interface OrderService {
    public Set<Order> createOrder(User user, Address shippingAddress, Cart cart);

    public Order findOrderById(Long orderId) throws OrderException;

    public List<Order> usersOrderHistory(Long userId);

    public List<Order>getShopsOrders(Long sellerId);

    public Order updateOrderStatus(Long orderId,
                                   OrderStatus orderStatus)
            throws OrderException;

    public void deleteOrder(Long orderId) throws OrderException;

    Order cancelOrder(Long orderId, User user) throws OrderException;
}
