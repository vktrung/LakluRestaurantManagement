package com.laklu.pos.dataObjects.request;

import com.laklu.pos.entities.Tables;
import com.laklu.pos.enums.Status_Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TableUpdateRequest {
    String tableNumber;
    Integer capacity;
    String status;
}