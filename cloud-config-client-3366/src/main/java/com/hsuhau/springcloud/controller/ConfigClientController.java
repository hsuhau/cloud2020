package com.hsuhau.springcloud.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hsuhau
 * @date 2020/6/13 8:17
 */
@RestController
@RefreshScope
public class ConfigClientController {

    @Value("${server.port}")
    private String serverPort;

    @Value("${config.info}")
    private String configInfo;

    @GetMapping("/config-info")
    public String getConfigInfo() {
        return "serverPort: " + serverPort + "\t\n\n" + "configInfo: " + configInfo;
    }
}
