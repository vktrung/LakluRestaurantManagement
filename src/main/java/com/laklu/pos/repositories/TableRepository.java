package com.laklu.pos.repositories;

import com.laklu.pos.entities.Reservation;
import com.laklu.pos.entities.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TableRepository extends JpaRepository<Table, Integer> {
    Optional<Table> findById(Integer id);
    Optional<Table> findByTableNumber(String tableNumber);

    @Query("SELECT t FROM Table t WHERE t.id NOT IN :tableIds AND t.id NOT IN " +
            "(SELECT rt.table.id FROM ReservationTable rt WHERE rt.reservation = :reservation)")
    List<Table> findAllExceptInReservation(List<Integer> tableIds, Reservation reservation);
}
