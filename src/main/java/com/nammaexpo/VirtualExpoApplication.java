package com.nammaexpo;

//import de.codecentric.boot.admin.server.config.EnableAdminServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.nammaexpo")
public class VirtualExpoApplication {
    public static void main(String[] args) {
        SpringApplication.run(VirtualExpoApplication.class, args);
    }
}