package com.laklu.pos.services;

import com.laklu.pos.entities.Categories;
import com.laklu.pos.repositories.CategoriesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import java.util.List;

@Slf4j
@Service
public class CategoriesService {
    private final CategoriesRepository categoriesRepository;

    public CategoriesService(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    public Categories addCategory(Categories category) {
        if (category.getId() != 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID must not be provided when creating a category");
        }
        return categoriesRepository.save(category);
    }


    public List<Categories> getAllCategories() {
        return categoriesRepository.findAll();
    }

    public Categories updateCategory(int id, Categories updatedCategory) {
        if (id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID phải lớn hơn 0");
        }


        Categories category = categoriesRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy category với ID: " + id));

        category.setName(updatedCategory.getName());
        category.setDescription(updatedCategory.getDescription());
        category.setUpdatedAt(updatedCategory.getUpdatedAt());

        return categoriesRepository.save(category);
    }

    public void deleteCategory(int id) {
        if (id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID phải lớn hơn 0");
        }

        if (!categoriesRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy category với ID: " + id);
        }

        categoriesRepository.deleteById(id);
    }


}
