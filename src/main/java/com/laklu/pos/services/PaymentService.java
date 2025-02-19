package com.laklu.pos.services;

import com.laklu.pos.entities.*;
import com.laklu.pos.enums.PaymentStatus;
import com.laklu.pos.exceptions.httpExceptions.NotFoundException;
import com.laklu.pos.repositories.*;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PaymentService {
    PaymentRepository paymentRepository;
    OrderRepository orderRepository;
    OrderItemRepository orderItemRepository;
    VoucherRepository voucherRepository;
    MenuItemRepository menuItemRepository;
    ReservationRepository reservationRepository;

    public Payment createPayment(int orderId) {
        //get order
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isEmpty()) {
            throw new NotFoundException();
        }

        Order order = optionalOrder.get();

        //Calculate total price for  OrderItems
        BigDecimal totalAmount = calculateTotalAmount(order);

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmountPaid(totalAmount);
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(java.time.LocalDateTime.now());
        payment.setUpdatedAt(java.time.LocalDateTime.now());
        paymentRepository.save(payment);
        return payment;
    }

    private BigDecimal calculateTotalAmount(Order order) {
        BigDecimal totalAmount = BigDecimal.ZERO;

        //Get list of orderItem in order
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());
        for (OrderItem item : orderItems) {
            Optional<MenuItem> optionalMenuItem = menuItemRepository.findById(item.getMenuItemId());
            if (optionalMenuItem.isPresent()) {
                MenuItem menuItem = optionalMenuItem.get();
                BigDecimal itemPrice = menuItem.getPrice();

                //Calculate total price for each order item(price*quantity)
                totalAmount = totalAmount.add(itemPrice.multiply(BigDecimal.valueOf(item.getQuantity())));
            } else {
                log.warn("MenuItem with ID {} not found for OrderItem ID {}", item.getMenuItemId(), item.getId());
            }
        }
        return totalAmount;
    }

    public String generateQrCode(int paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(NotFoundException::new);
        BigDecimal amount = payment.getAmountPaid();
        String bank = "MBBank";
        String account = "0587775888";
        String description = "Thanh toan hoa don #" + payment.getId();

        String qrCodeUrl = "https://qr.sepay.vn/img?bank=" + bank
                + "&acc=" + account
                + "&amount=" + amount
                + "&des=" + description;
        return qrCodeUrl;
    }

    @Transactional
    public void processPaymentWebhook(String paymentStatus, int paymentId) {
        Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);
        if (!optionalPayment.isPresent()) {
            throw new NotFoundException();
        }

        Payment payment = optionalPayment.get();
        switch (paymentStatus) {
            case "SUCCESS":
                payment.setPaymentStatus(PaymentStatus.SUCCESS);
                break;
            case "FAILED":
                payment.setPaymentStatus(PaymentStatus.FAILED);
                break;
            default:
                payment.setPaymentStatus(PaymentStatus.PENDING);
                break;
        }

        payment.setUpdatedAt(java.time.LocalDateTime.now());
        paymentRepository.save(payment);
        if ("SUCCESS".equals(paymentStatus)) {
            updateOrdeStatus(payment.getOrder());
        }
    }

    private void updateOrdeStatus(Order order) {
        order.setUpdatedAt(java.time.LocalDateTime.now());
        Optional<Reservation> optionalReservation = reservationRepository.findById(order.getReservationId());

        Reservation reservation = reservationRepository.findById(order.getReservationId())
                .orElseThrow(NotFoundException::new);
        reservation.setCheckOut(java.time.LocalDateTime.now());
        reservation.setUpdatedAt(java.time.LocalDateTime.now());
        reservation.setStatus(Reservation.Status.COMPLETED);

        reservationRepository.save(reservation);
        orderRepository.save(order);
    }
}
