package com.laklu.pos.services;

import com.laklu.pos.enums.PaymentMethod;
import com.laklu.pos.enums.PaymentStatus;
import com.laklu.pos.exceptions.httpExceptions.NotFoundException;
import com.laklu.pos.repositories.*;
import com.laklu.pos.entities.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private static final String SEPAY_QR_URL = "https://qr.sepay.vn/img";
    private static final String PREFIX = "LL";
    private static final Random RANDOM = new Random();
    private static final BigDecimal FIXED_AMOUNT = BigDecimal.valueOf(10000);

    public static String generatePaymentCode(){
        int number = RANDOM.nextInt(999999);
        return PREFIX + String.format("%06d", number);
    }

    public Payment findOrFail(Integer id) {
        return this.getPaymentById(id).orElseThrow(NotFoundException::new);
    }

    public Optional<Payment> getPaymentById(int paymentId) {
        return paymentRepository.findById(paymentId);
    }

    public List<Payment> getAll() {
        return paymentRepository.findAll();
    }

    public Payment createPayment(int orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException());

        Payment payment = new Payment();
        payment.setOrders(order);
        payment.setAmountPaid(FIXED_AMOUNT);
        payment.setPaymentMethod(PaymentMethod.TRANSFER);
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());
        payment.setCode(generatePaymentCode());

        return paymentRepository.save(payment);
    }

    public String generateQrCode(int paymentId) {
        Payment payment = findOrFail(paymentId);
        String bank = "MBBank";
        String account = "0587775888";
        String description = payment.getCode();
        return SEPAY_QR_URL + "?bank=" + bank
                + "&acc=" + account
                + "&amount=" + FIXED_AMOUNT
                + "&des=" + description
                + "&paymentId=" + payment.getId();
    }

    @Transactional
    public void processPaymentWebhook(String paymentStatus, String paymentCode, BigDecimal amount) {
        Payment payment = paymentRepository.findByCode(paymentCode)
                .orElseThrow(() -> new NotFoundException());

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
        log.info("PaymentId {} đã cập nhật thành {}", paymentCode, payment.getPaymentStatus());
    }

    private void updateOrderStatus(Orders orders) {
        orders.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(orders);
    }

    @Transactional
    public void deletePayment(Payment payment) {
        paymentRepository.delete(payment);
    }
}
