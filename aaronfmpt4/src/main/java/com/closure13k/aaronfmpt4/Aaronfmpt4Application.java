package com.closure13k.aaronfmpt4;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Aaronfmpt4Application {
    
    public static void main(String[] args) {
        SpringApplication.run(Aaronfmpt4Application.class, args);
    }
    
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Tourism Agency API")
                .version("0.0.1")
                .description("API for a tourism agency."));
    }
    
    
}
