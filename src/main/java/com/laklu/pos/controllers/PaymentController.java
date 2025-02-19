package com.laklu.pos.controllers;

import com.laklu.pos.services.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/v1/")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

}
