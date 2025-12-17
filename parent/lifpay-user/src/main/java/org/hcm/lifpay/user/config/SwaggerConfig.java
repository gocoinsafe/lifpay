package org.hcm.lifpay.user.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger 配置类
 * 使用 springdoc-openapi + knife4j（兼容 Spring Boot 2.6+）
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("user服务API文档")
                        .description("Swagger3 接口文档（Spring Boot 2.6.11）")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Lifpay Team")));
    }
}
