package com.hsuhau.springcloud.alibaba.dao;

import com.hsuhau.springcloud.alibaba.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author hsuhau
 */
@Mapper
public interface OrderDao {

    /**
     * 1.新建订单
     * @param record
     * @return
     */
    int create(@Param("order") Order order);

    /**
     * 2. 修改订单状态，从零改为1
     * @param userId
     * @param status
     * @return
     */
    int update(Long userId, Integer status);
}