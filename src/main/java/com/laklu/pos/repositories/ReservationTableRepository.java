package com.laklu.pos.repositories;

import com.laklu.pos.entities.ReservationTable;
import com.laklu.pos.entities.Reservations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationTableRepository extends JpaRepository<ReservationTable, Integer> {
    List<ReservationTable> findByReservation(Reservations reservation);
}
