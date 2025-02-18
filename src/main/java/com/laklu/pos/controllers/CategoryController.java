package com.laklu.pos.controllers;

import com.laklu.pos.auth.JwtGuard;
import com.laklu.pos.auth.policies.CategoryPolicy;
import com.laklu.pos.dataObjects.ApiResponseEntity;
import com.laklu.pos.dataObjects.request.CategoryRequest;
import com.laklu.pos.dataObjects.response.CategoryResponse;
import com.laklu.pos.exceptions.httpExceptions.ForbiddenException;
import com.laklu.pos.services.CategoryService;
import com.laklu.pos.uiltis.Ultis;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryPolicy categoryPolicy;

    @Autowired
    private CategoryService categoryService;

    // Lấy danh sách tất cả categories
    @GetMapping
    public ApiResponseEntity getAllCategories() throws Exception{
        Ultis.throwUnless(categoryPolicy.canList(JwtGuard.userPrincipal()), new ForbiddenException());
        List<CategoryResponse> categories = categoryService.getAllCategories();
        return ApiResponseEntity.success(categories, "Lấy danh sách categories thành công");
    }

    // Lấy thông tin một category theo ID
    @GetMapping("/{id}")
    public ApiResponseEntity getCategoryById(@PathVariable Long id) throws Exception{
        var category = categoryService.findOrFail(id);
        Ultis.throwUnless(categoryPolicy.canView(JwtGuard.userPrincipal(), category), new ForbiddenException());
        CategoryResponse categoryResponse = categoryService.getCategoryById(id);
        if (category == null) {
            return ApiResponseEntity.exception(HttpStatus.NOT_FOUND, "Không tìm thấy category với ID: " + id);
        }
        return ApiResponseEntity.success(categoryResponse, "Lấy thông tin category thành công");
    }

    // Thêm mới category
    @PostMapping
    public ApiResponseEntity createCategory( @Valid @RequestBody CategoryRequest categoryRequest)throws Exception{
        Ultis.throwUnless(categoryPolicy.canCreate(JwtGuard.userPrincipal()), new ForbiddenException());
        CategoryResponse createdCategory = categoryService.createCategory(categoryRequest);
        return ApiResponseEntity.success(createdCategory, "Tạo category thành công");
    }


    // Cập nhật category
    @PutMapping("/{id}")
    public ApiResponseEntity updateCategory(@PathVariable Long id, @RequestBody CategoryRequest categoryRequest) throws Exception{
        var  category = categoryService.findOrFail(id);
        Ultis.throwUnless(categoryPolicy.canEdit(JwtGuard.userPrincipal(), category), new ForbiddenException());
        CategoryResponse updatedCategory = categoryService.updateCategory(id, categoryRequest);
        if (updatedCategory == null) {
            return ApiResponseEntity.exception(HttpStatus.NOT_FOUND, "Không tìm thấy category với ID: " + id);
        }
        return ApiResponseEntity.success(updatedCategory, "Cập nhật category thành công");
    }

    // Xóa mềm category (Soft Delete)
    @DeleteMapping("/{id}")
    public ApiResponseEntity deleteCategory(@PathVariable Long id) throws Exception{
        var category = categoryService.findOrFail(id);
        Ultis.throwUnless(categoryPolicy.canDelete(JwtGuard.userPrincipal(), category), new ForbiddenException());
        categoryService.deleteCategory(id);
        return ApiResponseEntity.success(null, "Xóa category thành công");
    }
}
