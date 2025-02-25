package com.laklu.pos.entities;

import com.laklu.pos.controllers.ActivityLogListener;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@jakarta.persistence.Table(name = "menu_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(ActivityLogListener.class)
public class MenuItem implements Identifiable<Integer> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Thêm khóa chính để quản lý bản ghi

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id", nullable = false)
    private Dish dish;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private BigDecimal price; // Sử dụng BigDecimal theo yêu cầu DECIMAL

    @Column(nullable = false)
    private Integer discount; // Sử dụng Integer theo yêu cầu INT cho giảm giá

    @Column(nullable = false)
    private String status; // Sử dụng String vì anh chưa đề cập Enum cho status trong menuItem

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Override
    public Integer getId() {
        return id;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}