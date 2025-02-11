package com.laklu.pos.services;

import com.laklu.pos.entities.Menu;
import com.laklu.pos.repositories.MenuRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class MenuService {
    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public Menu addMenu(Menu menu) {
        validateMenu(menu);
        return menuRepository.save(menu);
    }

    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    public Menu updateMenu(int id, Menu updatedMenu) {
        validateMenu(updatedMenu);

        return menuRepository.findById(id).map(menu -> {
            menu.setName(updatedMenu.getName());
            menu.setDescription(updatedMenu.getDescription());
            menu.setPrice(updatedMenu.getPrice());
            menu.setQuantity(updatedMenu.getQuantity());
            menu.setStatus(updatedMenu.getStatus());
            menu.setCategory(updatedMenu.getCategory());
            menu.setUpdatedAt(updatedMenu.getUpdatedAt());
            return menuRepository.save(menu);
        }).orElseThrow(() -> {
            return new IllegalArgumentException("Menu not found with ID: " + id);
        });
    }

    public void deleteMenu(int id) {
        Optional<Menu> menu = menuRepository.findById(id);
        if (menu.isEmpty()) {
            throw new IllegalArgumentException("Menu not found with ID: " + id);
        }
        menuRepository.deleteById(id);
    }

    private void validateMenu(Menu menu) {
        if (menu == null) {
            throw new IllegalArgumentException("Menu object cannot be null");
        }
        if (!StringUtils.hasText(menu.getName())) {
            throw new IllegalArgumentException("Menu name cannot be empty");
        }
        if (!StringUtils.hasText(menu.getDescription())) {
            throw new IllegalArgumentException("Menu description cannot be empty");
        }
        if (menu.getPrice() <= 0) {
            throw new IllegalArgumentException("Menu price must be greater than 0");
        }
        if (menu.getQuantity() < 0) {
            throw new IllegalArgumentException("Menu quantity cannot be negative");
        }
        if (menu.getStatus() == null) {
            throw new IllegalArgumentException("Menu status cannot be null");
        }
        if (menu.getCategory() == null) {
            throw new IllegalArgumentException("Menu category cannot be null");
        }
    }
}