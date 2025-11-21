package com.kimlongdev.shopme.controller;


import com.kimlongdev.shopme.exception.UserException;
import com.kimlongdev.shopme.modal.Cart;
import com.kimlongdev.shopme.modal.CartItem;
import com.kimlongdev.shopme.modal.Product;
import com.kimlongdev.shopme.modal.User;
import com.kimlongdev.shopme.request.AddItemRequest;
import com.kimlongdev.shopme.response.ApiResponse;
import com.kimlongdev.shopme.service.CartItemService;
import com.kimlongdev.shopme.service.CartService;
import com.kimlongdev.shopme.service.ProductService;
import com.kimlongdev.shopme.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserService userService;
    private final ProductService productService;
    private final CartItemService cartItemService;

    @GetMapping
    public ResponseEntity<Cart> handleFindUserCart(@RequestHeader("Authorization") String jwt) throws UserException, Exception {

        User user=userService.findUserByJwt(jwt);

        Cart cart=cartService.findUserCart(user);

        System.out.println("cart - "+cart.getUser().getEmail());

        return new ResponseEntity<Cart>(cart, HttpStatus.OK);
    }

    @PutMapping("/add")
    public ResponseEntity<CartItem> addItemToCart(@RequestBody AddItemRequest req,
                                                  @RequestHeader("Authorization") String jwt) throws Exception {

        User user=userService.findUserByJwt(jwt);
        Product product=productService.findProductById(req.getProductId());

        CartItem item = cartService.addCartItem(user,
                product,
                req.getSize(),
                req.getQuantity());


        return new ResponseEntity<>(item,HttpStatus.ACCEPTED);

    }

    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<ApiResponse> handleDeleteCartItem(
            @PathVariable Long cartItemId,
            @RequestHeader("Authorization")String jwt)
            throws Exception {

        User user=userService.findUserByJwt(jwt);
        cartItemService.removeCartItem(user.getId(), cartItemId);

        ApiResponse res=new ApiResponse();
        res.setMessage("Cart item removed successfully");

        return new ResponseEntity<ApiResponse>(res,HttpStatus.ACCEPTED);
    }

    @PutMapping("/item/{cartItemId}")
    public ResponseEntity<CartItem> handleUpdateCartItem(
            @PathVariable Long cartItemId,
            @RequestBody CartItem cartItem,
            @RequestHeader("Authorization")String jwt)
            throws Exception {

        User user=userService.findUserByJwt(jwt);

        CartItem updatedCartItem = null;
        if(cartItem.getQuantity()>0){
            updatedCartItem=cartItemService.updateCartItem(user.getId(),
                    cartItemId, cartItem);
        }
        return new ResponseEntity<>(updatedCartItem,HttpStatus.ACCEPTED);
    }
}
