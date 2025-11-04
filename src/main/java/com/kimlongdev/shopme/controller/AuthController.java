package com.kimlongdev.shopme.controller;

import com.kimlongdev.shopme.repository.UserRepository;
import com.kimlongdev.shopme.modal.User;
import com.kimlongdev.shopme.response.SignUpRequest;
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

    @PostMapping("/signup")
    public ResponseEntity<User> handleCreateUser(@RequestBody SignUpRequest signUpRequest) {
        User user = new User();
        user.setEmail(signUpRequest.getEmail());
        user.setFullName(signUpRequest.getFullName());

        user = userRepository.save(user);
        return ResponseEntity.ok(user);
    }
}
