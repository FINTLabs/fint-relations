package no.fint.relations.config;

import no.fint.relations.FintRelationAspect;
import no.fint.relations.relations.FintRelationHal;
import no.fint.relations.relations.FintRelationJsonLd;
import no.fint.relations.relations.SpringHateoasIntegration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FintRelationsConfig {

    @Bean
    public SpringHateoasIntegration springHateoasIntegration() {
        return new SpringHateoasIntegration();
    }

    @Bean
    public FintRelationHal fintRelationHal() {
        return new FintRelationHal();
    }

    @Bean
    public FintRelationJsonLd fintRelationJsonLd() {
        return new FintRelationJsonLd();
    }

    @Bean
    public FintRelationAspect fintRelationAspect() {
        return new FintRelationAspect();
    }
}
