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

        List<Tables> tables = tableRepository.findAllById(request.getTableIds());

        Map<Integer, Tables> tableMap = tables.stream()
                .collect(Collectors.toMap(Tables::getId, Function.identity()));

        for (Integer tableId : request.getTableIds()) {
            Tables table = tableMap.get(tableId);
            if (table == null) {
                throw new RuntimeException("Table not found with ID: " + tableId);
            }
            log.info("able {} found with status: {}", tableId, table.getStatus());

            if (table.getStatus() != StatusTable.AVAILABLE) {
                throw new RuntimeException("Table " + tableId + " is already reserved.");
            }

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

        // Cập nhật thông tin cá nhân nếu có
        //sau dùng mapstruc sẽ không phải if else đến chết
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

            // Xóa các bàn cũ
            List<ReservationTable> oldTables = reservationTableRepository.findByReservation(reservation);
            for (ReservationTable rt : oldTables) {
                Tables table = rt.getTable();
                table.setStatus(StatusTable.AVAILABLE); // Giải phóng bàn cũ
                tableRepository.save(table);
            }
            reservationTableRepository.deleteAll(oldTables); // Xóa bản ghi cũ trong `reservation_table`

            // Thêm các bàn mới
            for (Integer tableId : request.getTableIds()) {
                Tables newTable = tableRepository.findById(tableId)
                        .orElseThrow(() -> new RuntimeException("Table not found with ID: " + tableId));

                if (newTable.getStatus() != StatusTable.AVAILABLE) {
                    throw new RuntimeException("Table " + tableId + " is already occupied.");
                }

                newTable.setStatus(StatusTable.OCCUPIED);
                tableRepository.save(newTable);

                ReservationTable newReservationTable = ReservationTable.builder()
                        .reservation(reservation)
                        .table(newTable)
                        .createdAt(LocalDateTime.now())
                        .build();
                reservationTableRepository.save(newReservationTable);
            }
        }

        // Lưu cập nhật vào database
        return reservationRepository.save(reservation);
    }


}
