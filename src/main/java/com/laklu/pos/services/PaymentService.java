package com.laklu.pos.services;

import com.laklu.pos.enums.PaymentMethod;
import com.laklu.pos.enums.PaymentStatus;
import com.laklu.pos.repositories.*;
import com.laklu.pos.entities.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private static final String SEPAY_QR_URL = "https://qr.sepay.vn/img";
    private static final BigDecimal FIXED_AMOUNT = BigDecimal.valueOf(10000);

    public Payments getPaymentById(int paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    public List<Payments> getAll() {
        return paymentRepository.findAll();
    }

    public Payments createPayment(int orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Payments payment = new Payments();
        payment.setOrders(order);
        payment.setAmountPaid(FIXED_AMOUNT);
        payment.setPaymentMethod(PaymentMethod.TRANSFER);
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    public String generateQrCode(int paymentId) {
        Payments payment = getPaymentById(paymentId);
        String bank = "MBBank";
        String account = "0587775888";
        String description = "Thanh toan don hang " + payment.getId();
        return SEPAY_QR_URL + "?bank=" + bank
                + "&acc=" + account
                + "&amount=" + FIXED_AMOUNT
                + "&des=" + description
                + "&paymentId=" + payment.getId();
    }

    @Transactional
    public void processPaymentWebhook(String paymentStatus, int paymentId, BigDecimal amount) {
        Payments payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (amount.compareTo(payment.getAmountPaid()) != 0) {
            log.error("Số tiền nhận được ({}) không khớp với yêu cầu ({})!", amount, payment.getAmountPaid());
            throw new IllegalArgumentException("Số tiền thanh toán không khớp");
        }

        if ("SUCCESS".equals(paymentStatus)) {
            payment.setPaymentStatus(PaymentStatus.PAID);
            updateOrderStatus(payment.getOrders());
        } else if ("FAILED".equals(paymentStatus)) {
            payment.setPaymentStatus(PaymentStatus.FAILED);
        } else {
            payment.setPaymentStatus(PaymentStatus.PENDING);
        }

        payment.setUpdatedAt(LocalDateTime.now());
        paymentRepository.save(payment);
        log.info("PaymentId {} đã cập nhật thành {}", paymentId, payment.getPaymentStatus());
    }

    private void updateOrderStatus(Orders orders) {
        orders.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(orders);
    }

    @Transactional
    public void deletePayment(Payments payment) {
        paymentRepository.delete(payment);
    }
}
