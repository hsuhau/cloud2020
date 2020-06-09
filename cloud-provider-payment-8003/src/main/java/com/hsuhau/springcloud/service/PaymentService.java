package com.hsuhau.springcloud.service;

import com.hsuhau.springcloud.entities.Payment;

/**
 * @author hsuhau
 * @date 2020/6/9 1:43
 */
public interface PaymentService {
    int create(Payment payment);

    Payment getPaymentById(Long id);
}
