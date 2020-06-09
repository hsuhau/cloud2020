package com.hsuhau.springcloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author hsuhau
 * @date 2020/6/9 22:24
 */
@SpringBootApplication
@EnableDiscoveryClient
public class OrderZKApplication80 {
    public static void main(String[] args) {
        SpringApplication.run(OrderZKApplication80.class, args);
    }
}
