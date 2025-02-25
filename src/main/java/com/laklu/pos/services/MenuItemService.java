package com.laklu.pos.services;

import com.laklu.pos.entities.Category;
import com.laklu.pos.entities.Dish;
import com.laklu.pos.entities.Menu;
import com.laklu.pos.entities.MenuItem;
import com.laklu.pos.exceptions.httpExceptions.NotFoundException;
import com.laklu.pos.repositories.MenuItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final DishService dishService;
    private final MenuService menuService;
    private final CategoryService categoryService;

    public List<MenuItem> getAll() {
        return menuItemRepository.findAll();
    }

    public Optional<MenuItem> findById(Integer id) {
        return menuItemRepository.findById(id);
    }

    public MenuItem createMenuItem(MenuItem menuItem) {
        validateReferences(menuItem);
        return menuItemRepository.save(menuItem);
    }

    public MenuItem updateMenuItem(MenuItem menuItem) {
        validateReferences(menuItem);
        return menuItemRepository.save(menuItem);
    }

    public void deleteMenuItem(MenuItem menuItem) {
        menuItemRepository.delete(menuItem);
    }

    private void validateReferences(MenuItem menuItem) {
        if (menuItem.getDish() != null && menuItem.getDish().getId() != null) {
            Dish dish = dishService.findOrFail(menuItem.getDish().getId());
            menuItem.setDish(dish);
        }
        if (menuItem.getMenu() != null && menuItem.getMenu().getId() != null) {
            Menu menu = menuService.findOrFail(menuItem.getMenu().getId());
            menuItem.setMenu(menu);
        }
        if (menuItem.getCategory() != null && menuItem.getCategory().getId() != null) {
            Category category = categoryService.findOrFail(menuItem.getCategory().getId());
            menuItem.setCategory(category);
        }
    }

    public MenuItem findOrFail(Integer id) {
        return findById(id)
                .orElseThrow(NotFoundException::new);
    }
}