package com.laklu.pos.repositories;

import com.laklu.pos.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Optional<Payment> findByCode(String code);
    boolean existsById(Integer id);
}
