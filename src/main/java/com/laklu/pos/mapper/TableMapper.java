package com.laklu.pos.mapper;

import com.laklu.pos.dataObjects.request.TableUpdateRequest;
import com.laklu.pos.entities.Tables;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface TableMapper {

    // Chỉ cập nhật các trường không null từ request vào entity
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTable(TableUpdateRequest request, @MappingTarget Tables table);
}
