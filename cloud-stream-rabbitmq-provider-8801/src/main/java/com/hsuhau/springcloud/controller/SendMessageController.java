package com.hsuhau.springcloud.controller;

import com.hsuhau.springcloud.service.impl.MessageProviderImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author hsuhau
 * @date 2020/6/13 20:22
 */
@RestController
public class SendMessageController {

    @Resource
    private MessageProviderImpl provider;

    @GetMapping("/send-message")
    public String sendMessage() {
        return provider.send();
    }
}
