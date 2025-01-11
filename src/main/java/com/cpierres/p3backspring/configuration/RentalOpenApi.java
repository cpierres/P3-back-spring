package com.cpierres.p3backspring.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RentalOpenApi {

    public OpenAPI rentalOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Rental API")
                        .description("Documentation de l'API pour la gestion des locations")
                        .version("v1.0.0")
                        .license(new License()
                                .name("Spring Framework: Apache License 2.0, MySQL: GPLv2")
                                .url("https://www.apache.org/licenses/LICENSE-2.0"))
                        .contact(new Contact()
                                .name("Christophe Pierrès")
                                .email("cpierres[at]hotmail.com")
                        )
                );
    }
}
