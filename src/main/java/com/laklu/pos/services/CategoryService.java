package com.laklu.pos.services;

import com.laklu.pos.dataObjects.request.CategoryRequest;
import com.laklu.pos.dataObjects.response.CategoryResponse;
import com.laklu.pos.entities.Category;
import com.laklu.pos.mapper.CategoryMapper;
import com.laklu.pos.repositories.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryMapper categoryMapper;
    // Lấy danh sách tất cả Categories
    @Transactional
    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categoryMapper.toResponseList(categories);
    }

    // Lấy một Category theo ID
    @Transactional
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục với ID: " + id));
        return categoryMapper.toResponse(category);
    }

    // Thêm mới Category
    @Transactional
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        Category category = categoryMapper.toEntity(categoryRequest);
        category.setCreatedAt(new java.util.Date());
        category.setUpdatedAt(new java.util.Date());
        category.setIsDeleted(false);
        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    // Cập nhật Category
    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            category.setName(categoryRequest.getName());
            category.setDescription(categoryRequest.getDescription());
            category.setUpdatedAt(new java.util.Date());
            return categoryMapper.toResponse(categoryRepository.save(category));
        }
        return null;
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
}
