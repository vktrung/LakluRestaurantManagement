package com.laklu.pos.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laklu.pos.auth.JwtGuard;
import com.laklu.pos.auth.policies.PaymentPolicy;
import com.laklu.pos.dataObjects.ApiResponseEntity;
import com.laklu.pos.dataObjects.request.SepayWebhookRequest;
import com.laklu.pos.entities.Payments;
import com.laklu.pos.exceptions.httpExceptions.ForbiddenException;
import com.laklu.pos.services.PaymentService;
import com.laklu.pos.uiltis.Ultis;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    private final PaymentPolicy paymentPolicy;
    private final PaymentService paymentService;

    @GetMapping("/{id}")
    public ApiResponseEntity getPaymentById(@PathVariable int id) throws Exception {
        Payments payment = paymentService.getPaymentById(id);
//        Ultis.throwUnless(paymentPolicy.canView(JwtGuard.userPrincipal(), payment), new ForbiddenException());

        return ApiResponseEntity.success(payment, "Lấy hóa đơn thành công");
    }

    @GetMapping("/getAll")
    public ApiResponseEntity getAll() throws Exception {
//        Ultis.throwUnless(paymentPolicy.canList(JwtGuard.userPrincipal()), new ForbiddenException());

        return ApiResponseEntity.success(paymentService.getAll(), "Lấy danh sách hóa đơn thanh toán");
    }

    @PostMapping("/create")
    public ApiResponseEntity createPayment(@Valid @RequestParam int orderId) throws Exception {
//        Ultis.throwUnless(paymentPolicy.canCreate(JwtGuard.userPrincipal()), new ForbiddenException());

        Payments payment = paymentService.createPayment(orderId);
        return ApiResponseEntity.success(payment, "Tạo hóa đơn thanh toán thành công");
    }

    @GetMapping("/{id}/qr")
    public ApiResponseEntity generateQrCode(@PathVariable int id) throws Exception {
        Payments payment = paymentService.getPaymentById(id);
//        Ultis.throwUnless(paymentPolicy.canView(JwtGuard.userPrincipal(), payment), new ForbiddenException());

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
            log.info("Đã parse payload: {}", payload);

            // Kiểm tra description
            String description = payload.getDescription();
            if (description == null || description.trim().isEmpty()) {
                log.error("Description bị thiếu!");
                return ApiResponseEntity.exception(HttpStatus.BAD_REQUEST, "Dữ liệu mô tả (description) bị thiếu");
            }

            Integer paymentId = extractPaymentIdFromDescription(payload.getDescription());
            if (paymentId == null) {
                log.error("Không tìm thấy paymentId từ description: {}", description);
                return ApiResponseEntity.exception(HttpStatus.BAD_REQUEST, "Không tìm thấy paymentId trong description");
            }

            BigDecimal amount = payload.getTransferAmount();
            if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
                log.error("Số tiền giao dịch không hợp lệ: {}", amount);
                return ApiResponseEntity.exception(HttpStatus.BAD_REQUEST, "Số tiền không hợp lệ");
            }

            log.info("Xử lý thanh toán cho paymentId: {}, amount: {}", paymentId, amount);
            paymentService.processPaymentWebhook("SUCCESS", paymentId, amount);

            return ApiResponseEntity.success(Map.of(
                    "paymentId", paymentId,
                    "status", "PAID",
                    "amount", amount
            ), "Cập nhật trạng thái thanh toán thành công");
        } catch (Exception e) {
            log.error("Lỗi xử lý JSON: ", e);
            return ApiResponseEntity.exception(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), "Lỗi xử lý webhook");
        }
    }

    private Integer extractPaymentIdFromDescription(String description) {
        log.info("Phân tích description: {}", description);

        Pattern pattern = Pattern.compile("don hang (\\d+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(description);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }

        log.error("Không tìm thấy paymentId trong description: {}", description);
        return null;
    }

    @DeleteMapping("/{id}")
    public ApiResponseEntity deletePayment(@PathVariable int id) throws Exception {
        Payments payment = paymentService.getPaymentById(id);
//        Ultis.throwUnless(paymentPolicy.canDelete(JwtGuard.userPrincipal(), payment), new ForbiddenException());

        paymentService.deletePayment(payment);
        return ApiResponseEntity.success("Xóa hóa đơn thanh toán thành công");
    }
}
