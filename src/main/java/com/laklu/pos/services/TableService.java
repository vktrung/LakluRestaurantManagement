package com.laklu.pos.services;

import com.laklu.pos.dataObjects.request.NewTable;
import com.laklu.pos.dataObjects.request.TableUpdateRequest;
import com.laklu.pos.entities.Table;
import com.laklu.pos.enums.StatusTable;
import com.laklu.pos.exceptions.httpExceptions.NotFoundException;
import com.laklu.pos.mapper.TableMapper;
import com.laklu.pos.repositories.TableRepository;
import com.laklu.pos.validator.RuleValidator;
import com.laklu.pos.validator.TableMustAvailable;
import com.laklu.pos.validator.TableMustBeDeletable;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TableService {

    TableRepository tableRepository;
    TableMapper tableMapper;

    public Table createTable(NewTable request) {
        Table table = tableMapper.toEntity(request); // Dùng MapStruct để chuyển đổi DTO thành Entity

        return tableRepository.save(table);
    }

    public Optional<Table> findByTableName(String tablename) {
        return tableRepository.findByTableNumber(tablename);
    }


    public List<Table> getAllTables() {
        return tableRepository.findAll();
    }


    public Table findOrFail(Integer id) {
        return this.findTableById(id).orElseThrow(NotFoundException::new);
    }

    public Optional<Table> findTableById(Integer id) {
        return tableRepository.findById(id);
    }


    public Table updateTable(Integer id, TableUpdateRequest request) {
        Table table = findOrFail(id);

        RuleValidator.validate(new TableMustAvailable(List.of(table)));

        tableMapper.updateTable(request, table);

        return tableRepository.save(table);
    }


    public void deleteTable(Integer id) {
        Table table = findOrFail(id);

        RuleValidator.validate(new TableMustBeDeletable(table));

        tableRepository.deleteById(id);
    }



    public Table updateTableStatus(Integer id, StatusTable status) {
        return tableRepository.findById(id).map(existingTable -> {
            existingTable.setStatus(status);
            return tableRepository.save(existingTable);
        }).orElseThrow(() -> new RuntimeException("Không tìm thấy bàn với ID " + id));
    }

}
