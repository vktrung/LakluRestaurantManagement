package com.laklu.pos.controllers;

import com.laklu.pos.dataObjects.reponse.ResponseData;
import com.laklu.pos.dataObjects.reponse.ResponseError;
import com.laklu.pos.entities.Categories;
import com.laklu.pos.services.CategoriesService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@Slf4j
public class CategoriesController {
    private final CategoriesService categoriesService;

    public CategoriesController(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @PostMapping
    public ResponseData<Categories> add(@Valid @RequestBody Categories category) {
        try {
            Categories savedCategory = categoriesService.addCategory(category);
            return new ResponseData<>(HttpStatus.OK.value(), "Add category thành công", savedCategory);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lỗi khi thêm category");
        }
    }

    @GetMapping
    public ResponseData<List<Categories>> show() {
        try {
            List<Categories> categories = categoriesService.getAllCategories();
            return new ResponseData<>(HttpStatus.OK.value(), "Lấy dữ liệu category thành công", categories);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lỗi khi lấy danh sách category");
        }
    }

    @PutMapping("/{id}")
    public ResponseData<Categories> update(@PathVariable int id, @Valid @RequestBody Categories category) {
        try {
            Categories updatedCategory = categoriesService.updateCategory(id, category);
            return new ResponseData<>(HttpStatus.OK.value(), "Update category thành công", updatedCategory);
        } catch (Exception e) {
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lỗi khi cập nhật category");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseData<Void> delete(@PathVariable int id) {
        try {
            categoriesService.deleteCategory(id);
            return new ResponseData<>(HttpStatus.OK.value(), "Delete category thành công");
        } catch (Exception e) {
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lỗi khi xóa category");
        }
    }
}