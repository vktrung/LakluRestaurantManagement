package com.laklu.pos.controllers;

import com.laklu.pos.dataObjects.request.TableCreationRequest;
import com.laklu.pos.dataObjects.request.TableUpdateRequest;
import com.laklu.pos.dataObjects.response.ApiResponse;
import com.laklu.pos.entities.Tables;
import com.laklu.pos.enums.StatusTable;
import com.laklu.pos.services.TableService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    public Tables createTable(@Valid @RequestBody TableCreationRequest request) {
        return tableService.createTable(request);
    }

    @GetMapping
    public List<Tables> getAllTables() {
        return tableService.getAllTables();
    }

    @GetMapping("/{id}")
    public Tables getTableById(@PathVariable Integer id) {
        return tableService.getTableById(id);
    }

    @PutMapping("/{id}")
    public Tables updateTable(@PathVariable Integer id, @Valid @RequestBody TableUpdateRequest request) {
        return tableService.updateTable(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTable(@PathVariable Integer id) {
        Tables table = tableService.getTableById(id);

        // Kiểm tra nếu bàn đã được đặt trước không cho xoá
        if (table.getStatus() == StatusTable.RESERVED || table.getStatus() == StatusTable.OCCUPIED) {
            throw new RuntimeException("Cannot delete table that is currently reserved or occupied.");
        }

        tableService.deleteTable(id);
    }




}
