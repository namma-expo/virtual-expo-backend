package com.nammaexpo;

import com.nammaexpo.utils.SerDe;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.nammaexpo")
public class VirtualExpoApplication {

    public static void main(String[] args) {
        SpringApplication.run(VirtualExpoApplication.class, args);
        SerDe.init();
    }

}