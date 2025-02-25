package com.laklu.pos.dataObjects.request;

import com.laklu.pos.entities.Menu;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewMenu {
    private String name;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private Menu.MenuStatus status;
}
