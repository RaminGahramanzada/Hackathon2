package com.easyfin.openbanking.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * OpenAPI/Swagger configuration for API documentation
 */
@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI easyFinOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("EasyFin Open Banking API")
                        .description("Financial management and tax optimization API for small business owners in Azerbaijan. " +
                                "Designed specifically for restaurants and small businesses with employees. " +
                                "Supports sadələşdirilmiş vergi (simplified tax) for micro-entrepreneurs.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("EasyFin Team")
                                .email("support@easyfin.az")
                                .url("https://easyfin.az"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(Arrays.asList(
                        new Server().url("http://localhost:8080").description("Local Development Server"),
                        new Server().url("https://api.easyfin.az").description("Production Server")
                ))
                .tags(Arrays.asList(
                        new Tag().name("Dashboard").description("Dashboard and overview endpoints"),
                        new Tag().name("Transactions").description("Transaction management endpoints"),
                        new Tag().name("Categories").description("Spending categorization endpoints"),
                        new Tag().name("Tax Management").description("Tax calculation and deduction endpoints"),
                        new Tag().name("Employees").description("Employee management endpoints"),
                        new Tag().name("Payroll").description("Payroll processing endpoints"),
                        new Tag().name("Cash Flow").description("Cash flow forecasting endpoints"),
                        new Tag().name("Alerts").description("Intelligent alert endpoints"),
                        new Tag().name("Recommendations").description("Actionable recommendation endpoints"),
                        new Tag().name("Reports").description("PDF and export endpoints"),
                        new Tag().name("Integrations").description("External integration endpoints (DVX, eGov, ASAN İmza)"),
                        new Tag().name("Business Profile").description("Business profile management endpoints")
                ));
    }
}

