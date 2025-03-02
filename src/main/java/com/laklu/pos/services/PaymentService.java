package com.laklu.pos.services;

import com.laklu.pos.dataObjects.request.PaymentRequest;
import com.laklu.pos.dataObjects.response.CashResponse;
import com.laklu.pos.enums.*;
import com.laklu.pos.exceptions.httpExceptions.NotFoundException;
import com.laklu.pos.repositories.*;
import com.laklu.pos.entities.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

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
    private final VoucherRepository voucherRepository;
    private static final String SEPAY_QR_URL = "https://qr.sepay.vn/img";
    private static final String PREFIX = "LL";
    private static final Random RANDOM = new Random();
    private static final BigDecimal FIXED_AMOUNT = BigDecimal.valueOf(100000);

    public static String generatePaymentCode() {
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

    public Payment createPayment(PaymentRequest request) {
        Orders order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new NotFoundException());

        BigDecimal orderAmount = FIXED_AMOUNT;
        BigDecimal discount = BigDecimal.ZERO;

        if (request.getVoucherCode() != null && !request.getVoucherCode().isEmpty()) {
            Optional<Voucher> voucherOpt = voucherRepository.findByCode(request.getVoucherCode());
            if (voucherOpt.isEmpty()) {
                throw new NotFoundException();
            }

            Voucher voucher = voucherOpt.get();
            if (voucher.getStatus() == VoucherStatus.INACTIVE) {
                throw new IllegalArgumentException("Voucher không còn hiệu lực");
            }

            discount = applyVoucherDiscount(orderAmount, voucher);
            orderAmount = orderAmount.subtract(discount);

            voucher.setStatus(VoucherStatus.USED);
            voucherRepository.save(voucher);
        }

        Payment payment = new Payment();
        payment.setOrders(order);
        payment.setAmountPaid(orderAmount);
        payment.setReceivedAmount(null);
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());
        payment.setCode(generatePaymentCode());

        return paymentRepository.save(payment);
    }

    @Transactional
    public CashResponse processCashPayment(int paymentId, BigDecimal receivedAmount) {
        Payment payment = findOrFail(paymentId);

        if (payment.getPaymentMethod() != PaymentMethod.CASH) {
            throw new IllegalArgumentException("Chỉ áp dụng cho thanh toán tiền mặt");
        }

        BigDecimal orderAmount = payment.getAmountPaid();
        if (receivedAmount.compareTo(orderAmount) < 0) {
            throw new IllegalArgumentException("Số tiền nhận được không đủ để thanh toán");
        }

        BigDecimal change = receivedAmount.subtract(orderAmount);

        payment.setReceivedAmount(receivedAmount);
        payment.setUpdatedAt(LocalDateTime.now());
        payment.setPaymentStatus(PaymentStatus.PAID);
        paymentRepository.save(payment);
        updateOrderStatus(payment.getOrders());
        return new CashResponse(
                payment.getOrders().getId(),
                payment.getAmountPaid(),
                payment.getReceivedAmount(),
                payment.getPaymentMethod(),
                payment.getPaymentStatus(),
                payment.getPaymentDate(),
                change
        );
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

    private BigDecimal applyVoucherDiscount(BigDecimal totalAmount, Voucher voucher) {
        if (voucher.getDiscountType() == DiscountType.PERCENTAGE) {
            return totalAmount.multiply(voucher.getDiscountValue()).divide(BigDecimal.valueOf(100));
        } else {
            return voucher.getDiscountValue();
        }
    }
}
