package com.kimlongdev.shopme.service.impl;


import com.kimlongdev.shopme.exception.ProductException;
import com.kimlongdev.shopme.modal.Cart;
import com.kimlongdev.shopme.modal.CartItem;
import com.kimlongdev.shopme.modal.Product;
import com.kimlongdev.shopme.modal.User;
import com.kimlongdev.shopme.repository.CartItemRepository;
import com.kimlongdev.shopme.repository.CartRepository;
import com.kimlongdev.shopme.service.CartService;
import com.kimlongdev.shopme.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductService productService;


    public Cart findUserCart(User user) {
        Cart cart =	cartRepository.findByUserId(user.getId());

        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart.setCartItems(new HashSet<>());
            cart = cartRepository.save(cart);
        }

        int totalPrice=0;
        int totalDiscountedPrice=0;
        int totalItem=0;
        for(CartItem cartsItem : cart.getCartItems()) {
            totalPrice+=cartsItem.getMrpPrice();
            totalDiscountedPrice+=cartsItem.getSellingPrice();
            totalItem+=cartsItem.getQuantity();
        }

        double finalPrice=totalDiscountedPrice-cart.getCouponPrice();

        cart.setTotalMrpPrice(totalPrice);
        cart.setTotalItem(cart.getCartItems().size());
        cart.setTotalSellingPrice(finalPrice);
        cart.setDiscount(calculateDiscountPercentage(totalPrice,totalDiscountedPrice));
        cart.setTotalItem(totalItem);

        return cartRepository.save(cart);

    }

    public static int calculateDiscountPercentage(double mrpPrice, double sellingPrice) {
        if (mrpPrice <= 0) {
            return 0;
        }
        double discount = mrpPrice - sellingPrice;
        double discountPercentage = (discount / mrpPrice) * 100;
        return (int) discountPercentage;
    }

    @Override
    public CartItem addCartItem(User user,
                                Product product,
                                String size,
                                int quantity
    ) throws ProductException {
        Cart cart=findUserCart(user);

        CartItem isPresent=cartItemRepository.findByCartAndProductAndSize(
                cart, product, size);

        if(isPresent == null) {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);

            cartItem.setQuantity(quantity);
            cartItem.setUserId(user.getId());

            int totalPrice=quantity*product.getSellingPrice();
            cartItem.setSellingPrice(totalPrice);
            cartItem.setMrpPrice(quantity*product.getMrpPrice());
            cartItem.setSize(size);

            cart.getCartItems().add(cartItem);
            cartItem.setCart(cart);

            return cartItemRepository.save(cartItem);
        }

        return isPresent;
    }

}
