package com.stone.it.rcms.admin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author cj
 * @Description: TODO
 * @Date 2018/12/4 7:32 PM
 */
@SpringBootApplication
@EnableAdminServer
public class AdminStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminStarterApplication.class, args);
    }
}
