package com.laklu.pos.services;

import com.laklu.pos.repositories.PaymentRepository;

public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    
}
