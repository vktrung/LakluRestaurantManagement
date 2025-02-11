package com.laklu.pos.repositories;

import com.laklu.pos.entities.Reservations;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservations, Integer> {
}
