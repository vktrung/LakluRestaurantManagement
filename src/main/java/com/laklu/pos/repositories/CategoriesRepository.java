package com.laklu.pos.repositories;

import com.laklu.pos.entities.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriesRepository  extends JpaRepository<Categories, Integer> {
}
