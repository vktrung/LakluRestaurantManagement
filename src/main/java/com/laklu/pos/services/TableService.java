package com.laklu.pos.services;

import com.laklu.pos.dataObjects.request.NewTable;
import com.laklu.pos.dataObjects.request.TableUpdateRequest;
import com.laklu.pos.entities.Tables;
import com.laklu.pos.enums.StatusTable;
import com.laklu.pos.exceptions.httpExceptions.NotFoundException;
import com.laklu.pos.mapper.TableMapper;
import com.laklu.pos.repositories.ReservationTableRepository;
import com.laklu.pos.repositories.TableRepository;
import com.laklu.pos.validator.RuleValidator;
import com.laklu.pos.validator.TableMustAvailable;
import com.laklu.pos.validator.TableMustBeDeletable;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TableService {

    TableRepository tableRepository;
    TableMapper tableMapper;
    ReservationTableRepository reservationTableRepository;

    public Tables createTable(NewTable request) {
        Tables tables = tableMapper.toEntity(request); // Dùng MapStruct để chuyển đổi DTO thành Entity

        return tableRepository.save(tables);
    }

    public Optional<Tables> findByTableName(String tablename) {
        return tableRepository.findByTableNumber(tablename);
    }


    public List<Tables> getAllTables() {
        return tableRepository.findAll();
    }


    public Tables findOrFail(Integer id) {
        return this.findTableById(id).orElseThrow(NotFoundException::new);
    }

    public Optional<Tables> findTableById(Integer id) {
        return tableRepository.findById(id);
    }


    public Tables updateTable(Integer id, TableUpdateRequest request) {
        Tables tables = findOrFail(id);

        RuleValidator.validate(new TableMustBeDeletable(tables));

        tableMapper.updateTable(request, tables);

        return tableRepository.save(tables);
    }


    public void deleteTable(Integer id) {
        Tables tables = findOrFail(id);

        RuleValidator.validate(new TableMustBeDeletable(tables));

        tableRepository.deleteById(id);
    }

    public Tables updateTableStatus(Integer id, StatusTable status) {
        return tableRepository.findById(id).map(existingTable -> {
            existingTable.setStatus(status);
            return tableRepository.save(existingTable);
        }).orElseThrow(() -> new RuntimeException("Không tìm thấy bàn với ID " + id));
    }

    public void checkAndUpdateTableStatus() {
        LocalDate today = LocalDate.now();
        List<Tables> tables = tableRepository.findAll();

        tables.forEach(table -> {
            long count = reservationTableRepository.countByTableAndDateAndNotCompleted(table.getId(), today);
            if (count > 0) {
                table.setStatus(StatusTable.RESERVED);
            } else {
                table.setStatus(StatusTable.AVAILABLE);
            }
        });

        tableRepository.saveAll(tables);
    }
}
