package com.laklu.pos.services;

import com.laklu.pos.dataObjects.dtos.MenuItemDTO;
import com.laklu.pos.entities.Categories;
import com.laklu.pos.entities.MenuItem;
import com.laklu.pos.enums.MenuItemStatus;
import com.laklu.pos.repositories.CategoriesRepository;
import com.laklu.pos.repositories.MenuItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MenuItemService {
    private final MenuItemRepository menuRepository;
    private final CategoriesRepository categoriesRepository;

    public MenuItemService(MenuItemRepository menuRepository, CategoriesRepository categoriesRepository) {
        this.menuRepository = menuRepository;
        this.categoriesRepository = categoriesRepository;
    }

    public MenuItemDTO addMenu(MenuItemDTO menuItemDTO) {
        MenuItem menuItem = toEntity(menuItemDTO);
        MenuItem savedMenuItem = menuRepository.save(menuItem);
        return toDTO(savedMenuItem);
    }

    public List<MenuItemDTO> getAllMenus() {
        return menuRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public MenuItemDTO updateMenu(int id, MenuItemDTO updatedDTO) {
        if (id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID must be greater than 0");
        }

        return menuRepository.findById(id).map(menu -> {
            menu.setName(updatedDTO.getName());
            menu.setDescription(updatedDTO.getDescription());
            menu.setPrice(updatedDTO.getPrice());
            menu.setQuantity(updatedDTO.getQuantity());
            menu.setStatus(updatedDTO.getStatus());

            if (!menu.getCategory().getName().equals(updatedDTO.getCategory())) {
                Categories category = categoriesRepository.findByName(updatedDTO.getCategory())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
                menu.setCategory(category);
            }

            menu.setUpdatedAt(updatedDTO.getUpdatedAt());

            MenuItem savedMenu = menuRepository.save(menu);
            return toDTO(savedMenu);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu not found with ID: " + id));
    }

    public void deleteMenu(int id) {
        if (id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID must be greater than 0");
        }

        if (!menuRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu not found with ID: " + id);
        }

        menuRepository.deleteById(id);
    }

    private MenuItem toEntity(MenuItemDTO dto) {
        Categories category = categoriesRepository.findByName(dto.getCategory())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found: " + dto.getCategory()));

        MenuItem menuItem = new MenuItem();
        menuItem.setId(dto.getId());
        menuItem.setName(dto.getName());
        menuItem.setDescription(dto.getDescription());
        menuItem.setPrice(dto.getPrice());
        menuItem.setQuantity(dto.getQuantity());
        menuItem.setStatus(dto.getStatus() != null ? dto.getStatus() : MenuItemStatus.AVAILABLE); // Mặc định AVAILABLE nếu null
        menuItem.setCategory(category);
        menuItem.setCreatedAt(dto.getCreatedAt());
        menuItem.setUpdatedAt(dto.getUpdatedAt());

        return menuItem;
    }

    private MenuItemDTO toDTO(MenuItem menuItem) {
        return new MenuItemDTO(
                menuItem.getId(),
                menuItem.getName(),
                menuItem.getDescription(),
                menuItem.getPrice(),
                menuItem.getQuantity(),
                menuItem.getStatus(),
                menuItem.getCategory().getName(), // Lấy tên category
                menuItem.getCreatedAt(),
                menuItem.getUpdatedAt()
        );
    }

}
