package com.esg.gestao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class GestaoResiduosApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestaoResiduosApplication.class, args);
    }
}