package no.fint.relations.config;

import no.fint.relations.FintSelfIdAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FintRelationsConfig {
    @Bean
    public FintSelfIdAspect getFintRoleAspect() {
        return new FintSelfIdAspect();
    }
}
