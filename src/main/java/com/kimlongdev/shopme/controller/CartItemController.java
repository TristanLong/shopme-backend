package com.kimlongdev.shopme.controller;

import com.kimlongdev.shopme.service.CartItemService;
import com.kimlongdev.shopme.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart-items")
public class CartItemController {

    private CartItemService cartItemService;
    private UserService userService;

    public CartItemController(CartItemService cartItemService, UserService userService) {
        this.cartItemService=cartItemService;
        this.userService=userService;
    }

}
