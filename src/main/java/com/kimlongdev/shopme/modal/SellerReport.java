package com.kimlongdev.shopme.modal;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SellerReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Seller seller;

    private long totalEarnings = 0L;
    private long totalSales = 0L;
    private long totalRefunds = 0L;
    private long totalTax = 0L;
    private long netEarnings = 0L;
    private int totalOrders = 0;
    private int cancelledOrders = 0;
    private int totalTransactions = 0;
}
