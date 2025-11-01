package com.inditex.price.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

/**
 * Configuración de OpenAPI/Swagger para documentación de la API
 * Proporciona información detallada sobre los endpoints, modelos y ejemplos
 */
@Configuration
public class OpenApiConfig implements WebMvcConfigurer {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Price Service API")
                        .version("1.0.0")
                        .description("API REST para consulta de precios de productos de Inditex. " +
                                "Permite obtener el precio aplicable de un producto para una marca específica en una fecha determinada, " +
                                "aplicando las reglas de negocio de prioridad y vigencia.")
                        .termsOfService("https://www.inditex.com/terms")
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT"))
                        .contact(new Contact()
                                .name("Equipo de Desarrollo Inditex")
                                .email("desarrollo@inditex.com")
                                .url("https://www.inditex.com")))
                .addServersItem(new Server()
                        .url("http://localhost:8080")
                        .description("Servidor de desarrollo"))
                .addServersItem(new Server()
                        .url("https://api.inditex.com")
                        .description("Servidor de producción"));
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(false)
                .maxAge(3600);
    }
}