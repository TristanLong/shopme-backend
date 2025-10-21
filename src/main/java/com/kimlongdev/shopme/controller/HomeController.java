package com.kimlongdev.shopme.controller;

import com.kimlongdev.shopme.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping
    public ApiResponse index() {
        ApiResponse response = new ApiResponse();
        response.setMessage("Welcome to Shopme Application");
        return response;
    }
}
