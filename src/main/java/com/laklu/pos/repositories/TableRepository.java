package com.laklu.pos.repositories;

import com.laklu.pos.entities.Tables;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TableRepository extends JpaRepository<Tables, Integer> {
    Optional<Tables> findById(Integer id);
    Optional<Tables> findByTableNumber(String tableNumber);
}
