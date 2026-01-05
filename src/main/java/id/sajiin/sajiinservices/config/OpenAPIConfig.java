package id.sajiin.sajiinservices.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Sajiin Engineering Team",
                        email = "fahmi.hidayatullah12@gmail.com",
                        url = "https://sajiin.id"
                ),
                description = "Open API Documentation for Sajiin API Services",
                title = "Sajiin API Services",
                version = "1.0",
                license = @License(
                        name = "MIT License",
                        url = "https://choosealicense.com/licenses/mit/"
                ),
                termsOfService = "Terms of Service"
        ),
        servers = {
                @Server(
                        description = "Current Environment",
                        url = "${sajiin.server-url}"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT Auth Token",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenAPIConfig {
}
