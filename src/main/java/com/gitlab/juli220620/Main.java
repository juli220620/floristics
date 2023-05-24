package com.gitlab.juli220620;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.gitlab.juli220620")
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}