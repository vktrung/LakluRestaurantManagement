package com.laklu.pos.repositories;

import com.laklu.pos.entities.Reservation;
import com.laklu.pos.entities.ReservationTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationTableRepository extends JpaRepository<ReservationTable, Integer> {
    List<ReservationTable> findByReservation(Reservation reservation);
}
