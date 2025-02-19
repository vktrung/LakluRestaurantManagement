package com.laklu.pos.repositories;

import com.laklu.pos.entities.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {
    Optional<MenuItem> findById(Integer menuItemId);
}
