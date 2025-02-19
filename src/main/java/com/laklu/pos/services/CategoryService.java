package com.laklu.pos.services;

import com.laklu.pos.dataObjects.request.CategoryRequest;
import com.laklu.pos.dataObjects.response.CategoryResponse;
import com.laklu.pos.entities.Category;
import com.laklu.pos.exceptions.CategoryAlreadyExistsException;
import com.laklu.pos.exceptions.NotFoundCategory;
import com.laklu.pos.exceptions.RestHttpException;
import com.laklu.pos.mapper.CategoryMapper;
import com.laklu.pos.repositories.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@AllArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    // Lấy danh sách tất cả Categories
    @Transactional
    public List<Category> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories;
    }

    // Lấy một Category theo ID
    public Category getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new NotFoundCategory());
        return category;
    }

    // Thêm mới Category
    @Transactional
    public Category createCategory(CategoryRequest categoryRequest) {
       categoryRepository.findByName(categoryRequest.getName()).ifPresent(existingCategory -> {
            throw new CategoryAlreadyExistsException();
        });

        Category category = categoryMapper.toEntity(categoryRequest);
        category.setCreatedAt(new java.util.Date());
        category.setUpdatedAt(new java.util.Date());
        category.setIsDeleted(false);
        return categoryRepository.save(category);
    }

    // Xóa mềm (Soft Delete)
    @Transactional
    public void deleteCategory(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        optionalCategory.ifPresent(category -> {
            category.setIsDeleted(true);
            categoryRepository.save(category);
        });
    }

    public Optional<Category> findByCategoryById(Long id) {
        return categoryRepository.findById(id);
    }
    public Category findOrFail(Long id) {
        return this. findByCategoryById(id).orElseThrow(()
                -> new NotFoundCategory());
    }

    @Transactional
    public Category updateCategoryPartially(Long id, Map<String, Object> updates) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundCategory());
        updates.forEach((key, value) -> {
            switch (key) {
                case "name" -> category.setName((String) value);
                case "description" -> category.setDescription((String) value);
            }
        });

        category.setUpdatedAt(new Date());
        return categoryRepository.save(category);
    }
}
