package com.kimlongdev.shopme.service;

import com.kimlongdev.shopme.modal.User;

public interface UserService {

    User findUserByJwt(String jwt) throws Exception;

    User findUserByEmail(String email) throws Exception;
}
