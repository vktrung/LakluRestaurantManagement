package com.laklu.pos.repositories;

import com.laklu.pos.entities.Reservations;
import com.laklu.pos.entities.Tables;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservations, Integer> {
    Optional<Reservations> findById(Integer id);
}
