package com.cpierres.p3backspring.configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Classe de configuration pour l'intégration de Cloudinary.
 * Le bean Cloudinary défini facilite le téléchargement, la gestion et la suppression des ressources multimédias
 * en fournissant une configuration centralisée pour l'accès à l'API Cloudinary.
 * Cette classe lit les informations d'identification Cloudinary requises (nom du cloud, clé API et secret API)
 * à partir des propriétés de l'application et définit un bean Cloudinary pour une utilisation à l'échelle de
 * l'application.
 * Les informations d'identification sont injectées à l'aide de l'annotation @Value de Spring, permettant la
 * récupération dynamique des valeurs configurées à partir des clés présentes dans <code>application.properties</code>,
 * sachant qu'elles-mêmes ont des correspondances avec des variables d'environnement.
 */
@Configuration
public class CloudinaryConfig {
    @Value("${cloudinary.cloud_name}")
    private String cloudName;

    @Value("${cloudinary.api_key}")
    private String apiKey;

    @Value("${cloudinary.api_secret}")
    private String apiSecret;

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret
        ));
    }
}
