package no.fint.relations.integration.testutils;

import com.google.common.collect.ImmutableMap;
import no.fint.relations.internal.FintLinkMapper;
import no.fint.relations.integration.testutils.dto.Person;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class TestConfiguration {

    @Qualifier("linkMapper")
    @Bean
    public Map<String, String> linkMapper() {
        return ImmutableMap.of(FintLinkMapper.getName(Person.class), "http://my-test-url");
    }

}
