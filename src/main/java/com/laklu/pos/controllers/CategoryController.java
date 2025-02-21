package com.laklu.pos.controllers;

import com.laklu.pos.auth.JwtGuard;
import com.laklu.pos.auth.policies.CategoryPolicy;
import com.laklu.pos.dataObjects.ApiResponseEntity;
import com.laklu.pos.dataObjects.request.CategoryRequest;
import com.laklu.pos.entities.Category;
import com.laklu.pos.exceptions.httpExceptions.ForbiddenException;
import com.laklu.pos.mapper.CategoryMapper;
import com.laklu.pos.services.CategoryService;
import com.laklu.pos.uiltis.Ultis;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@Tag(name = "Category Controller", description = "Quản lý danh mục")
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryPolicy categoryPolicy;

    private final CategoryService categoryService;

    private final CategoryMapper categoryMapper;

    // TODO: thêm filter deleted hoặc not deleted
    @Operation(summary = "Lấy danh sách categories", description = "API này dùng để lấy toàn bộ các categories")
    @GetMapping("/")
    public ApiResponseEntity getAllCategories() throws Exception{
        Ultis.throwUnless(categoryPolicy.canList(JwtGuard.userPrincipal()), new ForbiddenException());

        List<Category> categories = categoryService.getAllCategories();

        return ApiResponseEntity.success(categoryMapper.toResponseList(categories), "Lấy danh sách categories thành công");
    }


    // TODO: khi deleted = true thì không hiển thị là đúng nghiệp vụ hay 0 ?
    @Operation(summary = "Lấy thông tin category", description = "API này dùng để lấy thông tin category")
    @GetMapping("/{id}")
    public ApiResponseEntity getCategoryById(@PathVariable Long id) throws Exception{
        var category = categoryService.findOrFail(id);
        Ultis.throwUnless(categoryPolicy.canView(JwtGuard.userPrincipal(), category), new ForbiddenException());


        return ApiResponseEntity.success(categoryMapper.toResponse(category), "Lấy thông tin category thành công");
    }

    @Operation(summary = "Tạo category", description = "API này dùng để tạo category mới")
    @PostMapping("/")
    public ApiResponseEntity createCategory( @Valid @RequestBody CategoryRequest categoryRequest)throws Exception{
        Ultis.throwUnless(categoryPolicy.canCreate(JwtGuard.userPrincipal()), new ForbiddenException());

        Category category = categoryService.createCategory(categoryRequest);

        return ApiResponseEntity.success(categoryMapper.toResponse(category), "Tạo category thành công");
    }

    @Operation(summary = "Cập nhật category", description = "API này dùng để cập nhật thông tin category")
    @PutMapping("/{id}")
    public ApiResponseEntity updateCategory(@PathVariable Long id, @RequestBody Map<String, Object> updates) throws Exception{
        Category category = categoryService.findOrFail(id);
        Ultis.throwUnless(categoryPolicy.canEdit(JwtGuard.userPrincipal(), category), new ForbiddenException());

        Category updatedCategory = categoryService.updateCategoryPartially(category, updates);

        return ApiResponseEntity.success(categoryMapper.toResponse(updatedCategory), "Cập nhật category thành công");
    }

    @Operation(summary = "Xóa category", description = "API này dùng để xóa category")
    @DeleteMapping("/{id}")
    public ApiResponseEntity deleteCategory(@PathVariable Long id) throws Exception{
        var category = categoryService.findOrFail(id);
        Ultis.throwUnless(categoryPolicy.canDelete(JwtGuard.userPrincipal(), category), new ForbiddenException());

        categoryService.deleteCategory(category);

        return ApiResponseEntity.success(null, "Xóa category thành công");
    }
}
