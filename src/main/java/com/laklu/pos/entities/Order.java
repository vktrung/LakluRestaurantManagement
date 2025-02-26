package com.laklu.pos.entities;

import com.laklu.pos.enums.OrderStatus;
import com.laklu.pos.validator.ValidEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.Table;


@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    @NotNull(message = "Yêu cầu đặt bàn")
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    @NotNull(message = "Yêu cầu nhân viên")
    private User staff;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @ValidEnum(enumClass = OrderStatus.class, message = "Trạng thái đơn hàng không hợp lệ!")
    @Column(name = "status", nullable = false)
    private String status;
}
