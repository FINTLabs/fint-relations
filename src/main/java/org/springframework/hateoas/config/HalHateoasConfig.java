package org.springframework.hateoas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.hal.HalConfiguration;

import static org.springframework.hateoas.hal.HalConfiguration.RenderSingleLinks.AS_ARRAY;

@Configuration
@Import(HateoasConfiguration.class)
public class HalHateoasConfig {

    @Bean
    public HalConfiguration halConfiguration() {
        return new HalConfiguration().withRenderSingleLinks(AS_ARRAY);
    }
}
