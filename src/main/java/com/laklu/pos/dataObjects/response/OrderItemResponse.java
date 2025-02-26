package com.laklu.pos.dataObjects.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderItemResponse {
    private Long menuItemId;
    private int quantity;
    private String status;
    private Boolean isDeleted;
}
