package com.laklu.pos.mapper;

import com.laklu.pos.dataObjects.request.NewMenuItem;
import com.laklu.pos.entities.MenuItem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface MenuItemMapper {
    MenuItem toEntity(NewMenuItem dto);

    void updateMenuItemFromDto(NewMenuItem dto, @MappingTarget MenuItem entity);
}
