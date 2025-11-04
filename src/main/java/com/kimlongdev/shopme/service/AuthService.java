package com.kimlongdev.shopme.service;

import com.kimlongdev.shopme.response.SignUpRequest;
import jakarta.servlet.ServletException;

public interface AuthService {

    public String createUser(SignUpRequest signUpRequest);
}
