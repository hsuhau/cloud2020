package com.hsuhau.springcloud.alibaba.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.hsuhau.springcloud.alibaba.service.PaymentFallbackService;
import com.hsuhau.springcloud.entities.CommonResult;
import com.hsuhau.springcloud.entities.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author hsuhau
 * @date 2020/6/16 8:38
 */
@RestController
@Slf4j
public class CircleBreakerController {
    public static final String SERVICE_URL = "http://nacos-payment-provider";

    @Resource
    private RestTemplate restTemplate;

    @GetMapping("/consumer/fallback/{id}")
//    @SentinelResource(value = "fallback") // 没有配置
//    @SentinelResource(value = "fallback", fallback = "handlerFallback") // fallback只负责业务异常
//    @SentinelResource(value = "fallback", blockHandler = "blockHandler") // fallback只负责sentinel控制台配置违规, blockHandler 属性没有起作用?/(ㄒoㄒ)/~~
//    @SentinelResource(value = "fallback", fallback = "handlerFallback", blockHandler = "blockHandler") // blockHandler 属性没有起作用?/(ㄒoㄒ)/~~
    @SentinelResource(value = "fallback",
            fallback = "handlerFallback",
            blockHandler = "blockHandler",
            exceptionsToIgnore = IllegalArgumentException.class) // blockHandler 属性没有起作用?/(ㄒoㄒ)/~~
    public CommonResult<Payment> fallback(@PathVariable Long id) {
        CommonResult<Payment> result = restTemplate.getForObject(SERVICE_URL + "/paymentSQL/" + id, CommonResult.class, id);
        if (id == 4L) {
            throw new IllegalArgumentException("IllegalArgumentException, 非法参数异常.....");
        } else if (result.getData() == null) {
            throw new NullPointerException("NullPointerException, 该ID没有对应记录,空指针异常");
        }
        return result;
    }

    public CommonResult handlerFallback(@PathVariable Long id, Throwable throwable) {
        Payment payment = new Payment(id, "null");
        return new CommonResult<>(444, "兜底异常handlerFallback, exception内容 " + throwable.getMessage(), payment);
    }

    public CommonResult blockHandler(@PathVariable Long id, BlockException e) {
        Payment payment = new Payment(id, "null");
        return new CommonResult<>(445, "blockHandler-sentinel 限流, 无此流水: blockException: " + e.getMessage(), payment);
    }

    @Autowired
    private PaymentFallbackService paymentFallbackService;

    @GetMapping(value = "/consumer/paymentSQL/{id}")
    public CommonResult<Payment> paymentSQL(@PathVariable Long id) {
        return paymentFallbackService.paymentSQL(id);
    }
}
