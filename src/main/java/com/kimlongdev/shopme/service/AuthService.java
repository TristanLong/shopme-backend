package com.kimlongdev.shopme.service;

import com.kimlongdev.shopme.response.SignUpRequest;
import jakarta.servlet.ServletException;

public interface AuthService {

    public String createUser(SignUpRequest signUpRequest) throws Exception;
    void sentLoginOtp(String email) throws Exception;
}
