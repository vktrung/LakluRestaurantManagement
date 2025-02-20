package com.laklu.pos.repositories;

import com.laklu.pos.entities.SalaryRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SalaryRateRepository extends JpaRepository<SalaryRate, Integer> {
    Optional<SalaryRate> findById(Integer id);
}
