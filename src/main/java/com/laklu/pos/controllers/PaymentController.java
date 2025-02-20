package com.laklu.pos.controllers;

import com.laklu.pos.auth.JwtGuard;
import com.laklu.pos.auth.policies.PaymentPolicy;
import com.laklu.pos.dataObjects.ApiResponseEntity;
import com.laklu.pos.entities.Payment;
import com.laklu.pos.exceptions.httpExceptions.ForbiddenException;
import com.laklu.pos.services.PaymentService;
import com.laklu.pos.uiltis.Ultis;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final PaymentPolicy paymentPolicy;

    @GetMapping("payments")
    public ApiResponseEntity getAllPayments() throws Exception {
        Ultis.throwUnless(paymentPolicy.canList(JwtGuard.userPrincipal()), new ForbiddenException());

        List<Payment> payments = paymentService.getAllPayments();

        return ApiResponseEntity.success(payments);
    }

    @GetMapping("payments/{id}")
    public ApiResponseEntity getPaymentById(@PathVariable int id) throws Exception {
        Payment payment = paymentService.getPaymentById(id);
        return ApiResponseEntity.success(payment);
    }

    @PostMapping("payments/create/{orderId}")
    public ApiResponseEntity createPayment(@PathVariable int orderId) throws Exception {
        Ultis.throwUnless(paymentPolicy.canCreate(JwtGuard.userPrincipal()), new ForbiddenException());

        Payment payment = paymentService.createPayment(orderId);

        return ApiResponseEntity.success(payment);
    }

    @GetMapping("payments/qrCode/{paymentId}")
    public ApiResponseEntity getQrCode(@PathVariable int paymentId) throws Exception {
        Payment payment = paymentService.getPaymentById(paymentId);
        Ultis.throwUnless(paymentPolicy.canView(JwtGuard.userPrincipal(), payment), new ForbiddenException());

        String qrCodeUrl = paymentService.generateQrCode(paymentId);

        return ApiResponseEntity.success(qrCodeUrl);
    }

    @PostMapping("payments/webhook")
    public ApiResponseEntity processPaymentWebhook(@RequestParam String paymentStatus,
                                                   @RequestParam int paymentId,
                                                   @RequestParam BigDecimal amount) throws Exception {
        Payment payment = paymentService.getPaymentById(paymentId);
        Ultis.throwUnless(paymentPolicy.canEdit(JwtGuard.userPrincipal(), payment), new ForbiddenException());

        paymentService.processPaymentWebhook(paymentStatus, paymentId, amount);

        return ApiResponseEntity.success(null);
    }

}
