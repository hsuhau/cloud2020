package com.hsuhau.springcloud.alibaba.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.hsuhau.springcloud.alibaba.handler.CustomerBlockHandler;
import com.hsuhau.springcloud.entities.CommonResult;
import com.hsuhau.springcloud.entities.Payment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hsuhau
 * @date 2020/6/16 7:34
 */
@RestController
public class RateLimitController {
    @GetMapping("/byResource")
    @SentinelResource(value = "byResource", blockHandler = "handleException")
    public CommonResult byResource() {
        return new CommonResult(200, "按资源名称限流测试OK", new Payment(2020L, "serial001"));
    }

    public CommonResult handleException(BlockException e) {
        return new CommonResult(444, e.getClass().getCanonicalName() + "\t 服务不可用");
    }


    @GetMapping("/rateLimit/byUrl")
    @SentinelResource(value = "byUrl")
    public CommonResult byUrl() {
        return new CommonResult(200, "按url名称限流测试OK", new Payment(2020L, "serial002"));
    }

    @GetMapping("/rateLimit/customerBlockHandler")
    @SentinelResource(
            value = "customerBlockHandler",
            blockHandlerClass = CustomerBlockHandler.class,
            blockHandler = "handlerException2"
    )
    public CommonResult customerBlockHandler() {
        return new CommonResult(200, "按客户自定义 ", new Payment(2020L, "serial003"));
    }
}
