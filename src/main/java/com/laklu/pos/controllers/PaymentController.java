package com.laklu.pos.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laklu.pos.auth.JwtGuard;
import com.laklu.pos.auth.policies.PaymentPolicy;
import com.laklu.pos.dataObjects.ApiResponseEntity;
import com.laklu.pos.dataObjects.request.PaymentRequest;
import com.laklu.pos.dataObjects.request.SepayWebhookRequest;
import com.laklu.pos.dataObjects.response.CashResponse;
import com.laklu.pos.dataObjects.response.PaymentResponse;
import com.laklu.pos.entities.Payment;
import com.laklu.pos.enums.PaymentMethod;
import com.laklu.pos.exceptions.httpExceptions.ForbiddenException;
import com.laklu.pos.repositories.OrderRepository;
import com.laklu.pos.repositories.PaymentRepository;
import com.laklu.pos.services.PaymentService;
import com.laklu.pos.uiltis.Ultis;
import com.laklu.pos.validator.OrderMustExist;
import com.laklu.pos.validator.PaymentMustExist;
import com.laklu.pos.validator.RuleValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    private final PaymentPolicy paymentPolicy;
    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @GetMapping("/{id}")
    public ApiResponseEntity getPaymentById(@PathVariable int id) throws Exception {
        Payment payment = paymentService.findOrFail(id);
        Ultis.throwUnless(paymentPolicy.canView(JwtGuard.userPrincipal(), payment), new ForbiddenException());
        PaymentResponse response = new PaymentResponse(
                payment.getOrders().getId(),
                payment.getAmountPaid(),
                payment.getReceivedAmount(),
                payment.getPaymentMethod(),
                payment.getPaymentStatus(),
                payment.getPaymentDate()
        );
        return ApiResponseEntity.success(response, "Lấy hóa đơn thành công");
    }

    @GetMapping("/getAll")
    public ApiResponseEntity getAll() throws Exception {
        Ultis.throwUnless(paymentPolicy.canList(JwtGuard.userPrincipal()), new ForbiddenException());
        List<PaymentResponse> responses = paymentService.getAll().stream()
                .map(payment -> new PaymentResponse(
                        payment.getOrders().getId(),
                        payment.getAmountPaid(),
                        payment.getReceivedAmount(),
                        payment.getPaymentMethod(),
                        payment.getPaymentStatus(),
                        payment.getPaymentDate()
                )).collect(Collectors.toList());
        return ApiResponseEntity.success(responses, "Lấy danh sách hóa đơn thanh toán");
    }

    @PostMapping("/create")
    public ApiResponseEntity createPayment(@Valid @RequestParam int orderId,
                                           @RequestParam PaymentMethod paymentMethod,
                                           @RequestParam(value = "Voucher-Code", required = false) String voucher) throws Exception {
        Ultis.throwUnless(paymentPolicy.canCreate(JwtGuard.userPrincipal()), new ForbiddenException());
        PaymentRequest request = new PaymentRequest(orderId, paymentMethod, voucher);
        Payment payment = paymentService.createPayment(request);
        PaymentResponse response = new PaymentResponse(
                payment.getOrders().getId(),
                payment.getAmountPaid(),
                payment.getReceivedAmount(),
                payment.getPaymentMethod(),
                payment.getPaymentStatus(),
                payment.getPaymentDate()
        );
        return ApiResponseEntity.success(response, "Tạo hóa đơn thanh toán thành công");
    }

    @PostMapping("/{id}/checkout/cash")
    public ApiResponseEntity processCashPayment(@PathVariable int id, @RequestParam BigDecimal receivedAmount) throws Exception {
        RuleValidator.validate(new PaymentMustExist(id, paymentRepository));
        Payment payment = paymentService.findOrFail(id);
//        Ultis.throwUnless(paymentPolicy.canEdit(JwtGuard.userPrincipal(), payment), new ForbiddenException());
        CashResponse response = paymentService.processCashPayment(id, receivedAmount);
        paymentService.processCashPayment(id, receivedAmount);
        return ApiResponseEntity.success(response, "Thanh toán tien mat thanh cong");
    }

    @GetMapping("/{id}/qr")
    public ApiResponseEntity generateQrCode(@PathVariable int id) throws Exception {
        Payment payment = paymentService.findOrFail(id);
        Ultis.throwUnless(paymentPolicy.canView(JwtGuard.userPrincipal(), payment), new ForbiddenException());

        String qrCodeUrl = paymentService.generateQrCode(id);
        return ApiResponseEntity.success(Map.of("qrCodeUrl", qrCodeUrl), "Tạo mã QR thành công");
    }

    @PostMapping("/webhook/sepay")
    public ApiResponseEntity processSepayWebhook(HttpServletRequest request) {
        try {
            // Đọc JSON gốc từ request
            String json = request.getReader().lines().collect(Collectors.joining());
            log.info("Raw JSON từ SePay: {}", json);

            // Parse JSON thành SepayWebhookRequest
            ObjectMapper objectMapper = new ObjectMapper();
            SepayWebhookRequest payload = objectMapper.readValue(json, SepayWebhookRequest.class);
            log.info("parse payload: {}", payload);

            // Lấy paymentCode từ webhook
            String paymentCode = payload.getCode();
            if (paymentCode == null || paymentCode.trim().isEmpty()) {
                log.error("Payment code bị thiếu!");
                return ApiResponseEntity.exception(HttpStatus.BAD_REQUEST, "Mã thanh toán bị thiếu");
            }

            BigDecimal amount = payload.getTransferAmount();
            if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
                log.error("Số tiền giao dịch không hợp lệ: {}", amount);
                return ApiResponseEntity.exception(HttpStatus.BAD_REQUEST, "Số tiền không hợp lệ");
            }

            log.info("Xử lý thanh toán cho paymentCode: {}, amount: {}", paymentCode, amount);
            paymentService.processPaymentWebhook("SUCCESS", paymentCode, amount);

            return ApiResponseEntity.success(Map.of(
                    "paymentCode", paymentCode,
                    "status", "PAID",
                    "amount", amount
            ), "Cập nhật trạng thái thanh toán thành công");
        } catch (Exception e) {
            log.error("Lỗi xử lý JSON: ", e);
            return ApiResponseEntity.exception(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "Lỗi xử lý webhook");
        }
    }
}
