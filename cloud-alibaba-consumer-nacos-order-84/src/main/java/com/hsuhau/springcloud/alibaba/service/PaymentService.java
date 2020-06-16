package com.hsuhau.springcloud.alibaba.service;

import com.hsuhau.springcloud.entities.CommonResult;
import com.hsuhau.springcloud.entities.Payment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author hsuhau
 * @date 2020/6/16 9:33
 */
@FeignClient(value = "nacos-payment-provider", fallback = PaymentFallbackService.class)
public interface PaymentService {
    @GetMapping(value = "/paymentSQL/{id}")
    CommonResult<Payment> paymentSQL(@PathVariable Long id);
}
