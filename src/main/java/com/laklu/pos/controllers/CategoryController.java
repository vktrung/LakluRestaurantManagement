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
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryPolicy categoryPolicy;

    private final CategoryService categoryService;

    private final CategoryMapper categoryMapper;

    // TODO: thêm filter deleted hoặc not deleted
    @GetMapping("/")
    public ApiResponseEntity getAllCategories() throws Exception{
        Ultis.throwUnless(categoryPolicy.canList(JwtGuard.userPrincipal()), new ForbiddenException());

        List<Category> categories = categoryService.getAllCategories();

        return ApiResponseEntity.success(categoryMapper.toResponseList(categories), "Lấy danh sách categories thành công");
    }


    // TODO: khi deleted = true thì không hiển thị là đúng nghiệp vụ hay 0 ?
    @GetMapping("/{id}")
    public ApiResponseEntity getCategoryById(@PathVariable Long id) throws Exception{
        var category = categoryService.findOrFail(id);
        Ultis.throwUnless(categoryPolicy.canView(JwtGuard.userPrincipal(), category), new ForbiddenException());


        return ApiResponseEntity.success(categoryMapper.toResponse(category), "Lấy thông tin category thành công");
    }

    @PostMapping("/")
    public ApiResponseEntity createCategory( @Valid @RequestBody CategoryRequest categoryRequest)throws Exception{
        Ultis.throwUnless(categoryPolicy.canCreate(JwtGuard.userPrincipal()), new ForbiddenException());

        Category category = categoryService.createCategory(categoryRequest);

        return ApiResponseEntity.success(categoryMapper.toResponse(category), "Tạo category thành công");
    }

    @PutMapping("/{id}")
    public ApiResponseEntity updateCategory(@PathVariable Long id, @RequestBody CategoryRequest categoryRequest) throws Exception{
        Category category = categoryService.findOrFail(id);
        Ultis.throwUnless(categoryPolicy.canEdit(JwtGuard.userPrincipal(), category), new ForbiddenException());

        Category updatedCategory = categoryService.updateCategoryPartially(category, categoryRequest);

        return ApiResponseEntity.success(categoryMapper.toResponse(updatedCategory), "Cập nhật category thành công");
    }

    @DeleteMapping("/{id}")
    public ApiResponseEntity deleteCategory(@PathVariable Long id) throws Exception{
        var category = categoryService.findOrFail(id);
        Ultis.throwUnless(categoryPolicy.canDelete(JwtGuard.userPrincipal(), category), new ForbiddenException());

        categoryService.deleteCategory(category);

        return ApiResponseEntity.success(null, "Xóa category thành công");
    }
}
