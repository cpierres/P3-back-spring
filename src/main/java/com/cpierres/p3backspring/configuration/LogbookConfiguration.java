package com.cpierres.p3backspring.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.zalando.logbook.HeaderFilter;
import org.zalando.logbook.HttpLogFormatter;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.Sink;
import org.zalando.logbook.core.Conditions;
import org.zalando.logbook.core.HeaderFilters;
import org.zalando.logbook.json.JsonHttpLogFormatter;

import java.util.ArrayList;
import java.util.List;


@Configuration
public class LogbookConfiguration {

    @Bean
    public Logbook logbook() {
        List<HeaderFilter> headerFilters = new ArrayList<>();
        //headerFilters.add(HeaderFilters.defaultValue());
        //headerFilters.add(HeaderFilters.authorization()); // Masque l'entête authorization

        //note : je souhaite volontairement afficher authorization
        //le seul moyen que j'ai trouvé est d'ajouter un filtre manuellement (sinon authorization tjs caché)
        headerFilters.add(HeaderFilters.replaceHeaders("Set-Cookie", "****"));

        return Logbook.builder()
//                .condition(Conditions.exclude(
//                        // Exclure certains chemins des logs
//                        Paths.startsWith("/actuator"),
//                        Paths.startsWith("/admin")
//                ))
                .headerFilters(headerFilters)
                .build();
    }

}
