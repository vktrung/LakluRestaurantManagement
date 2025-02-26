package com.laklu.pos.mapper;

import com.laklu.pos.dataObjects.request.OrderRequest;
import com.laklu.pos.dataObjects.response.OrderResponse;
import com.laklu.pos.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface OrderMapper {
    @Mapping(source = "reservation.id", target = "reservationId")
    @Mapping(source = "staff.id", target = "staffId")
    OrderResponse orderToResponse(Order order);

    Order requestToOrder(OrderRequest request);
}
