package com.laklu.pos.dataObjects.response;


import com.laklu.pos.entities.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemResponse {

    private String name;

    private String description;

    private Double price;

    private Date createdAt = new Date();

    private Date updatedAt = new Date();

    private Boolean isDeleted = false;

    private Category category;
}
