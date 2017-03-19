package no.fint.relations.config;

import no.fint.relations.FintRelationAspect;
import no.fint.relations.relations.hal.FintMappers;
import no.fint.relations.relations.hal.FintRelationHal;
import no.fint.relations.relations.hal.HalResourceLinks;
import no.fint.relations.relations.jsonld.FintRelationJsonLd;
import no.fint.relations.relations.hal.SpringHateoasIntegration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FintRelationsConfig {

    @Bean
    public SpringHateoasIntegration springHateoasIntegration() {
        return new SpringHateoasIntegration();
    }

    @Bean
    public FintMappers fintMappers() {
        return new FintMappers();
    }

    @Bean
    public HalResourceLinks fintResourceLinks() {
        return new HalResourceLinks();
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
