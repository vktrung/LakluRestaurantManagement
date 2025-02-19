package com.laklu.pos.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.laklu.pos.auth.JwtGuard;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "customer_name", nullable = false)
    String customerName;

    @Column(name = "customer_phone", nullable = false)
    String customerPhone;

    @Column(name = "reservation_time", nullable = false)
    LocalDateTime reservationTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    Status status;

    @Column(name = "check_in")
    LocalDateTime checkIn;

    @Column(name = "check_out")
    LocalDateTime checkOut;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Column(name = "user_id")
    Integer userId;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    Set<ReservationTable> reservationTables;

    @PrePersist
    protected void onCreate() {
        this.updatedAt = LocalDateTime.now();
        this.userId = JwtGuard.userPrincipal().getPersitentUser().getId();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public enum Status {
        PENDING, CONFIRMED, CANCELLED, COMPLETED
    }
}