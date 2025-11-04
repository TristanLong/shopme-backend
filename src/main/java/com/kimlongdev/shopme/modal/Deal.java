package com.kimlongdev.shopme.modal;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Deal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private int discount;

    @OneToOne
    private HomeCategory category;

}
