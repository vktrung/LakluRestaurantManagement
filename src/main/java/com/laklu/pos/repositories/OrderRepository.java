package com.laklu.pos.repositories;

import com.laklu.pos.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    boolean existsById(Integer id);
}
