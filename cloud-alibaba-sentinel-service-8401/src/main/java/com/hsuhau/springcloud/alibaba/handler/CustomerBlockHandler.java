package com.hsuhau.springcloud.alibaba.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.hsuhau.springcloud.entities.CommonResult;

/**
 * @author hsuhau
 * @date 2020/6/16 7:54
 */
public class CustomerBlockHandler {
    public static CommonResult handlerException(BlockException e) {
        return new CommonResult(444, "按客户自定义, global handlerException--1");
    }
    public static CommonResult handlerException2(BlockException e) {
        return new CommonResult(444, "按客户自定义, global handlerException--2");
    }
}
