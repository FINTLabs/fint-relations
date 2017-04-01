package no.fint.relations.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.fint.relations.FintRelationAspect;
import no.fint.relations.relations.FintLinkMapper;
import no.fint.relations.relations.hal.FintRelProvider;
import no.fint.relations.relations.hal.FintRelationHal;
import no.fint.relations.relations.hal.SpringHateoasIntegration;
import no.fint.relations.relations.jsonld.FintRelationJsonLd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class FintRelationsConfig {

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Bean
    public FintRelationsProps fintRelationsProps() {
        return new FintRelationsProps();
    }

    @Bean
    public FintLinkMapper fintLinkMapper() {
        return new FintLinkMapper();
    }

    @Bean
    public SpringHateoasIntegration springHateoasIntegration() {
        return new SpringHateoasIntegration();
    }

    @Bean
    public FintRelProvider fintRelProvider() {
        return new FintRelProvider();
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
