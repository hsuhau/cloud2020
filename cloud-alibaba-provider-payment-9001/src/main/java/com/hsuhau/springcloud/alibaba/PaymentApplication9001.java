package com.hsuhau.springcloud.alibaba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author hsuhau
 * @date 2020/6/14 3:58
 */
@SpringBootApplication
@EnableDiscoveryClient
public class PaymentApplication9001 {
    public static void main(String[] args) {
        SpringApplication.run(PaymentApplication9001.class, args);
    }
}
