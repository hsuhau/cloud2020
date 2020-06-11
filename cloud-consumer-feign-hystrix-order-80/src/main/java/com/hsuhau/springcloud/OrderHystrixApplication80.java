package com.hsuhau.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author hsuhau
 * @date 2020/6/11 1:37
 */
@SpringBootApplication
@EnableFeignClients
@EnableHystrix
public class OrderHystrixApplication80 {
    public static void main(String[] args) {
        SpringApplication.run(OrderHystrixApplication80.class, args);
    }
}
