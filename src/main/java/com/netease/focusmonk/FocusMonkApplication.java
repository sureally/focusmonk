package com.netease.focusmonk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.netease.focusmonk.dao")
public class FocusMonkApplication {

    public static void main(String[] args) {
        SpringApplication.run(FocusMonkApplication.class, args);
    }

}
