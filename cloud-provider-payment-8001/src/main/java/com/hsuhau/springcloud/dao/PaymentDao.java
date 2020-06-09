package com.hsuhau.springcloud.dao;

import com.hsuhau.springcloud.entities.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author hsuhau
 * @date 2020/6/9 1:14
 */
@Mapper
public interface PaymentDao {
    int create(Payment payment);

    Payment getPaymentId(@Param("id") Long id);
}
