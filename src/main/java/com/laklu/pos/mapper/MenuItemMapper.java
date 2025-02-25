package com.laklu.pos.mapper;

import com.laklu.pos.dataObjects.request.NewMenuItem;
import com.laklu.pos.entities.MenuItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MenuItemMapper {
    @Mapping(source = "categoryId", target = "category.id")
    @Mapping(source = "dishId", target = "dish.id")
    @Mapping(source = "menuId", target = "menu.id")
    MenuItem toEntity(NewMenuItem dto);

    @Mapping(source = "categoryId", target = "category.id")
    @Mapping(source = "dishId", target = "dish.id")
    @Mapping(source = "menuId", target = "menu.id")
    void updateMenuItemFromDto(NewMenuItem dto, @MappingTarget MenuItem entity);
}
