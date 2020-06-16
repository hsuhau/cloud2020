package com.hsuhau.springcloud.alibaba.controller;

import com.hsuhau.springcloud.entities.CommonResult;
import com.hsuhau.springcloud.entities.Payment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author hsuhau
 * @date 2020/6/16 8:31
 */
@RestController
public class PaymentController {
    @Value("${server.port}")
    private String serverPort;

    public static HashMap<Long, Payment> hashMap = new HashMap<>();
    static
    {
        hashMap.put(1L, new Payment(1L, "7239423hixchf9sdyf"));
        hashMap.put(1L, new Payment(2L, "2938423hdf9yw0r239"));
        hashMap.put(1L, new Payment(3L, "dfhdof9r3hkfhwefos"));
    }

    @GetMapping(value = "paymentSQL/{id}")
    public CommonResult<Payment> paymentSQL(@PathVariable Long id) {
        Payment payment = hashMap.get(id);
        CommonResult<Payment> result = new CommonResult(200, "from mysql, serverPort: " + serverPort, payment);
        return result;
    }
}

