package com.kimlongdev.shopme.repository;

import com.kimlongdev.shopme.modal.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

}
