package com.hsuhau.springcloud.alibaba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author hsuhau
 * @date 2020/6/14 4:17
 */
@SpringBootApplication
@EnableDiscoveryClient
public class PaymentApplication9002 {
    public static void main(String[] args) {
        SpringApplication.run(PaymentApplication9002.class, args);
    }
}
