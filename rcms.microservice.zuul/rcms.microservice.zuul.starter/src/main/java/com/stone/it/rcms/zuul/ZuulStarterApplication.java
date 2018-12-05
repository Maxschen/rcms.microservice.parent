package com.stone.it.rcms.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @Author cj
 * @Description: TODO
 * @Date 2018/12/4 7:36 PM
 */

@EnableZuulProxy
@EnableDiscoveryClient
@SpringBootApplication
public class ZuulStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZuulStarterApplication.class, args);
    }

}
