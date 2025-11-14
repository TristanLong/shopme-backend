package com.kimlongdev.shopme.service.impl;

import com.kimlongdev.shopme.config.JwtProvider;
import com.kimlongdev.shopme.modal.User;
import com.kimlongdev.shopme.repository.UserRepository;
import com.kimlongdev.shopme.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Override
    public User findUserByJwt(String jwt) throws Exception {
        String email=jwtProvider.getEmailFromJwtToken(jwt);

        return userRepository.findByEmail(email);
    }

    @Override
    public User findUserByEmail(String email) throws Exception {
        User user=userRepository.findByEmail(email);

        if(user==null) {
            throw new Exception("user not exist with email " + email);
        }

        return user;
    }
}
