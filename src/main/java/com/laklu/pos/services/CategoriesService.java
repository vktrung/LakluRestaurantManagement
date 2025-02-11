package com.laklu.pos.services;

import com.laklu.pos.entities.Categories;
import com.laklu.pos.repositories.CategoriesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CategoriesService {
    private final CategoriesRepository categoriesRepository;

    public CategoriesService(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    public Categories addCategory(Categories category) {
        validateCategory(category);
        return categoriesRepository.save(category);
    }

    public List<Categories> getAllCategories() {
        return categoriesRepository.findAll();
    }

    public Categories updateCategory(int id, Categories updatedCategory) {
        validateCategory(updatedCategory);

        return categoriesRepository.findById(id).map(category -> {
            category.setName(updatedCategory.getName());
            category.setDescription(updatedCategory.getDescription());
            category.setUpdatedAt(updatedCategory.getUpdatedAt());
            return categoriesRepository.save(category);
        }).orElseThrow(() -> {
            return new IllegalArgumentException("Category not found with ID: " + id);
        });
    }

    public void deleteCategory(int id) {
        Optional<Categories> category = categoriesRepository.findById(id);
        if (category.isEmpty()) {
            throw new IllegalArgumentException("Category not found with ID: " + id);
        }
        categoriesRepository.deleteById(id);
    }

    private void validateCategory(Categories category) {
        if (category == null) {
            throw new IllegalArgumentException("Category object cannot be null");
        }
        if (!StringUtils.hasText(category.getName())) {
            throw new IllegalArgumentException("Category name cannot be empty");
        }
        if (!StringUtils.hasText(category.getDescription())) {
            throw new IllegalArgumentException("Category description cannot be empty");
        }
    }
}
