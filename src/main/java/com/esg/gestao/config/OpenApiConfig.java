package com.esg.gestao.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Gestão de Resíduos e Reciclagem API",
                version = "1.0.0",
                description = "API RESTful para gerenciamento de resíduos, coletas e reciclagem com foco em ESG",
                contact = @Contact(
                        name = "Equipe ESG",
                        email = "contato@gestao-residuos.com"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8080/api", description = "Servidor Local"),
                @Server(url = "https://api.gestao-residuos.com/api", description = "Servidor Produção")
        }
)
@SecurityScheme(
        name = "bearer-token",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Token JWT para autenticação. Obtenha o token através do endpoint /auth/login"
)
public class OpenApiConfig {
}