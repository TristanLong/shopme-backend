package com.kimlongdev.shopme.repository;

import com.kimlongdev.shopme.modal.Cart;
import com.kimlongdev.shopme.modal.CartItem;
import com.kimlongdev.shopme.modal.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartAndProductAndSize(Cart cart, Product product, String size);
}
