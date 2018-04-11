package no.fint.relations.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.fint.relations.FintResourceCompatibility;
import no.fint.relations.internal.FintLinkMapper;
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
    public FintResourceCompatibility fintResourceCompatibility() { return new FintResourceCompatibility(); }
}
