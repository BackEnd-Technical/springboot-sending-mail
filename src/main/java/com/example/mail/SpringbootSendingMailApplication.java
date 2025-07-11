package com.example.mail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;

@SpringBootApplication
public class SpringbootSendingMailApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootSendingMailApplication.class, args);
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info().title("API Ứng dụng của Tôi").version("1.0.0")
                .description("Tài liệu cho RESTful API của ứng dụng.").termsOfService("http://swagger.io/terms/")
                .contact(new Contact().name("Đội ngũ Hỗ trợ").email("support@example.com"))
                .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .components(new Components().addSecuritySchemes("bearerAuth",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                                .description("Nhập JWT Bearer Token của bạn vào đây")));
    }

}
