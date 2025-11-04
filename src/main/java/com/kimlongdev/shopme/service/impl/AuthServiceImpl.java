package com.kimlongdev.shopme.service.impl;

import com.kimlongdev.shopme.domain.USER_ROLE;
import com.kimlongdev.shopme.modal.User;
import com.kimlongdev.shopme.repository.UserRepository;
import com.kimlongdev.shopme.response.SignUpRequest;
import com.kimlongdev.shopme.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String createUser(SignUpRequest signUpRequest) {
        User user = userRepository.findByEmail(signUpRequest.getEmail());

        if (user == null) {
            User newUser = new User();
            newUser.setEmail(signUpRequest.getEmail());
            newUser.setFullName(signUpRequest.getFullName());
            newUser.setRole(USER_ROLE.ROLE_CUSTOMER);
            newUser.setMobile("0988888888");
            newUser.setPassword(passwordEncoder.encode(signUpRequest.getOtp()));

            user = userRepository.save(newUser);
        }
        return "";
    }
}
