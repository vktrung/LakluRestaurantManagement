package com.laklu.pos.services;

import com.laklu.pos.dataObjects.request.UpdateReservationRequest;
import com.laklu.pos.entities.Reservations;
import com.laklu.pos.entities.ReservationTable;
import com.laklu.pos.entities.Tables;
import com.laklu.pos.enums.StatusTable;
import com.laklu.pos.dataObjects.request.ReservationRequest;
import com.laklu.pos.repositories.ReservationRepository;
import com.laklu.pos.repositories.ReservationTableRepository;
import com.laklu.pos.repositories.TableRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ReservationService {

    ReservationRepository reservationRepository;
    TableRepository tableRepository;
    ReservationTableRepository reservationTableRepository;

    @Transactional
    public Reservations createReservation(ReservationRequest request) {
        log.info("Creating reservation for customer: {}", request.getCustomerName());

        Reservations reservation = Reservations.builder()
                .customerName(request.getCustomerName())
                .customerPhone(request.getCustomerPhone())
                .reservationTime(LocalDateTime.now())
                .status(Reservations.Status.PENDING)
                .build();

        reservation = reservationRepository.save(reservation);

        List<Tables> tables = validateTables(request.getTableIds());

        Map<Integer, Tables> tableMap = tables.stream()
                .collect(Collectors.toMap(Tables::getId, Function.identity()));

        for (Integer tableId : request.getTableIds()) {
            Tables table = tableMap.get(tableId);

            table.setStatus(StatusTable.OCCUPIED);
            tableRepository.save(table);

            ReservationTable reservationTable = ReservationTable.builder()
                    .reservation(reservation)
                    .table(table)
                    .createdAt(LocalDateTime.now())
                    .build();

            reservationTableRepository.save(reservationTable);
        }

        return reservation;
    }

    public Reservations updateReservation(Integer reservationId, UpdateReservationRequest request) {
        Reservations reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found with ID: " + reservationId));

        if (request.getCustomerName() != null) {
            reservation.setCustomerName(request.getCustomerName());
        }
        if (request.getCustomerPhone() != null) {
            reservation.setCustomerPhone(request.getCustomerPhone());
        }
        if (request.getReservationTime() != null) {
            reservation.setReservationTime(request.getReservationTime());
        }

        // Kiểm tra nếu khách muốn đổi bàn
        if (request.getTableIds() != null && !request.getTableIds().isEmpty()) {
            log.info("Updating tables for reservation ID: {}", reservationId);

            // Giải phóng bàn cũ
            List<ReservationTable> oldTables = reservationTableRepository.findByReservation(reservation);
            List<Tables> tablesToRelease = oldTables.stream()
                    .map(ReservationTable::getTable)
                    .collect(Collectors.toList());

            tablesToRelease.forEach(table -> table.setStatus(StatusTable.AVAILABLE));
            tableRepository.saveAll(tablesToRelease);
            reservationTableRepository.deleteAll(oldTables);

            // Kiểm tra bàn mới
            List<Tables> newTables = validateTables(request.getTableIds());

            // Cập nhật trạng thái bàn mới
            newTables.forEach(table -> table.setStatus(StatusTable.OCCUPIED));
            tableRepository.saveAll(newTables);

            List<ReservationTable> newReservationTables = newTables.stream()
                    .map(table -> ReservationTable.builder()
                            .reservation(reservation)
                            .table(table)
                            .createdAt(LocalDateTime.now())
                            .build())
                    .collect(Collectors.toList());

            reservationTableRepository.saveAll(newReservationTables);
        }

        // Lưu cập nhật vào database
        return reservationRepository.save(reservation);
    }

    private List<Tables> validateTables(List<Integer> tableIds) {
        List<Tables> tables = tableRepository.findAllById(tableIds);

        // Tạo Map để dễ dàng truy xuất thông tin
        Map<Integer, Tables> tableMap = tables.stream()
                .collect(Collectors.toMap(Tables::getId, Function.identity()));

        for (Integer tableId : tableIds) {
            Tables table = tableMap.get(tableId);
            if (table == null) {
                throw new RuntimeException("Table not found with ID: " + tableId);
            }
            log.info("Table {} found with status: {}", tableId, table.getStatus());

            if (table.getStatus() != StatusTable.AVAILABLE) {
                throw new RuntimeException("Table " + tableId + " is already reserved.");
            }
        }

        return tables;
    }


}
