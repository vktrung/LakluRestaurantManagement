package com.laklu.pos.mapper;

import com.laklu.pos.dataObjects.request.ReservationRequest;
import com.laklu.pos.dataObjects.request.UpdateReservationDTO;
import com.laklu.pos.dataObjects.request.UpdateReservationRequest;
import com.laklu.pos.entities.Reservation;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(target = "status", constant = "PENDING") // Đặt trạng thái mặc định là PENDING
    Reservation toEntity(ReservationRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateReservation(UpdateReservationDTO request, @MappingTarget Reservation reservation);

}