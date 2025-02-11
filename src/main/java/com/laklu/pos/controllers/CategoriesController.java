package com.laklu.pos.controllers;

import com.laklu.pos.dataObjects.reponse.ResponseData;
import com.laklu.pos.dataObjects.reponse.ResponseError;
import com.laklu.pos.entities.Categories;
import com.laklu.pos.services.CategoriesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categories")
@Validated
@Slf4j
public class CategoriesController {
    private final CategoriesService categoriesService;

    public CategoriesController(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @PostMapping("/add")
    public ResponseData<Categories> addCategory(@RequestBody Categories category) {
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Tên danh mục không được để trống");
        }
        if (category.getDescription() == null || category.getDescription().trim().isEmpty()) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Mô tả không được để trống");
        }

        try {
            Categories savedCategory = categoriesService.addCategory(category);
            return new ResponseData<>(HttpStatus.OK.value(), "Add category thành công", savedCategory);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lỗi khi thêm category");
        }
    }

    @GetMapping
    public ResponseData<List<Categories>> getAllCategories() {
        try {
            List<Categories> categories = categoriesService.getAllCategories();
            return new ResponseData<>(HttpStatus.OK.value(), "Lấy dữ liệu category thành công", categories);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lỗi khi lấy danh sách category");
        }
    }

    @PutMapping("/update/{id}")
    public ResponseData<Categories> updateCategory(@PathVariable int id, @RequestBody Categories category) {
        if (category.getName() == null || category.getName().trim().isEmpty()) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Tên danh mục không được để trống");
        }
        if (category.getDescription() == null || category.getDescription().trim().isEmpty()) {
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Mô tả không được để trống");
        }

        try {
            Categories updatedCategory = categoriesService.updateCategory(id, category);
            return new ResponseData<>(HttpStatus.OK.value(), "Update category thành công", updatedCategory);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lỗi khi cập nhật category");
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseData<Void> deleteCategory(@PathVariable int id) {
        try {
            categoriesService.deleteCategory(id);
            return new ResponseData<>(HttpStatus.OK.value(), "Delete category thành công");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lỗi khi xóa category");
        }
    }


}
