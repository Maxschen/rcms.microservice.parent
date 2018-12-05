package com.stone.it.rcms.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @Author cj
 * @Description: TODO
 * @Date 2018/12/4 7:55 PM
 */

@EnableDiscoveryClient
@SpringBootApplication
public class UserStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserStarterApplication.class, args);
    }

}
