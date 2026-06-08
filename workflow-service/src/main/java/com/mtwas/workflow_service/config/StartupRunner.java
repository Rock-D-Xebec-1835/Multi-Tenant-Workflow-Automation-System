package com.mtwas.workflow_service.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner {

    @Override
    public void run(String... args) {
        System.out.println(System.getenv("DB_PASSWORD"));
    }
}