package com.duofuen.repair;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
/*(scanBasePackages = "com.duofuen.repair")*/
//@EnableJpaRepositories("com.duofuen.repair.domain.dao")
//@EntityScan("com.duofuen.repair.db.entity")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
