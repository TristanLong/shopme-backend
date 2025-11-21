package com.kimlongdev.shopme.controller;


import com.kimlongdev.shopme.domain.PaymentMethod;
import com.kimlongdev.shopme.modal.*;
import com.kimlongdev.shopme.response.PaymentLinkResponse;
import com.kimlongdev.shopme.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final OrderItemService orderItemService;
    private final CartService cartService;
    private final SellerService sellerService;
    private final SellerReportService sellerReportService;


    @PostMapping()
    public ResponseEntity<PaymentLinkResponse> createOrderHandler(
            @RequestBody Address shippingAddress,
            @RequestParam PaymentMethod paymentMethod,
            @RequestHeader("Authorization")String jwt)
            throws Exception {

        User user=userService.findUserByJwt(jwt);
        Cart cart=cartService.findUserCart(user);
        Set<Order> orders =orderService.createOrder(user, shippingAddress,cart);

        PaymentLinkResponse res = new PaymentLinkResponse();

        return new ResponseEntity<>(res,HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity< List<Order>> usersOrderHistoryHandler(
            @RequestHeader("Authorization")
            String jwt) throws Exception {

        User user=userService.findUserByJwt(jwt);
        List<Order> orders=orderService.usersOrderHistory(user.getId());
        return new ResponseEntity<>(orders,HttpStatus.ACCEPTED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity< Order> getOrderById(@PathVariable Long orderId, @RequestHeader("Authorization")
    String jwt) throws Exception {

        User user = userService.findUserByJwt(jwt);
        Order orders=orderService.findOrderById(orderId);
        return new ResponseEntity<>(orders,HttpStatus.ACCEPTED);
    }

    @GetMapping("/item/{orderItemId}")
    public ResponseEntity<OrderItem> getOrderItemById(
            @PathVariable Long orderItemId, @RequestHeader("Authorization")
            String jwt) throws Exception {
        System.out.println("------- controller ");
        User user = userService.findUserByJwt(jwt);
        OrderItem orderItem=orderItemService.getOrderItemById(orderItemId);
        return new ResponseEntity<>(orderItem, HttpStatus.ACCEPTED);
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrder(
            @PathVariable Long orderId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        User user=userService.findUserByJwt(jwt);
        Order order=orderService.cancelOrder(orderId,user);

        Seller seller= sellerService.getSellerById(order.getSellerId());
        SellerReport report=sellerReportService.getSellerReport(seller);

        report.setCancelledOrders(report.getCancelledOrders()+1);
        report.setTotalRefunds(report.getTotalRefunds()+order.getTotalSellingPrice());
        sellerReportService.updateSellerReport(report);

        return ResponseEntity.ok(order);
    }

}
