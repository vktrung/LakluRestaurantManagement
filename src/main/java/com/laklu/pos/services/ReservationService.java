package com.laklu.pos.services;

import com.laklu.pos.dataObjects.request.UpdateReservationRequest;
import com.laklu.pos.entities.Reservation;
import com.laklu.pos.entities.ReservationTable;
import com.laklu.pos.entities.Table;
import com.laklu.pos.enums.StatusTable;
import com.laklu.pos.dataObjects.request.ReservationRequest;
import com.laklu.pos.exceptions.httpExceptions.NotFoundException;
import com.laklu.pos.mapper.ReservationMapper;
import com.laklu.pos.repositories.ReservationRepository;
import com.laklu.pos.repositories.ReservationTableRepository;
import com.laklu.pos.repositories.TableRepository;
import com.laklu.pos.validator.RuleValidator;
import com.laklu.pos.validator.TableMustAvailable;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ReservationService {

    ReservationRepository reservationRepository;
    TableRepository tableRepository;
    ReservationTableRepository reservationTableRepository;
    ReservationMapper reservationMapper;

    @Transactional
    public Reservation createReservation(ReservationRequest request) {
        Reservation reservation = reservationMapper.toEntity(request);

        reservation.setReservationTime(LocalDateTime.now());

        reservation = reservationRepository.save(reservation);

        List<Table> tables = tableRepository.findAllById(request.getTableIds());

        RuleValidator.validate(new TableMustAvailable(tables));

        tables.forEach(table -> table.setStatus(StatusTable.OCCUPIED));

        tableRepository.saveAll(tables);

        // Gọi lại phương thức chung
        createReservationTables(reservation, tables, reservation.getReservationTime());

        return reservation;
    }


    public Reservation updateReservation(Integer reservationId, UpdateReservationRequest request) {
        Reservation reservation = findOrFail(reservationId);

        reservationMapper.updateReservation(request, reservation);

        if (request.getTableIds() != null && !request.getTableIds().isEmpty()) {

            List<ReservationTable> oldTables = reservationTableRepository.findByReservation(reservation);

            List<Table> tableToRelease = oldTables.stream()
                    .map(ReservationTable::getTable)
                    .collect(Collectors.toList());

            tableToRelease.forEach(table -> table.setStatus(StatusTable.AVAILABLE));

            tableRepository.saveAll(tableToRelease);

            reservationTableRepository.deleteAll(oldTables);

            List<Table> newTables = tableRepository.findAllById(request.getTableIds());

            RuleValidator.validate(new TableMustAvailable(newTables));

            newTables.forEach(table -> table.setStatus(StatusTable.OCCUPIED));

            tableRepository.saveAll(newTables);

            // Gọi lại phương thức chung
            createReservationTables(reservation, newTables, LocalDateTime.now());
        }

        return reservationRepository.save(reservation);
    }


    public Optional<Reservation> findReservationById(Integer id) {
        return reservationRepository.findById(id);
    }

    public Reservation findOrFail(Integer id) {
        return this.findReservationById(id).orElseThrow(NotFoundException::new);
    }

    private void createReservationTables(Reservation reservation, List<Table> tables, LocalDateTime createdAt) {
        List<ReservationTable> reservationTables = tables.stream()
                .map(table -> ReservationTable.builder()
                        .reservation(reservation)
                        .table(table)
                        .createdAt(createdAt)
                        .build())
                .collect(Collectors.toList());

        reservationTableRepository.saveAll(reservationTables);
    }
}
