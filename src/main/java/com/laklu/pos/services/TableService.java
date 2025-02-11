package com.laklu.pos.services;

import com.laklu.pos.dataObjects.request.TableCreationRequest;
import com.laklu.pos.dataObjects.request.TableUpdateRequest;
import com.laklu.pos.entities.Tables;
import com.laklu.pos.enums.Status_Table;
import com.laklu.pos.repositories.TableRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TableService {

    TableRepository tableRepository;

    public Tables createTable(TableCreationRequest request) {
        log.info("Creating table: {}", request);

        Tables table = Tables.builder()
                .tableNumber(request.getTableNumber())
                .capacity(request.getCapacity())
                .status(Status_Table.AVAILABLE) // Mặc định AVAILABLE
                .build();

        return tableRepository.save(table);
    }



    public List<Tables> getAllTables() {
        return tableRepository.findAll();
    }


    public Tables getTableById(Integer id) {
        return tableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found with id: " + id));
    }


    public Tables updateTable(Integer id, TableUpdateRequest request) {
        return tableRepository.findById(id).map(existingTable -> {
            if (request.getTableNumber() != null) {
                existingTable.setTableNumber(request.getTableNumber());
            }
            if (request.getCapacity() != null) {
                existingTable.setCapacity(request.getCapacity());
            }
            if (request.getStatus() != null) {
                existingTable.setStatus(request.getStatus());
            }
            return tableRepository.save(existingTable);
        }).orElseThrow(() -> new RuntimeException("Table not found with id: " + id));
    }

    public Tables updateTableStatus(Integer id, Status_Table status) {
        return tableRepository.findById(id).map(existingTable -> {
            existingTable.setStatus(status);
            return tableRepository.save(existingTable);
        }).orElseThrow(() -> new RuntimeException("Table not found with id: " + id));
    }



    public void deleteTable(Integer id) {
        if (!tableRepository.existsById(id)) {
            throw new RuntimeException("Table not found with id: " + id);
        }
        tableRepository.deleteById(id);
    }
}
