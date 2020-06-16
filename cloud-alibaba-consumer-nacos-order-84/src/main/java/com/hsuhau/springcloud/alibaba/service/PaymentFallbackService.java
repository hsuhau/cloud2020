package com.hsuhau.springcloud.alibaba.service;

import com.hsuhau.springcloud.entities.CommonResult;
import com.hsuhau.springcloud.entities.Payment;
import org.springframework.stereotype.Service;

/**
 * @author hsuhau
 * @date 2020/6/16 9:36
 */
@Service
public class PaymentFallbackService implements PaymentService{
    @Override
    public CommonResult<Payment> paymentSQL(Long id) {
        return new CommonResult<>(444, "PaymentFallbackService");
    }
}
