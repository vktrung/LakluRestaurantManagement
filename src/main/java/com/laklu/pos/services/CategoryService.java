package com.laklu.pos.services;

import com.laklu.pos.dataObjects.request.CategoryRequest;
import com.laklu.pos.entities.Category;
import com.laklu.pos.exceptions.NotFoundCategory;
import com.laklu.pos.mapper.CategoryMapper;
import com.laklu.pos.repositories.CategoryRepository;
import com.laklu.pos.validator.Rule;
import com.laklu.pos.validator.RuleValidator;
import com.laklu.pos.validator.ValueExistIn;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public List<Category> getAllCategoriesDeleted() {
        return categoryRepository.findAllByIsDeletedFalse();
    }

    public Category createCategory(CategoryRequest categoryRequest) {
        // Check if category name already exists
        ValueExistIn<String> valueExistIn = new ValueExistIn<>(
                "name",
                categoryRequest.getName(),
                (name) -> categoryRepository.findByName(name).isEmpty()
        );
        valueExistIn.setMessage("Danh mục đã tồn tại");
        RuleValidator.validate(valueExistIn);

        Category category = categoryMapper.toEntity(categoryRequest);
        category.setCreatedAt(new java.util.Date());
        category.setUpdatedAt(new java.util.Date());
        category.setIsDeleted(false);

        return category;
    }

    @Transactional
    public Category updateCategory(Category category, CategoryRequest categoryRequest) {
        ValueExistIn<String> valueExistIn = new ValueExistIn<>(
                "name",
                categoryRequest.getName(),
                (name) -> categoryRepository.findByName(name).isEmpty()
        );
        valueExistIn.setMessage("Danh mục đã tồn tại");
        RuleValidator.validate(valueExistIn);

        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());
        category.setUpdatedAt(new java.util.Date());

        return category;
    }

    @Transactional
    public void deleteCategory(Category category) {
        category.setIsDeleted(true);
        categoryRepository.save(category);
    }

    public Category findOrFail(Long id) {
        return this.categoryRepository.findById(id).orElseThrow(NotFoundCategory::new);
    }
}
