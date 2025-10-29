package com.kimlongdev.shopme.modal;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "categories")
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @NotNull
    @Column(unique = true)
    private String categoryId;

    @ManyToOne
    private Category parentCategory; // Self-referencing many-to-one relationship

    @NotNull
    private int level; // Level in the hierarchy (0 for root, 1 for sub-category, etc.)
}
