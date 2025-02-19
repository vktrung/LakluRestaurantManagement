package com.laklu.pos.repositories;

import com.laklu.pos.entities.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TableRepository extends JpaRepository<Table, Integer> {
    Optional<Table> findById(Integer id);
    Optional<Table> findByTableNumber(String tableNumber);
}