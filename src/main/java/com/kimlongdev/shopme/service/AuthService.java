package com.kimlongdev.shopme.service;

import com.kimlongdev.shopme.request.LoginRequest;
import com.kimlongdev.shopme.response.AuthResponse;
import com.kimlongdev.shopme.response.SignUpRequest;

public interface AuthService {

    public String createUser(SignUpRequest signUpRequest) throws Exception;
    void sendLoginOtp(String email) throws Exception;
    AuthResponse signingIn(LoginRequest loginRequest) throws Exception;
}
