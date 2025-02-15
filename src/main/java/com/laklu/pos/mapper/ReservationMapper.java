package com.laklu.pos.mapper;

import com.laklu.pos.dataObjects.request.ReservationRequest;
import com.laklu.pos.dataObjects.request.UpdateReservationRequest;
import com.laklu.pos.entities.Reservations;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(target = "status", constant = "PENDING") // Đặt trạng thái mặc định là PENDING
    Reservations toEntity(ReservationRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateReservation(UpdateReservationRequest request, @MappingTarget Reservations reservation);

}
