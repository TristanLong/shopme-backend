package com.kimlongdev.shopme.modal;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String code;
    private Double discountPercentage;
    private LocalDate validityStartDate;
    private LocalDate validityEndDate;
    private double minimumOrderValue;
    private boolean isActive;

    @ManyToMany(mappedBy = "usedCoupons")
    private Set<User> usedByUsers = new HashSet<>();
}
