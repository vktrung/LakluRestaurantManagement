package com.laklu.pos.services;

import com.laklu.pos.entities.Category;
import com.laklu.pos.entities.Dish;
import com.laklu.pos.exceptions.httpExceptions.NotFoundException;
import com.laklu.pos.repositories.DishRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DishService {

    private final DishRepository dishRepository;

    public List<Dish> getAll() {
        return dishRepository.findAll();
    }

    public Optional<Dish> findById(Integer id) {
        return dishRepository.findById(id);
    }

    public Dish createDish(Dish dish) {
        return dishRepository.save(dish);
    }

    public Dish updateDish(Dish dish) {
        return dishRepository.save(dish);
    }

    public Dish findOrFail(Integer id) {
        return findById(id)
                .orElseThrow(NotFoundException::new);
    }

    public void deleteDish(Dish dish) {
        dishRepository.delete(dish);
    }

    public Optional<Dish> findByName(String name) {
        return dishRepository.findByName(name);
    }
}