package com.laklu.pos.dataObjects.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.laklu.pos.enums.MenuItemStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public class MenuItemDTO {
    private int id;

    @NotBlank(message = "Menu name cannot be empty")
    private String name;

    @NotBlank(message = "Menu description cannot be empty")
    private String description;

    @Min(value = 1, message = "Menu price must be greater than 0")
    private double price;

    @Min(value = 0, message = "Menu quantity cannot be negative")
    private int quantity;

    private MenuItemStatus status;

    @JsonProperty("category_name") // Rename field in JSON response
    private String category;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // Format date
    private LocalDateTime createdAt = LocalDateTime.now();

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Constructors
    public MenuItemDTO() {}

    public MenuItemDTO(int id, String name, String description, double price, int quantity, MenuItemStatus status, String category, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
        this.category = category;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public MenuItemStatus getStatus() {
        return status;
    }

    public void setStatus(MenuItemStatus status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
