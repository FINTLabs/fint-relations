package no.fint.relations.integration.testutils;

import no.fint.relations.integration.testutils.dto.Person;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class TestConfiguration {

    @Qualifier("linkMapper")
    @Bean
    public Map<String, String> linkMapper() {
        Map<String, String> links = new HashMap<>();
        links.put(Person.class.getName(), "http://my-test-url");
        return links;
    }

}
