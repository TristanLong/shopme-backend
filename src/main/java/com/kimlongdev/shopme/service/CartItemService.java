package com.kimlongdev.shopme.service;

import com.kimlongdev.shopme.exception.CartItemException;
import com.kimlongdev.shopme.exception.UserException;
import com.kimlongdev.shopme.modal.CartItem;

public interface CartItemService {

    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException, UserException;

    public void removeCartItem(Long userId,Long cartItemId) throws CartItemException, UserException;

    public CartItem findCartItemById(Long cartItemId) throws CartItemException;

}
