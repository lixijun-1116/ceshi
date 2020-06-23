package com.example.demologin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.example.demologin.dao")
public class DemoLoginApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoLoginApplication.class, args);
    }

}
