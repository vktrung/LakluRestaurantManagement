package com.laklu.pos.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String code;
    private String discountType;
    private BigDecimal discountValue;
    private LocalDateTime validFrom;
    private LocalDateTime validUntil;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
