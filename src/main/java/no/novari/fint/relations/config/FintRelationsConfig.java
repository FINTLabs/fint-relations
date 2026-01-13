package no.novari.fint.relations.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.novari.fint.relations.FintResourceCompatibility;
import no.novari.fint.relations.internal.FintLinkMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

@Configuration
public class FintRelationsConfig {

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
    public FintLinkMapper fintLinkMapper(Environment environment, FintRelationsProps props) {
        return new FintLinkMapper(environment, props);
    }

    @Bean
    public FintResourceCompatibility fintResourceCompatibility() {
        return new FintResourceCompatibility();
    }
}
