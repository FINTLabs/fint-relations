package no.fint.relations.integration.testutils;

import com.google.common.collect.ImmutableMap;
import no.fint.relations.integration.testutils.dto.CityResource;
import no.fint.relations.integration.testutils.dto.PersonResource;
import no.fint.relations.internal.FintLinkMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class TestConfiguration {

    @Qualifier("linkMapper")
    @Bean
    public Map<String, String> linkMapper() {
        return ImmutableMap.of(
                FintLinkMapper.getName(PersonResource.class), "http://my-test-url",
                FintLinkMapper.getName(CityResource.class), "http://city-test-url"
        );
    }

}
