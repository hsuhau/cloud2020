package com.hsuhau.springcloud.alibaba.service;

import com.hsuhau.springcloud.alibaba.entity.Order;
import org.springframework.stereotype.Service;

@Service
public interface OrderService {
    void create(Order order);
}
