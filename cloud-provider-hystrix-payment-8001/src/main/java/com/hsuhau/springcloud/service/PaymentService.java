package com.hsuhau.springcloud.service;

import cn.hutool.core.util.IdUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.TimeUnit;

/**
 * @author hsuhau
 * @date 2020/6/11 0:41
 */
@Service
public class PaymentService {
    /**
     * 正常访问，肯定OK
     *
     * @param id
     * @return
     */
    public String paymentInfoOK(Integer id) {
        return "线程池： " + Thread.currentThread().getName() + " paymentInfoOK, id: " + id + "\t" + "O(∩_∩)O哈哈~";
    }

    @HystrixCommand(fallbackMethod = "paymentInfoTimeoutHandler", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
    })
    public String paymentInfoTimeout(Integer id) {
//        int age = 10 / 0;
        int timeNumber = 5;
        try {
            TimeUnit.SECONDS.sleep(timeNumber);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "线程池： " + Thread.currentThread().getName() + " paymentInfoTimeout, id: " + id + "\t" + "O(∩_∩)O哈哈~" + " 耗时" + timeNumber + "秒";
    }

    public String paymentInfoTimeoutHandler(Integer id) {
        return "线程池： " + Thread.currentThread().getName() + " 8001系统繁忙或者运行报错，请稍后再试, id: " + id + "\t" + "/(ㄒoㄒ)/~~";
    }

    // 服务熔断 在10秒的时间窗口期内，10次访问，失败率达到60%，跳闸
    @HystrixCommand(fallbackMethod = "paymentCircuitBreakerFallback", commandProperties = {
            // 是否开启断路器
            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),
            // 请求次数
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
            // 时间窗口期
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),
            // 失败率达到多少后跳闸
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60"),
    })

    public String paymentCircuitBreaker(@PathVariable("id") Integer id) {
        if (id < 0) {
            throw new RuntimeException("*******id 不能负数");
        }
        String serialNumber = IdUtil.simpleUUID();
        return Thread.currentThread().getName() + "\t" + "调用成功，流水号： " + serialNumber;
    }

    public String paymentCircuitBreakerFallback(@PathVariable("id") Integer id) {
        return "id 不能负数，请稍后再试，/(ㄒoㄒ)/~~ id： " + id;
    }
}
