package com.hsuhau.springcloud.alibaba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author hsuhau
 * @date 2020/6/16 8:25
 */
@SpringBootApplication
@EnableDiscoveryClient
public class PaymentApplication9004 {
    public static void main(String[] args) {
        SpringApplication.run(PaymentApplication9004.class, args);
    }
}
