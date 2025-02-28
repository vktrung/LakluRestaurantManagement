package com.laklu.pos.repositories;

import com.laklu.pos.entities.Reservation;
import com.laklu.pos.entities.ReservationTable;
import com.laklu.pos.entities.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationTableRepository extends JpaRepository<ReservationTable, Integer> {
    List<ReservationTable> findByReservation(Reservation reservation);

    @Query("SELECT COUNT(rt) FROM ReservationTable rt " +
            "WHERE rt.table.id = :tableId " +
            "AND FUNCTION('DATE', rt.reservation.reservationTime) = :date")
    long countByTableAndDate(@Param("tableId") Integer tableId, @Param("date") LocalDate date);

    @Query("SELECT COUNT(rt) FROM ReservationTable rt " +
            "WHERE rt.table.id = :tableId " +
            "AND FUNCTION('DATE', rt.reservation.checkIn) = :date " +
            "AND rt.reservation.status <> 'COMPLETED'")
    long countByTableAndDateAndNotCompleted(@Param("tableId") Integer tableId, @Param("date") LocalDate date);

    @Query("SELECT rt FROM ReservationTable rt " +
            "WHERE FUNCTION('DATE', rt.reservation.checkIn) = :localDate " +
            "AND rt.table IN :tables")
    List<ReservationTable> findReservationsDate(LocalDate localDate, List<Table> tables);

    @Query("SELECT rt FROM ReservationTable rt " +
            "WHERE rt.reservation = :reservation " +
            "AND rt.table.id = :tables")
    List<ReservationTable> findByReservationAndTables(@Param("reservation") Reservation reservation, @Param("tables") List<Integer> tables);
}

