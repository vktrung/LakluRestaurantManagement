package com.laklu.pos.entities;
import com.laklu.pos.enums.MenuItemStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(nullable = false, length = 255)
    @NotBlank(message = "Menu name cannot be empty")
    String name;

    @Column(columnDefinition = "TEXT")
    @NotBlank(message = "Menu description cannot be empty")
    String description;

    @Column(nullable = false)
    @Min(value = 1, message = "Menu price must be greater than 0")
    double price;

    @Column(nullable = false)
    @Min(value = 0, message = "Menu quantity cannot be negative")
    int quantity;

    @NotNull(message = "Menu status cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    MenuItemStatus status;

    @ManyToOne
    Categories category;

    LocalDateTime createdAt = LocalDateTime.now();

    LocalDateTime updatedAt = LocalDateTime.now();

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Categories getCategory() {
        return category;
    }

    public void setCategory(Categories category) {
        this.category = category;
    }

    public MenuItemStatus getStatus() {
        return status;
    }

    public void setStatus(MenuItemStatus status) {
        this.status = status;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
