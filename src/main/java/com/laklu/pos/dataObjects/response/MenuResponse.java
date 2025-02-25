package com.laklu.pos.dataObjects.response;

import com.laklu.pos.entities.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MenuResponse {
    private Integer id;
    private String name;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private Menu.MenuStatus status;

    public static MenuResponse fromEntity(Menu menu) {
        return new MenuResponse(menu.getId(), menu.getName(), menu.getStartAt(), menu.getEndAt(), menu.getStatus());
    }
}
