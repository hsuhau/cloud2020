package com.hsuhau.rule;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;

/**
 * @author hsuhau
 * @date 2020/6/10 1:04
 */
public class SelfRule {
    @Bean
    public IRule rule() {
        // 定义为随机
        return new RandomRule();
    }

}
