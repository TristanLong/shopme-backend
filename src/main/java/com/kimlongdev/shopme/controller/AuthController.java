package com.kimlongdev.shopme.controller;

import com.kimlongdev.shopme.domain.USER_ROLE;
import com.kimlongdev.shopme.modal.VerificationCode;
import com.kimlongdev.shopme.repository.UserRepository;
import com.kimlongdev.shopme.modal.User;
import com.kimlongdev.shopme.response.ApiResponse;
import com.kimlongdev.shopme.response.AuthResponse;
import com.kimlongdev.shopme.response.SignUpRequest;
import com.kimlongdev.shopme.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> handleCreateUser(@RequestBody SignUpRequest signUpRequest) throws Exception {
        String jwtToken = authService.createUser(signUpRequest);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwtToken(jwtToken);
        authResponse.setMessage("Register successfully");
        authResponse.setRole(USER_ROLE.ROLE_CUSTOMER);

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/send/login-signup-otp")
    public ResponseEntity<ApiResponse> handleSendOTP(@RequestBody VerificationCode req) throws Exception {
        authService.sentLoginOtp(req.getEmail());

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Send OTP successfully");

        return ResponseEntity.ok(apiResponse);
    }
}
