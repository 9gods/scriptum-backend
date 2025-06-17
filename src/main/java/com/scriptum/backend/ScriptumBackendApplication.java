package com.scriptum.backend;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class ScriptumBackendApplication {

    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(ScriptumBackendApplication.class, args);
    }

}