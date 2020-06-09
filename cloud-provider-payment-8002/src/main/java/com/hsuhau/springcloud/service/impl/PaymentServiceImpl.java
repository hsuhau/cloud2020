package com.hsuhau.springcloud.service.impl;

import com.hsuhau.springcloud.dao.PaymentDao;
import com.hsuhau.springcloud.entities.Payment;
import com.hsuhau.springcloud.service.PaymentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author hsuhau
 * @date 2020/6/9 1:43
 */
@Service
public class PaymentServiceImpl implements PaymentService {
    @Resource
    private PaymentDao paymentDao;

    @Override
    public int create(Payment payment) {
        int i = 0;
        return paymentDao.create(payment);
    }

    @Override
    public Payment getPaymentById(Long id) {
        return paymentDao.getPaymentId(id);
    }
}
