package com.example.newsrest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication //аннотация, помечающая, что это приложение Spring Boot
public class NewsRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewsRestApplication.class, args); //запуск приложения
    }

}
