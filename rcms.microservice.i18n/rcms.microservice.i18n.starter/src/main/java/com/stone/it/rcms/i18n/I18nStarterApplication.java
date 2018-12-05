package com.stone.it.rcms.i18n;

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
public class I18nStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(I18nStarterApplication.class, args);
    }

}
