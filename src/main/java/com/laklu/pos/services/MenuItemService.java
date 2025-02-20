package com.laklu.pos.services;

import com.laklu.pos.dataObjects.request.MenuItemRequest;
import com.laklu.pos.entities.Category;
import com.laklu.pos.entities.MenuItem;
import com.laklu.pos.exceptions.NotFoundCategory;
import com.laklu.pos.exceptions.NotFoundMenuItem;
import com.laklu.pos.mapper.MenuItemMapper;
import com.laklu.pos.repositories.CategoryRepository;
import com.laklu.pos.repositories.MenuItemRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MenuItemService {

    private final MenuItemMapper menuItemMapper;
    private final MenuItemRepository menuItemRepository;
    private final CategoryRepository categoryRepository;

    // Lấy tất cả menu items
    @Transactional
    public List<MenuItem> getAllMenuItems() {
        List<MenuItem> menuItems = menuItemRepository.findAll();
        return menuItems;
    }

    // Lấy menu item theo ID
    public MenuItem getMenuItemById(Long id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new NotFoundMenuItem());
        return menuItem;
    }

    // Thêm mới menu item
    @Transactional
    public MenuItem createMenuItem(MenuItemRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new NotFoundCategory());

        MenuItem menuItem = menuItemMapper.toEntity(request);
        menuItem.setCategory(category);

        return menuItemRepository.save(menuItem);
    }

    // Cập nhật menu item
    @Transactional
    public MenuItem updateMenuItem(Long id, MenuItemRequest request) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new NotFoundMenuItem());

        if (request.getName() != null) menuItem.setName(request.getName());
        if (request.getDescription() != null) menuItem.setDescription(request.getDescription());
        if (request.getPrice() != null) menuItem.setPrice(request.getPrice());

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new NotFoundCategory());
            menuItem.setCategory(category);
        }
        menuItem.setUpdatedAt(new java.util.Date());

        return menuItemRepository.save(menuItem);
    }

    // Xóa mềm menu item
    @Transactional
    public void deleteMenuItem(Long id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new NotFoundMenuItem());

        menuItem.setIsDeleted(true);
        menuItemRepository.save(menuItem);
    }

    public MenuItem updateMenuItemPartially(Long id, Map<String, Object> updates) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new NotFoundMenuItem());

        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    menuItem.setName(value.toString());
                    break;
                case "description":
                    menuItem.setDescription(value.toString());
                    break;
                case "price":
                    menuItem.setPrice(Double.valueOf(value.toString()));
                    break;
                case "categoryId":
                    Long categoryId = Long.valueOf(value.toString());
                    Category category = categoryRepository.findById(categoryId)
                            .orElseThrow(() -> new NotFoundCategory());
                    menuItem.setCategory(category);
                    break;
                default:
                    throw new IllegalArgumentException("Trường '" + key + "' không hợp lệ để cập nhật.");
            }
        });

        menuItem.setUpdatedAt(new Date());
        return menuItemRepository.save(menuItem);
    }

    public Optional<MenuItem> findMenuItemById(Long id) {
        return menuItemRepository.findById(id);
    }
    public MenuItem findOrFail(Long id) {
        return this.findMenuItemById(id).orElseThrow(()
                -> new NotFoundCategory());
    }
}
