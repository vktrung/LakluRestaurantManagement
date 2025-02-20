package com.laklu.pos.dataObjects.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse
{
    private String name;
    private String description;
    private Date createdAt;
    private Date updatedAt;
    private Boolean isDeleted;

    private List<MenuItemResponse> menuItems;
}
