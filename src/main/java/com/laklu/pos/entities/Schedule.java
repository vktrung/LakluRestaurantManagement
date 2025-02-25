package com.laklu.pos.entities;

import com.laklu.pos.controllers.ActivityLogListener;
import com.laklu.pos.enums.ShiftType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Entity
@EntityListeners(ActivityLogListener.class)
public class Schedule implements Identifiable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "staff_id", nullable = false)
    private User staff; // Nhân viên được gán lịch làm việc

    @Column(name = "shift_date", nullable = false)
    private LocalDate shiftDate; // Ngày làm việc

    @Column(name = "shift_start", nullable = false)
    private LocalTime shiftStart; // Giờ bắt đầu ca làm việc

    @Column(name = "shift_end", nullable = false)
    private LocalTime shiftEnd; // Giờ kết thúc ca làm việc

    @Enumerated(EnumType.STRING)
    @Column(name = "shift_type", nullable = false)
    private ShiftType shiftType; // Loại ca làm việc (SÁNG, CHIỀU, TỐI)

    @Column(name = "note", columnDefinition = "TEXT")
    private String note; // Ghi chú (nếu có)

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;  // Thời gian tạo lịch

    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // Thời gian cập nhật lịch

    @Override
    public Long getId() {
        return id;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

