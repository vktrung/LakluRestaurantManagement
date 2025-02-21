package com.laklu.pos.dataObjects.response;

import com.laklu.pos.entities.Tables;
import lombok.Data;

@Data
public class TableResponse {
    private String tableNumber;
    private int capacity;
    private String status;

    public TableResponse(Tables tables) {
        this.tableNumber = tables.getTableNumber();
        this.capacity = tables.getCapacity();
        this.status = tables.getStatus().toString();
    }
}
