package com.hsuhau.springcloud.alibaba.service.impl;

import com.hsuhau.springcloud.alibaba.dao.OrderDao;
import com.hsuhau.springcloud.alibaba.entity.Order;
import com.hsuhau.springcloud.alibaba.service.AccountService;
import com.hsuhau.springcloud.alibaba.service.OrderService;
import com.hsuhau.springcloud.alibaba.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hsuhau
 * @date 2020/6/17 4:03
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderDao orderDao;

    @Resource
    private StorageService storageService;

    @Resource
    private AccountService accountService;

    /**
     * 创建订单-》调用库存服务扣减库存-》调用账户服务扣减账户余额-》修改订单状态
     * 简单说：
     * 下订单-》减库存-》减余额-》该状态
     *
     * @param order
     */
    public void create(Order order) {
        // 1.创建订单
        log.info("--------->开始新建订单");
        orderDao.create(order);
        // 2.扣减库存
        log.info("---------->订单微服务开始调用库存，做扣减Count，开始");
        storageService.decrease(order.getProductId(), order.getCount());
        log.info("---------->订单微服务开始调用库存，做扣减Count，结束");
        // 3.扣减账户
        log.info("---------->订单微服务开始调用账户，做扣减Money，开始");
        accountService.decrease(order.getUserId(), order.getMoney());
        log.info("---------->订单微服务开始调用账户，做扣减Money，结束");
        // 4.修改订单的状态，从0到1，1代表已经完成
        log.info("-------->修改订单装填开始");
        orderDao.update(order.getUserId(), 0);
        log.info("-------->修改订单装填结束");

        log.info("------>下订单结束了，(*^_^*)哈哈~");
    }
}
