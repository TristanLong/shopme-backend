package com.kimlongdev.shopme.repository;

import com.kimlongdev.shopme.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
