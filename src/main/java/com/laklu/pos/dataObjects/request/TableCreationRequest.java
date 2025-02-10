package com.laklu.pos.dataObjects.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TableCreationRequest {
    String tableNumber;
    Integer capacity;
}
