package com.laklu.pos.services;

import com.laklu.pos.entities.Menu;
import com.laklu.pos.exceptions.httpExceptions.NotFoundException;
import com.laklu.pos.repositories.MenuRepository;
import com.laklu.pos.validator.MenuNameMustBeUnique;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    public List<Menu> getAll() {
        return menuRepository.findAll();
    }

    public Optional<Menu> findById(Integer id) {
        return menuRepository.findById(id);
    }

    public Menu createMenu(Menu menu) {
        if (menu.getStatus() == null) {
            menu.setStatus(Menu.MenuStatus.ENABLE);
        }
        return menuRepository.save(menu);
    }

    public Menu updateMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    public Menu findOrFail(Integer id) {
        return findById(id).orElseThrow(NotFoundException::new);
    }

    public void deleteMenu(Menu menu) {
        menuRepository.delete(menu);
    }

    public Optional<Menu> findByName(String name) {
        return menuRepository.findByName(name);
    }
}