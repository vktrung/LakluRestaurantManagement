package com.laklu.pos.repositories;

import com.laklu.pos.entities.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DishRepository extends JpaRepository<Dish, Integer> {

    Optional<Dish> findByName(String name); // Tìm món ăn theo tên
}
