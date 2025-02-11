package com.laklu.pos.controllers;

import com.laklu.pos.dataObjects.request.TableCreationRequest;
import com.laklu.pos.dataObjects.request.TableUpdateRequest;
import com.laklu.pos.dataObjects.response.ApiResponse;
import com.laklu.pos.entities.Tables;
import com.laklu.pos.enums.Status_Table;
import com.laklu.pos.services.TableService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/tables")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TableController {
    TableService tableService;

    @PostMapping
    ApiResponse<Tables> createTable(@RequestBody TableCreationRequest request) {

        ApiResponse<Tables> apiResponse = new ApiResponse<>();

        apiResponse.setResult(tableService.createTable(request));

        return apiResponse;
    }

    // READ (Lấy danh sách tất cả bàn)
    @GetMapping
    ApiResponse<List<Tables>> getAllTables() {
        return ApiResponse.<List<Tables>>builder()
                .result(tableService.getAllTables())
                .build();
    }

    // READ (Lấy thông tin một bàn theo ID)
    @GetMapping("/{id}")
    ApiResponse<Tables> getTableById(@PathVariable Integer id) {
        return ApiResponse.<Tables>builder()
                .result(tableService.getTableById(id))
                .build();
    }

    // UPDATE (Cập nhật bàn)
    @PutMapping("/{id}")
    ApiResponse<Tables> updateTable(@PathVariable Integer id, @RequestBody TableUpdateRequest request) {

        return ApiResponse.<Tables>builder()
                .result(tableService.updateTable(id, request))
                .build();
    }

    // DELETE (Xóa bàn)
    @DeleteMapping("/{id}")
    ApiResponse<Void> deleteTable(@PathVariable Integer id) {
        tableService.deleteTable(id);
        return ApiResponse.<Void>builder()
                .build();
    }



}
