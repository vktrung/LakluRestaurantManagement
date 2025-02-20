package com.laklu.pos.mapper;

import com.laklu.pos.dataObjects.request.MenuItemRequest;
import com.laklu.pos.dataObjects.response.MenuItemResponse;
import com.laklu.pos.entities.MenuItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MenuItemMapper {
    @Mapping(target = "category", ignore = true)
    MenuItem toEntity(MenuItemRequest menuItemRequest);

    @Mapping(target = "category.menuItems", ignore = true)
    MenuItemResponse toResponse(MenuItem menuItem);

    List<MenuItemResponse> toResponseList(List<MenuItem> menuItems);
}
