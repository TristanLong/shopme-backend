package com.kimlongdev.shopme.repository;

import com.kimlongdev.shopme.domain.AccountStatus;
import com.kimlongdev.shopme.modal.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface  SellerRepository extends JpaRepository<Seller, Long> {
    Seller findByEmail(String email);

    List<Seller> findByAccountStatus(AccountStatus status);
}
