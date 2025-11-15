package com.easyfin.openbanking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * EasyFin Open Banking Application
 * Financial management and tax optimization for small business owners in Azerbaijan
 * 
 * @author EasyFin Team
 * @version 1.0.0
 */
@SpringBootApplication
public class OpenBankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenBankingApplication.class, args);
        System.out.println("\n=================================");
        System.out.println("EasyFin Open Banking API Started");
        System.out.println("Swagger UI: http://localhost:8080/swagger-ui.html");
        System.out.println("H2 Console: http://localhost:8080/h2-console");
        System.out.println("=================================\n");
    }
}

