package com.laklu.pos.controllers;

import com.laklu.pos.auth.JwtGuard;
import com.laklu.pos.auth.policies.CategoryPolicy;
import com.laklu.pos.dataObjects.ApiResponseEntity;
import com.laklu.pos.dataObjects.request.CategoryRequest;
import com.laklu.pos.dataObjects.response.CategoryResponse;
import com.laklu.pos.entities.Category;
import com.laklu.pos.exceptions.httpExceptions.ForbiddenException;
import com.laklu.pos.mapper.CategoryMapper;
import com.laklu.pos.services.CategoryService;
import com.laklu.pos.uiltis.Ultis;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categories")
@AllArgsConstructor
public class CategoryController {

    private final CategoryPolicy categoryPolicy;
    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    // Lấy danh sách tất cả categories
    @GetMapping
    public ApiResponseEntity getAllCategories() throws Exception{
        Ultis.throwUnless(categoryPolicy.canList(JwtGuard.userPrincipal()), new ForbiddenException());
        List<Category> categories = categoryService.getAllCategories();
        List<CategoryResponse> categoryResponses = categoryMapper.toResponseList(categories);
        return ApiResponseEntity.success(categoryResponses, "Lấy danh sách categories thành công");
    }

    // Lấy thông tin một category theo ID
    @GetMapping("/{id}")
    public ApiResponseEntity getCategoryById(@PathVariable Long id) throws Exception{
        var categoryExist = categoryService.findOrFail(id);
        Ultis.throwUnless(categoryPolicy.canView(JwtGuard.userPrincipal(), categoryExist), new ForbiddenException());
        Category category = categoryService.getCategoryById(id);
        CategoryResponse categoryResponse = categoryMapper.toResponse(category);
        return ApiResponseEntity.success(categoryResponse, "Lấy thông tin category thành công");
    }

    // Thêm mới category
    @PostMapping
    public ApiResponseEntity createCategory( @Valid @RequestBody CategoryRequest categoryRequest)throws Exception{
        Ultis.throwUnless(categoryPolicy.canCreate(JwtGuard.userPrincipal()), new ForbiddenException());
        Category category = categoryService.createCategory(categoryRequest);
        CategoryResponse createdCategory = categoryMapper.toResponse(category);
        return ApiResponseEntity.success(createdCategory, "Tạo category thành công");
    }

    // Cập nhật category
    @PatchMapping("/{id}")
    public ApiResponseEntity updateCategoryPartially(@PathVariable Long id, @RequestBody Map<String, Object> updates) throws Exception {
        var categoryExist = categoryService.findOrFail(id);
        Ultis.throwUnless(categoryPolicy.canEdit(JwtGuard.userPrincipal(), categoryExist), new ForbiddenException());
        Category category = categoryService.updateCategoryPartially(id, updates);
        CategoryResponse updatedCategory = categoryMapper.toResponse(category);
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
