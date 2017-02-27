package no.fint.relations.config;

import no.fint.relations.rel.FintRelationAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FintRelationsConfig {

    @Bean
    public FintRelationAspect fintRelationAspect() {
        return new FintRelationAspect();
    }
}
