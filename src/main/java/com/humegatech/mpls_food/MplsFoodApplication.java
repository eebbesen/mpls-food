package com.humegatech.mpls_food;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@EnableCaching
public class MplsFoodApplication {

    public static void main(String[] args) {
        SpringApplication.run(MplsFoodApplication.class, args);
    }

}
