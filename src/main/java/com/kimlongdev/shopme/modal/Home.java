package com.kimlongdev.shopme.modal;

import jakarta.persistence.Entity;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Home {

    private List<HomeCategory> grid;
    private List<HomeCategory> electricCategories;
    private List<HomeCategory> shopByCategories;
    private List<HomeCategory> dealCategories;
    private List<Deal> deals;
}
