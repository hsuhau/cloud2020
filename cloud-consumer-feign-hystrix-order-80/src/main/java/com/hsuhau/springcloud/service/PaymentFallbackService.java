package com.hsuhau.springcloud.service;

import org.springframework.stereotype.Component;

/**
 * @author hsuhau
 * @date 2020/6/11 4:19
 */
@Component
public class PaymentFallbackService implements PaymentHystrixService{
    @Override
    public String paymentInfoOK(Integer id) {
        return "--------PaymentFallbackService fall back back-paymentInfoOK, /(ㄒoㄒ)/~~ ";
    }

    @Override
    public String paymentInfoTimeout(Integer id) {
        return "--------PaymentFallbackService fall back back-paymentInfoTimeout, /(ㄒoㄒ)/~~ ";
    }
}
