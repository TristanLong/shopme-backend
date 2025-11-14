package com.kimlongdev.shopme.controller;

import com.kimlongdev.shopme.modal.User;
import com.kimlongdev.shopme.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/profile")
    public ResponseEntity<User> handleCreateUser(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwt(jwt);

        return ResponseEntity.ok(user);
    }

}
