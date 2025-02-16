package com.laklu.pos.dataObjects.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse
{
    private String name;
    private String description;
    private Date createdAt = new Date();
    private Date updatedAt = new Date();
    private Boolean isDeleted = false;
}
