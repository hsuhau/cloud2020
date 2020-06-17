package com.hsuhau.springcloud.alibaba.controller;

import com.hsuhau.springcloud.alibaba.entity.Order;
import com.hsuhau.springcloud.alibaba.service.impl.OrderServiceImpl;
import com.hsuhau.springcloud.entities.CommonResult;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hsuhau
 * @date 2020/6/17 4:21
 */
@RestController
public class OrderController {
    @Resource
    private OrderServiceImpl orderService;

    public CommonResult create(Order order) {
        orderService.create(order);
        return new CommonResult(200, "订单创建成功");
    }
}
