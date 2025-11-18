package com.kimlongdev.shopme.repository;

import com.kimlongdev.shopme.modal.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByCategoryId(String categoryId);

    List<Category> findByLevel(Integer level);
}
