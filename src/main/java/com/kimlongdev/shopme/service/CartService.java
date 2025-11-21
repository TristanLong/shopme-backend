package com.kimlongdev.shopme.service;

import com.kimlongdev.shopme.exception.ProductException;
import com.kimlongdev.shopme.modal.Cart;
import com.kimlongdev.shopme.modal.CartItem;
import com.kimlongdev.shopme.modal.Product;
import com.kimlongdev.shopme.modal.User;

public interface CartService {

    public CartItem addCartItem(User user,
                                Product product,
                                String size,
                                int quantity) throws ProductException;

    public Cart findUserCart(User user);

}
